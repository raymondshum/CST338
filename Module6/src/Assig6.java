import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Main class that creates the new BuildGameModel, BuildGameView and 
 * BuildGameController objects.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
public class Assig6
{
   static final int NUM_PACKS = 1; //Packs in deck for Build
   static final int NUM_JOKERS_PER_PACK = 2; //Jokers per pack in Build
   static final int NUM_UNUSED_CARDS_PER_PACK = 0; //No unused cards in Build
   static final Card UNUSED_CARDS_PER_PACK[] = null; //No unused cards to hold
   static final int NUM_PLAYERS = 2; //Number of players in Build
   static final int NUM_CARDS_PER_HAND = 7; //Cards per hand in Build

   public static void main(String[] args)
   {     
      BuildGameModel myModel = new BuildGameModel(NUM_PACKS, 
            NUM_JOKERS_PER_PACK, NUM_UNUSED_CARDS_PER_PACK, UNUSED_CARDS_PER_PACK,
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      BuildGameView myView = new BuildGameView(NUM_CARDS_PER_HAND, NUM_PLAYERS);

      //BuildGameModel and BuildGameView references are passed to controller
      BuildGameController myController = 
            new BuildGameController(myModel, myView);
   }

}


/**
 * BuildGameController sits between BuildGameModel and BuildGameView. It
 * accesses and manipulates information from BuildGameModel and uses that to
 * update BuildGameView.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class BuildGameController implements ActionListener
{
   private final int STACK_BUTTON_INDEX = 1000; //Identifier for Stack GUI
   private final int TIMER_BUTTON_INDEX = 2000; //Identifier for Timer GUI
   private final int SKIP_BUTTON_INDEX = 3000;  //Identifier for Skip Button

   private ActionListener buttonEar;
   private BuildGameModel myModel;
   private BuildGameView myView;

   private boolean playerTurn;
   private boolean playerCardIsClicked; //Has player selected card from hand
   private int playerCardSelection; //Index of player selected card in array

   /**
    * Constructor. Begins game once model and view are created.
    * @param model BuildGameModel class to store/retrieve data from
    * @param view BuildGameView class to push visual update info to
    */
   BuildGameController(BuildGameModel model, BuildGameView view)
   {
      buttonEar = this;
      myModel = model;
      myView = view;

      playerTurn = false;
      playerCardIsClicked = false;
      playerCardSelection = -1;

      initializeGame();
   }

   /**
    * ActionListener. Translates button clicks on GUI into actions performed on
    * BuildGameModel. Updates BuildGameView after actions are performed.
    */
   public void actionPerformed(ActionEvent e)
   {


      //Translates button press into integer value, used to identify button 
      int actionSource = Integer.parseInt(e.getActionCommand());

      //player presses timer button
      if(actionSource == TIMER_BUTTON_INDEX)
      {
         myModel.resetTimer(); //Timer is started if stopped and vice versa
      }
      //player presses skip turn button
      else if(actionSource == SKIP_BUTTON_INDEX)
      {
         skipTurn(); //Player turn is skipped
      }
      //player has already selected hand card and then selected stack
      else if(playerTurn && playerCardIsClicked 
            && actionSource >= STACK_BUTTON_INDEX 
            && actionSource < STACK_BUTTON_INDEX + myModel.getNumStacks())
      {
         playerSelectsStack(actionSource); //Player attempts to play card
      }
      //player has already selected a card from his hand
      else if(playerTurn && playerCardIsClicked  
            && actionSource < myModel.getNumCardsPerHand())
      {
         playerSelectsCard(actionSource); //Index of new card is saved
      }
      //player has not selected a card from his hand
      else if(playerTurn && !playerCardIsClicked  
            && actionSource < myModel.getNumCardsPerHand())
      {
         playerSelectsCard(actionSource); //Index of selected card is saved
      }
   }

   /**
    * Helper function for constructor. Begins game.
    */
   private void initializeGame()
   {
      //Computer deals first card and GUI table is updated
      myModel.newGame();
      computerTurn();
      initializeView();

      //Starts ongoing timer GUI update request using private inner class
      Thread updateTimer = new UpdateTimerLabel();
      updateTimer.start();
   }

   /**
    * Refreshes all elements of the myView GUI.
    */
   private void initializeView()
   {
      //Requests data from myModel and calls updates for myView
      myView.updateComputerGUI(myModel.getBackCardIcon());
      myView.updatePlayerGUI(myModel.getPlayerIcons(), buttonEar);
      myView.updatePlayedCardsGUI(myModel.getStackIcons(), buttonEar,
            STACK_BUTTON_INDEX);
      myView.updateScoreGUI(
            myModel.getPlayerSkips(), myModel.getComputerSkips(), 
            myModel.getNumCardsRemainingInDeck(), buttonEar, SKIP_BUTTON_INDEX);
      myView.updateTimerGUI(getTimerLabel(), buttonEar, 
            TIMER_BUTTON_INDEX);
   }

   /**
    * Stores integer value as player's currently selected card from hand.
    * @param actionSource int representing index of card on player hand
    */
   private void playerSelectsCard(int actionSource)
   {
      //saves index of player's card and that player has selected a card
      playerCardSelection = actionSource;
      playerCardIsClicked = true;
      playerTurn = true; //maintains state of player's turn
   }

   /**
    * Logic for computer's movements. Computer will play a card and skip its
    * turn if it cannot.
    */
   private void computerTurn()
   {
      if(!computerPlaysCard())
      {
         skipTurn();
         initializeView();
      }
   }

   /**
    * Checks to see if selected card from player hand can be played to selected
    * card on the play area. Plays the card if it is possible.
    * @param actionSource
    */
   private void playerSelectsStack(int actionSource)
   {
      //translates action source into index of stack on playable area
      int stackIndex = actionSource - STACK_BUTTON_INDEX;
      int playerSelection = playerCardSelection;

      Card playerCard = myModel.inspectHumanHand(playerSelection);
      Card stackCard = myModel.getPlayAreaCard(stackIndex);

      //Player plays card to empty stack, redraws GUI, ends its turn
      if( myModel.getPlayAreaCard(stackIndex) == null)
      {
         myModel.playerPlaysToStack(playerSelection, stackIndex);
         initializeView();
         checkGameOver();
         playerCardIsClicked = false; //clear player card selection
         playerTurn = false; //end player turn
         computerTurn(); //begin computer's turn
      }
      //if the source card can be played on the destination card (non empty)
      else if(cardCanBePlayed(myModel.valueAsInt(playerCard), 
            myModel.valueAsInt(stackCard)))
      {
         //model takes card from player hand and adds it to top of stack
         myModel.playerPlaysToStack(playerSelection, stackIndex);
         initializeView(); //redraw screen
         checkGameOver(); //check if deck is 0, end game if so
         playerCardIsClicked = false; //clear player card selection
         playerTurn = false; //end player turn
         computerTurn(); //begin computer's turn
      }
      else
      {
         //if source card cannot be played, prompt player through messages
         myView.selectionErrorPrompt(
               playerCard.toString(), stackCard.toString());
         myView.hintMessagePrompt(findPlayerMoveHint());
         playerCardIsClicked = false; //clear player selected hand card 
         playerTurn = true; //maintain player turn
      }
   }

   /**
    * Computer attempts to play a card to the stack.
    * @return boolean TRUE if computer is able to play a card, FALSE if not
    */
   private boolean computerPlaysCard()
   {
      int computerCardValue, stackCardValue;
      Card computerCard, stackCard;

      //Loop checks each computer card against each (top) stack card
      for(int card = 0; card < myModel.getNumCardsPerHand(); card++)
      {
         computerCard = myModel.inspectComputerHand(card);
         computerCardValue = myModel.valueAsInt(computerCard);         

         for(int stack = 0; stack < myModel.getNumStacks(); stack++)
         {

            //Computer plays card to empty stack, redraws GUI, ends its turn
            if( myModel.getPlayAreaCard(stack) == null)
            {
               myModel.computerPlaysToStack(card, stack);
               initializeView();
               checkGameOver();
               playerTurn = true; 
               return true;
            }

            stackCard= myModel.getPlayAreaCard(stack);
            stackCardValue = myModel.valueAsInt(stackCard);

            //if card can be played to current stack, computer plays card
            if(cardCanBePlayed(computerCardValue, stackCardValue))
            {
               myModel.computerPlaysToStack(card, stack);
               initializeView(); 
               checkGameOver();
               playerTurn = true; 
               return true;
            }
         }
      }
      return false; //returns false if computer cannot play card to any stack
   }

   /**
    * Helper function. Checks to see if source value is within 1 of destination
    * value. Intended for integer representation of card values to be passed
    * as arguments.
    * @param source int value of card selected from computer or player hand
    * @param destination int value of target card on stack
    * @return TRUE if source card can be played, FALSE if not
    */
   private boolean cardCanBePlayed(int source, int destination)
   {
      //check if source values are within 1 of destination
      return (source == (destination + 1) || source == (destination - 1));
   }

   /**
    * Used to pass turn from player to computer and vice versa. Checks if
    * neither party can move and deals new cards over the stacks if so.
    */
   private void skipTurn()
   {
      //check if either player can make a move and deals to stacks if not
      if(!isStackPlayable()) 
      {
         //if model cannot deal to stacks & deck has less than 3 cards
         if(!myModel.deckDealsToStacks() 
               && myModel.getNumCardsRemainingInDeck() < 3)

            myView.displayGameOverScreen(getGameOverMessage()); //end game
      }

      if(playerTurn) //player skips turn
      {
         myModel.increasePlayerSkips(); //increases player skip counter
         playerTurn = false;
         computerTurn(); //passes turn to computer
      }
      else if(!playerTurn) //computer skips turn
      {
         myModel.increaseComputerSkips(); //increases computer skip counter
         playerTurn = true; //passes turn to player
      }
   }

   /**
    * Checks to see if either player can play any card in hand to any stack. It
    * requests view to notify player if neither party can move.
    * @return TRUE if either player can move and FALSE if neither can
    */
   private boolean isStackPlayable()
   {
      int computerCardValue, playerCardValue, stackCardValue;
      Card computerCard, playerCard, stackCard;

      //loops compare each card in each hand to each top card on each stack
      for(int card = 0; card < myModel.getNumCardsPerHand(); card++)
      {
         computerCard = myModel.inspectComputerHand(card);
         computerCardValue = myModel.valueAsInt(computerCard);
         playerCard = myModel.inspectHumanHand(card);
         playerCardValue = myModel.valueAsInt(playerCard);

         for (int stack = 0; stack < myModel.getNumStacks(); stack++)
         {
            //computer can play over a null (empty) stack
            if(myModel.getPlayAreaCard(stack) == null)
               return true;

            stackCard= myModel.getPlayAreaCard(stack);
            stackCardValue = myModel.valueAsInt(stackCard);

            //player has a valid move
            if(cardCanBePlayed(playerCardValue, stackCardValue))
               return true;

            //computer has a valid move
            if(cardCanBePlayed(computerCardValue, stackCardValue))
               return true;
         }
      }

      myView.neitherCanMove();
      return false;
   }

   /**
    * Provides one of two notifications to player. First, the first valid move 
    * that can be made. Second, if no moves can be made.
    * @return String containing first or second message
    */
   private String findPlayerMoveHint()
   {
      int playerValue, stackValue;
      Card playerCard, stackCard;

      //Loops compare each card in player hand to each card on stack
      for(int i = 0; i < myModel.getNumCardsPerHand(); i++)
      {
         for(int j = 0; j < myModel.getNumStacks(); j++)
         {
            stackCard= myModel.getPlayAreaCard(j);
            playerCard = myModel.inspectHumanHand(i);

            if (stackCard == null)
            {
               return ("You can play " + playerCard + " to an empty stack.");
            }
            else if(stackCard != null) 
            {
               playerValue = myModel.valueAsInt(playerCard);
               stackValue = myModel.valueAsInt(stackCard);

               //returns message if valid move is found
               if(cardCanBePlayed(playerValue, stackValue))
               {
                  return ("You can play " + playerCard + " to " 
                        + stackCard + ".");
               }
            }

         }
      }
      //returns message if no valid moves are found
      return "It appears that you have no valid moves available.";
   }

   /**
    * Checks to see if any cards are left in the deck, ends game if none are.
    * @return TRUE if no cards are left (unreachable), FALSE if cards are left
    */
   private boolean checkGameOver()
   {
      if(myModel.getNumCardsRemainingInDeck() == 0)
      {
         myView.displayGameOverScreen(getGameOverMessage());
         return true;
      }

      return false;
   }

   /**
    * Generates and returns string containing win, loss or tie message.
    * @return String containing post game results
    */
   private String getGameOverMessage()
   {
      int computerSkipCounter = myModel.getComputerSkips();
      int playerSkipCounter = myModel.getPlayerSkips();

      if (computerSkipCounter < playerSkipCounter)
      {
         return ("Sorry! You've lost the game. You've skipped your turn " +
               playerSkipCounter + " times while the computer only skipped its "
               + "turn " + computerSkipCounter + " times.");
      }
      else if(computerSkipCounter > playerSkipCounter)
      {
         return ("Congratulations! You've won the game. You've skipped your" + 
               " turn " + playerSkipCounter + " times while the computer skipped" 
               + " its turn " + computerSkipCounter + " times.");
      }
      else
         return ("You've tied! You both skipped your turns " + 
               computerSkipCounter + " times!");
   }

   /**
    * Processes timer information from model into Minutes:Seconds format and
    * returns as String object.
    * @return String object reprsenting processed seconds information
    */
   private String getTimerLabel()
   {
      int minutes = (int)myModel.getTimerCounter()/60;
      int seconds = myModel.getTimerCounter() % 60;
      return String.format("%d:%02d", minutes, seconds);
   }

   /**
    * Thread class used to requests updates to TimerGUI of myModel
    * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
    *
    */
   private class UpdateTimerLabel extends Thread
   {
      /**
       * Requests second count from myModel and updates myView continuously,
       * even if timer is stopped.
       */
      public void run()
      {
         while(true)
         {
            myView.updateTimerGUI(getTimerLabel(), buttonEar, 
                  TIMER_BUTTON_INDEX);
            doNothing();
         }
      }

      /**
       * Pauses thread for 1 second.
       */
      public void doNothing()
      {
         try
         {
            Thread.sleep(1000);
         }
         catch(InterruptedException e)
         {

         }
      }
   }

}


/**
 * BuildGameView draws the GUI that represents the state of BuildGameModel. It
 * does not interact with BuildGameModel. Instead, BuildGameController calls 
 * BuildGameView methods when it is necessary to update the GUI elements, and 
 * passes any required data.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class BuildGameView extends JFrame
{
   final private int NUM_STACKS = 3; //Number of stacks in the game
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games

   private JLabel[] computerLabels;
   private JButton[] playAreaButtons;
   private JButton[] playerButtons;
   private String skipLabel;

   private JPanel 
   pnlComputerHand, pnlHumanHand, pnlPlayArea, pnlTimer, pnlScore, pnlInfo;

   /**
    * Constructor. Builds framework for "CardTable" GUI and initializes instance
    * variables.
    * @param cardsPerHand
    * @param players
    */
   BuildGameView(int cardsPerHand, int players)
   {
      super("Build Game");
      skipLabel = "I cannot play.";

      computerLabels = new JLabel[cardsPerHand];
      playerButtons = new JButton[cardsPerHand];
      playAreaButtons = new JButton[NUM_STACKS];

      setupTable();
   }
   
   /**
    * Merged from CardTable class. Setup initial panels for Build game GUI.
    */
   private void setupTable()
   {
      //setup initial table
      setSize(1024, 768);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);     
      setLayout(new BorderLayout());

      //Initialize five primary panels
      pnlComputerHand = new JPanel();
      pnlHumanHand = new JPanel();
      pnlPlayArea = new JPanel();
      pnlTimer = new JPanel();
      pnlScore = new JPanel();

      //Set layouts for five panels
      pnlComputerHand.setLayout(new FlowLayout());
      pnlHumanHand.setLayout(new FlowLayout());
      pnlPlayArea.setLayout(new GridLayout(1,3)); //changed to flow for test
      pnlTimer.setLayout(new GridLayout(1, 1));
      pnlScore.setLayout(new GridLayout(4,1));

      //Created titled borders for five panels
      pnlComputerHand.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Computer Hand"));
      pnlHumanHand.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Human Hand"));
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Play Area"));
      pnlTimer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Timer"));
      pnlScore.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Score"));

      //add three panels in orientation described in spec sheet
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlHumanHand, BorderLayout.SOUTH);
      add(pnlTimer, BorderLayout.WEST);
      add(pnlScore, BorderLayout.EAST);
   }

   /**
    * Updates JPanel holding player buttons to represent the player hand at the
    * time the method was called.
    * @param playerIcons Icon[] objects representing cards in the player's hand
    * @param controller ActionLister object processing ActionEvents
    */
   public void updatePlayerGUI(Icon[] playerIcons, ActionListener controller)
   {
      //remove all buttons from the panel
      pnlHumanHand.removeAll();
      String handIndex;

      //create buttons for all cards in the player's hand and add to jpanel
      for (int i = 0; i < playerIcons.length; i++)
      {
         playerButtons[i] = new JButton(playerIcons[i]);
         playerButtons[i].addActionListener(controller);

         //ActionCommand is the toString of integer representing index of card
         playerButtons[i].setActionCommand(Integer.toString(i));
         pnlHumanHand.add(playerButtons[i]);
      }

      //redraw the GUI with the correct elements
      pnlHumanHand.revalidate();
      pnlHumanHand.repaint();
   }

   /**
    * Places card back icons on the computer hand's JPanel for each card in the
    * computer's hand. Technically, this only has to be called once during the
    * game. However, this method can allow for the computer to play their cards
    * face up or for games to have uneven hands.
    */
   public void updateComputerGUI(Icon backCardIcon)
   {
      pnlComputerHand.removeAll();

      //add labels holding the back icon gif for all cards in computer's hand
      for(int i = 0; i < computerLabels.length; i++)
      {
         computerLabels[i] = new JLabel(backCardIcon);
         pnlComputerHand.add(computerLabels[i]);
      }

      pnlComputerHand.revalidate();
      pnlComputerHand.repaint();
   }

   /**
    * Updates buttons in play area to reflect the top card of each stack.
    * @param stackIcons Icon[] object representing top cards of each stack.
    * @param buttonEar ActionListener object processing ActionEvents
    * @param stackIdentifier int representing numeric identifier for buttons
    */
   public void updatePlayedCardsGUI(Icon[] stackIcons, ActionListener buttonEar,
         int stackIdentifier)
   {
      pnlPlayArea.removeAll();
      String stackIndex;

      for (int i = 0; i < playAreaButtons.length; i++)
      {
         stackIndex = Integer.toString(i + stackIdentifier); 
         //"Empty" buttons are placed if no cards are on stack
         if(stackIcons[i] == null)
         {
            playAreaButtons[i] = new JButton("EMPTY");
            playAreaButtons[i].addActionListener(buttonEar);
            playAreaButtons[i].setActionCommand(stackIndex);
            pnlPlayArea.add(playAreaButtons[i]);
         }
         else
         {
            /*Identifier is added to index and converted to string. When 
             * ActionCommand is translated, identifier is subtracted to convert
             * value to index of stack on array.
             */

            playAreaButtons[i] = new JButton(stackIcons[i]);
            playAreaButtons[i].addActionListener(buttonEar);
            playAreaButtons[i].setActionCommand(stackIndex);
            pnlPlayArea.add(playAreaButtons[i]);
         }
      }

      pnlPlayArea.revalidate();
      pnlPlayArea.repaint();
   }

   /**
    * Updates the score panel of myCardTable to represent the turns skipped by
    * the computer and player, the cards remaining in the deck and the skip
    * button.
    * @param playerSkips int representing turns skipped by player
    * @param computerSkips int representing turns skipped by computer
    * @param cardsLeft int representing cards remaining in deck
    * @param buttonEar ActionListener object that processes ActionEvents
    * @param skipIdentifier int representing numeric identifier for skip button
    */
   public void updateScoreGUI(int playerSkips, int computerSkips, int cardsLeft,
         ActionListener buttonEar, int skipIdentifier)
   {
      String playerSkipCounterLabel, computerSkipCounterLabel, deckLabel;

      //Set labels to updated values
      playerSkipCounterLabel = "Skipped turns (Player): " + playerSkips;
      computerSkipCounterLabel = "Skipped turns (Computer): " + computerSkips;
      deckLabel = "Cards in Deck: " + cardsLeft;

      JLabel deckCards = new JLabel(deckLabel);
      JLabel playerSkipCounter = new JLabel(playerSkipCounterLabel);
      JLabel computerSkipCounter = new JLabel(computerSkipCounterLabel);

      //create skip button
      JButton skipButton = new JButton(skipLabel);
      skipButton.addActionListener(buttonEar);
      skipButton.setActionCommand(Integer.toString(skipIdentifier));

      //remove all contents of panel, add updated buttons and redraw
      pnlScore.removeAll();
      pnlScore.add(deckCards);
      pnlScore.add(playerSkipCounter);
      pnlScore.add(computerSkipCounter);
      pnlScore.add(skipButton);
      pnlScore.revalidate();
      pnlScore.repaint();
   }

   /**
    * Updates timer JPanel with passed parameters.
    * @param seconds int representing number of seconds to display 
    * @param buttonEar ActionListener object that processes ActionEvents
    * @param buttonIdentifier int representing numeric identifier for button
    */
   public void updateTimerGUI(String timerLabel, ActionListener buttonEar,
         int buttonIdentifier)
   {
      //creates timer button, removes old button from jpanel and adds new one
      JButton timerButton = new JButton(timerLabel);
      timerButton.addActionListener(buttonEar);
      timerButton.setActionCommand(Integer.toString(buttonIdentifier));
      pnlTimer.removeAll();
      pnlTimer.add(timerButton);
      pnlTimer.revalidate();
      pnlTimer.repaint();
   }

   /**
    * Prompt that is displayed showing that source card cannot be played on
    * destination card. Intent is to call this method when the player plays
    * a card from his hand onto an incorrect one on the stack.
    * @param source String representing card from player hand
    * @param destination String representing target card on stack
    */
   public void selectionErrorPrompt(String source, String destination)
   {
      String message = "You cannot play " + source + " on " + destination 
            + ". Please select a new card from your hand and try again.";

      JOptionPane.showMessageDialog(null, message, "Whoops!", 
            JOptionPane.INFORMATION_MESSAGE);
   }

   /**
    * Displays "Hint" message with passed parameter. Intent is use 
    * findPlayerMoveHint() in BuildGameController to generate the String and
    * call the method.
    * @param hint String generated by findPlayerMoveHint()
    */
   public void hintMessagePrompt(String hint)
   {
      String message = hint;
      JOptionPane.showMessageDialog(null, message, "Hint", 
            JOptionPane.INFORMATION_MESSAGE);
   }

   /**
    * Displays that neither player can play a card to the stack. Intent is to
    * inform the player that cards will be dealt to the stack before doing so.
    */
   public void neitherCanMove()
   {
      String message = "It seems that neither player can move. We'll deal to" +
            " the stack if there are enough cards left in the deck.";
      JOptionPane.showMessageDialog(null, message, "Uh oh!", 
            JOptionPane.INFORMATION_MESSAGE);
   }

   /**
    * Displays "Game Over" box with parameter as the body. Intent is the 
    * BuildGameController processes the message and calls BuildGameView to
    * display it. Exits game after message is confirmed.
    * @param message
    */
   public void displayGameOverScreen(String message)
   {
      JOptionPane.showMessageDialog(null, message, "Game Over!", 
            JOptionPane.INFORMATION_MESSAGE);
      System.exit(0);
   }
}


/**
 * BuildGameModel holds data regarding the game state. BuildGameController
 * uses BuildGameModel's accessors and mutators to access this data. This class
 * does not interact with BuildGameView.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class BuildGameModel
{
   private static final int MAX_PLAYERS = 50;
   private final int HUMAN_PLAYER = 1; //Hand index of the player
   private final int COMPUTER_PLAYER = 0; //Hand index of the computer
   private final int NUM_STACKS = 3; //3 stacks are used for the build game
   
   // 14 = A thru K + joker
   private static Icon[][] iconCards = new ImageIcon[14][4]; 
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   private int playerSkipCounter;
   private int computerSkipCounter;
   private int timerCounter;
   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
                                    // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
                                    // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                        // in the game.  e.g. pinochle does not
                                        // use cards 2-8 of any suit 
   private Hand playAreaStacks[]; //an array of Hands are used contain stacks
   private Timer gameTimer;
  
   /**
    * Constructor. Initializes instance variables and creates a new game using
    * CardGameFramework.
    * @param numPacks int representing packs of cards used
    * @param numJokersPerPack int representing Jokers in each pack
    * @param numUnusedCardsPerPack int representing number of unused cards
    * @param unusedCardsPerPack int[] holding unused cards if they exist
    * @param numPlayers int representing number of players in the  game
    * @param numCardsPerHand int representing number cards in each players hand
    */
   BuildGameModel(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack,  
      Card[] unusedCardsPerPack, int numPlayers, 
      int numCardsPerHand)
   {
      playerSkipCounter = computerSkipCounter = timerCounter = 0;
      playAreaStacks = new Hand[NUM_STACKS];
      
      gameTimer = new Timer();
      gameTimer.start();
      
      //
      initializeGame(numPacks, numJokersPerPack, 
         numUnusedCardsPerPack, unusedCardsPerPack, 
         numPlayers, numCardsPerHand);
   }
   
   /**
    * Private member function. Merged from CardGameFramework & used to assist
    * in starting a new game.
    * @param numPacks int representing packs of cards used
    * @param numJokersPerPack int representing Jokers in each pack
    * @param numUnusedCardsPerPack int representing number of unused cards
    * @param unusedCardsPerPack int[] holding unused cards if they exist
    * @param numPlayers int representing number of players in the  game
    * @param numCardsPerHand int representing number cards in each players hand
    */
   private void initializeGame(int numPacks, int numJokersPerPack,
      int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
      int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
      
      loadCardIcons();

      // prepare deck and shuffle
      newGame();
   }
   
   /**
    * Used to initialize a game by dealing cards to the player and computer,
    * then sorting their hands.
    */
   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );

      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );

      // shuffle the cards
      deck.shuffle();
   
      // deal cards to each player and sort their hands
      deal();
      sortHands();
   }
   
   /**
    * Deals a card to the top of each stack if enough cards are left in deck.
    * @return TRUE if cards are dealt and FALSE if they cannot be dealt
    */
   public boolean deckDealsToStacks()
   {
      //return false if less than 3 cards are left in the deck
      if(getNumCardsRemainingInDeck() <= NUM_STACKS)
      {
         return false;
      }
      
      //For each stack, take a card from the deck and add it to the top
      addStackOneCard(getCardFromDeck());
      addStackTwoCard(getCardFromDeck());
      addStackThreeCard(getCardFromDeck());
      
      return true;
   }
   
   /**
    * Plays a card from the player's hand to the top of one of the stacks. The
    * boolean represents whether the player's hand can be refilled. False means
    * that the deck is empty and the game should be ended. This method does not
    * end the game.
    * @param cardIndex int representing index of player card in array
    * @param stackIndex int representing index of stack in hand array
    * @return TRUE if can deal card to player, FALSE if not
    */
   public boolean playerPlaysToStack(int cardIndex, int stackIndex)
   {
      //create a new hand object at stackIndex if null
      if(playAreaStacks[stackIndex] == null)
         playAreaStacks[stackIndex] = new Hand();
      
      //Stack at stackIndex takes a card from the player's hand at cardIndex
      playAreaStacks[stackIndex].takeCard(
            getHand(HUMAN_PLAYER).playCard(cardIndex));
      
      //if deck has cards remaining, deal to the player and return true
      if(getNumCardsRemainingInDeck() > 0)
      {
         getHand(HUMAN_PLAYER).
            takeCard(getCardFromDeck());
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * Plays a card from the computer's hand to the top of one of the stacks. 
    * The boolean represents whether the computer's hand can be refilled. False 
    * means that the deck is empty and the game should be ended. This method 
    * does not end the game.
    * @param cardIndex int representing index of computer card in array
    * @param stackIndex int representing index of stack in hand array
    * @return TRUE if can deal card to computer, FALSE if not
    */
   public boolean computerPlaysToStack(int cardIndex, int stackIndex)
   {
      //create a new hand object at stackIndex if null
      if(playAreaStacks[stackIndex] == null)
         playAreaStacks[stackIndex] = new Hand();
      
      //Stack at stackIndex takes a card from the computer's hand at cardIndex
      playAreaStacks[stackIndex].takeCard
         (getHand(COMPUTER_PLAYER).playCard(cardIndex));
      
      //if deck has cards remaining, deal to the computer and return true
      if(getNumCardsRemainingInDeck() > 0)
      {
         getHand(COMPUTER_PLAYER).
            takeCard(getCardFromDeck());
         return true;
      }
      else
      {
         return false;
      }
   }   
   
   /**
    * Returns an array of Icon objects in the order of the cards in the player's
    * hand.
    * @return icon[] representing the player's hand
    */
   public Icon[] getPlayerIcons()
   {
      int numCardsInHand = getHand(HUMAN_PLAYER).getNumCards();
      Icon[] tempIcon = new Icon[numCardsInHand];
      
      //get icon for each card in the player's hand and store in tempIcon[]
      for(int i = 0; i < numCardsInHand; i++)
      {
         tempIcon[i] = 
            getIcon(getHand(HUMAN_PLAYER).inspectCard(i));
      }
      
      return tempIcon;
   }
   
   /**
    * Returns an array of Icon objects representing the top card of each stack
    * @return Icon[] representing the top card of each stack
    */
   public Icon[] getStackIcons()
   {
      Icon tempIcon[] = new Icon[NUM_STACKS];
    
      //store either an icon or null value in tempIcon[i] depending on state
      for(int i = 0; i < NUM_STACKS; i++)
      {
         if (getPlayAreaCard(i) != null)
            tempIcon[i] = getIcon(getPlayAreaCard(i));
         else
            tempIcon[i] = null;
      }
      
      return tempIcon;
   }
   
   /**
    * Return a copy of the top card of the stack at stackNumber. 
    * @param stackNumber int representing index of stack in array
    * @return Card object of top card of stack
    */
   public Card getPlayAreaCard(int stackNumber)
   {
      if(stackNumber == 0)
         return getStackOneCard();
      else if (stackNumber == 1)
         return getStackTwoCard();
      else
         return getStackThreeCard();
   }
   
   /**
    * Adds a card to the top of the designated stack.
    * @param stackNumber int representing index of stack in array
    * @param card Card object to be added to the top of the stack
    * @return
    */
   public boolean addPlayAreaCard(int stackNumber, Card card)
   {
      if(stackNumber == 0)
         return addStackOneCard(card);
      else if (stackNumber == 1)
         return addStackTwoCard(card);
      else
         return addStackThreeCard(card);
   }
   
   /**
    * Return copy of card at the top of stack one.
    * @return Card object at top of stack one or null if stack is empty
    */
   private Card getStackOneCard()
   {
      //returns top card if stack is not empty
      if(playAreaStacks[0] != null) 
      {
         int topCard = playAreaStacks[0].getNumCards(); 
         return playAreaStacks[0].inspectCard(topCard - 1);
      }
      else
         return null;
   }

   /**
    * Adds card to the top of stack one if parameter is not null.
    * @param card Card object to be added to the top of stack one.
    * @return TRUE if card is added to the top, FALSE if not
    */
   private boolean addStackOneCard(Card card)
   {
      if(card == null)
         return false;
      
      playAreaStacks[0].takeCard(card);
      return true;
   }
   
   /**
    * Return copy of card at the top of stack two.
    * @return Card object at top of stack two or null if stack is empty
    */
   private Card getStackTwoCard()
   {
      if(playAreaStacks[1] != null) 
      {
         int topCard = playAreaStacks[1].getNumCards();
         return playAreaStacks[1].inspectCard(topCard - 1);
      }
      else
         return null;
   }
   
   /**
    * Adds card to the top of stack two if parameter is not null.
    * @param card Card object to be added to the top of stack two.
    * @return TRUE if card is added to the top, FALSE if not
    */
   private boolean addStackTwoCard(Card card)
   {
      if(card == null)
         return false;
      
      playAreaStacks[1].takeCard(card);
      return true;
   }
   
   /**
    * Return copy of card at the top of stack three.
    * @return Card object at top of stack three or null if stack is empty
    */
   private Card getStackThreeCard()
   {
      if(playAreaStacks[2] != null) 
      {
         int topCard = playAreaStacks[2].getNumCards();
         return playAreaStacks[2].inspectCard(topCard - 1);
      }
      else
         return null;
   }
   
   /**
    * Adds card to the top of stack three if parameter is not null.
    * @param card Card object to be added to the top of stack three.
    * @return TRUE if card is added to the top, FALSE if not
    */
   private boolean addStackThreeCard(Card card)
   {
      if(card == null)
         return false;
      
      playAreaStacks[2].takeCard(card);
      return true;
   }
   
   /**
    * Returns copy of card at index of computer's hand.
    * @param index int representing location of card on computer's hand
    * @return Card object copy of desired card
    */
   public Card inspectComputerHand(int index)
   {
      return new Card(getHand(COMPUTER_PLAYER).inspectCard(index));
   }
   
   /**
    * Returns copy of card at index of player's hand.
    * @param index int representing location of card on player's hand
    * @return Card object copy of desired card
    */
   public Card inspectHumanHand(int index)
   {
      return new Card(getHand(HUMAN_PLAYER).inspectCard(index));
   }
   
   /**
    * Returns number of cards in an individual hand. Both hands are same size
    * (game ends if not) so computer hand is an arbitrary reference.
    * @return int representing number of cards in an individual's hand
    */
   public int getNumCardsPerHand()
   {
      return getHand(COMPUTER_PLAYER).getNumCards();
   }
   
   
   /**
    * Return number of stacks in the play area.
    * @return int representing number of stacks
    */
   public int getNumStacks()
   {
      return NUM_STACKS;
   }
   
   /**
    * Returns number of times player has skipped their turn.
    * @return int representing number of skips made by the player.
    */
   public int getPlayerSkips()
   {
      return playerSkipCounter;
   }
   
   /**
    * Increases the playerSkipCounter by one.
    * @return TRUE if maximum skip count has not been reached.
    */
   public boolean increasePlayerSkips()
   {
      //Return false if counter has incremented beyond a reasonable number
      if (playerSkipCounter > 1000)
         return false;
         
      playerSkipCounter++;
      return true;
   }
   
   /**
    * Returns number of times computer has skipped their turn.
    * @return int representing number of skips made by the computer.
    */
   public int getComputerSkips()
   {
      return computerSkipCounter;
   }
   
   /**
    * Increases the computerSkipCounter by one.
    * @return TRUE if maximum skip count has not been reached.
    */
   public boolean increaseComputerSkips()
   {
      //Return false if counter has incremented beyond a reasonable number
      if (computerSkipCounter > 1000)
         return false;
      
      computerSkipCounter++;
      return true;
   }
   
   /**
    * Returns second count from the gameTimer object.
    * @return int representing number of seconds counted so far.
    */
   public int getTimerCounter()
   {
      return gameTimer.getSeconds();
   }
   
   /**
    * Sets the value of the timerCounter private instance variable.
    * @param seconds int representing new value of timerCounter
    * @return TRUE if value has been set and FALSE if not
    */
   public boolean setTimerCounter(int seconds)
   {
      if(seconds < 0)
      {
         System.out.println("Error: Invalid Timer value.");
         return false;
      }
      
      timerCounter = seconds;
      return true;
   }
   
   /**
    * Stops the Timer object if it is on and starts it if it is off. When 
    * stopping a Timer object.
    */
   public void resetTimer()
   {
      //if timer is active, store its second count in timerCounter and stop it
      if(gameTimer.getTimerOn())
      {
         setTimerCounter(gameTimer.getSeconds());
         gameTimer.setTimerOn(false);
         gameTimer.stop();
      }
      else //if timer is not active, start it and initialize it to saved time
      {
         gameTimer = new Timer(getTimerCounter());
         gameTimer.start(); 
      }
   }
   
   /**
    * Returns hand object at selected index.
    * @param k int representing index of hand to be returned
    * @return Hand object at index location in array
    */
   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   /**
    * Returns card dealt from top of Deck private instance variable.
    * @return
    */
   public Card getCardFromDeck() { return deck.dealCard(); }

   /**
    * Returns number of Card objects left in Deck array
    * @return int representing number of cards left in the deck
    */
   public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }
   
   /**
    * Deals cards to all player hands until full or until no cards remain.
    * @return TRUE if cards are dealt until hands are full & FALSE if deck 
    * empties
    */
   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards ; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard( deck.dealCard() );
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   /**
    * Sorts hands in ascending order of value from bottom to top of array.
    */
   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   /**
    * Hand at playerIndex plays (and removes) Card at cardIndex on its array.
    * @param playerIndex int representing index on array of hands
    * @param cardIndex int representing index on array of cards in Hand object
    * @return Card object representing card played from hand (or invalid card)
    */
   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
          cardIndex < 0 || cardIndex > numCardsPerHand - 1)
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);      
      }
   
      // return the card played
      return hand[playerIndex].playCard(cardIndex);
   
   }

   /**
    * Hand object at playerIndex in Hand array takes a card from the deck. The
    * deck removes its top card.
    * @param playerIndex
    * @return
    */
   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;
     
       // Are there enough Cards?
       if (deck.getNumCards() <= 0)
          return false;

       return hand[playerIndex].takeCard(deck.dealCard());
   }

   static void loadCardIcons()
   {
      //exit method if icons are already loaded
      if (iconsLoaded == true)
         return;
      
      //load iconBack image file
      iconBack = new ImageIcon("images/BK.gif");
      String fileName = "";     
      
      //load all card images into iconCards array
      for (int suit = 0; suit < 4; suit++)
         for(int card = 0; card < 14; card++)
         {
            fileName = "images/" + turnIntIntoCardValue(card)
               + turnIntIntoCardSuit(suit) + ".gif";
            iconCards[card][suit] = new ImageIcon(fileName);
         }
      
      iconsLoaded = true;
   }
   
   /**
    * Compares value and suit of parameter Card against iconCards array and
    * returns associated Icon.
    * @param card Card to be compared to icon array
    * @return Icon icon associated with Card parameter
    */
   static public Icon getIcon(Card card)
   {
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }
   
   /**
    * Returns Icon of the card back image.
    * @return Icon representing the back of the card.
    */
   static public Icon getBackCardIcon()
   {
      return iconBack;
   }
   
   /**
    * Converts char value of a Card to int and returns it
    * @param card card holding value to be converted to int
    * @return int integer representing char value of card
    */
   static int valueAsInt(Card card)
   {  
      //Compares card value to valuRanks array and returns resident index
      for (int i = 0; i < Card.valuRanks.length; i++)
      {
         if(card.getValue() == Card.valuRanks[i])
            return i;
      }
      //Returns -1 if value is not found in valuRanks array
      return -1;
      
   }
   
   /**
    * Returns suit of card as int (based on position of suit in Icon array)
    * @param card Card whos suit should be translated to int
    * @return int representing value of suit
    */
   static int suitAsInt(Card card)
   {
      switch(card.getSuit())
      {
      case clubs: return 0;
      case diamonds: return 1;
      case hearts: return 2;
      default: return 3;
      }
   }
   
   /**
    * Return character (as string) at position k in the valuRanks array. From 
    * 0-13.
    * @param k position of value in valuRanks array
    * @return String of character in k position of valuRanks array
    */
   static String turnIntIntoCardValue(int k)
   {
    return Character.toString(Card.valuRanks[k]);
   }
   
   /**
    * Return string of capitalized first letter of suit
    * @param j int representing suit of card (based on Icon array)
    * @return String of first letter of capitalized suit name
    */
   static String turnIntIntoCardSuit(int j)
   {
      switch(j)
      {
      case 0: return "C";
      case 1: return "D";
      case 2: return "H";
      default: return "S";
      }
   }
}

/**
 * Timer iterates a counter every second to track the amount of time that the
 * thread has been active. BuildGameController informs BuildGameModel to get
 * and store the second count from Timer. BuildGameController then accesses
 * the stored timer data to inform BuildGameView to update its timer button.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class Timer extends Thread
{
   private int seconds;
   private boolean timerOn;
   private String timerLabel;

   /**
    * Default Constructor. Initializes all instance variables.
    */
   Timer()
   {
      super();
      seconds = 0;
      timerOn = true;
      timerLabel = "Seconds: " + seconds;
   }

   /**
    * Constructor that initializes instance variables to passed parameters
    * @param seconds int representing second count that timer should start at
    */
   Timer(int seconds)
   {
      super();
      this.seconds = seconds;
      timerOn = true;
      timerLabel = "Seconds: " + seconds;
   }

   /**
    * Starts the timer if it is off and if it is on, stops the timer by setting
    * a boolean value to false, breaking a loop in the thread.
    * @return int value representing total number of iterations
    */
   public int resetTimer()
   {
      if (!timerOn)
      {
         timerOn = true;
         start();
      }
      else if(timerOn)
      {
         timerOn = false;
      }

      return seconds;
   }

   /**
    * Generates label for timer button.
    * @return String object representing active time for Timer
    */
   public String getTimerLabel()
   {
      timerLabel = "Seconds: " + seconds;
      return timerLabel;
   }

   /**
    * Returns value of timerOn private instance variable.
    * @return boolean value of timerOn
    */
   public boolean getTimerOn()
   {
      return timerOn;
   }

   /**
    * Returns value of seconds private instance variable
    * @return int value of seconds
    */
   public int getSeconds()
   {
      return seconds;
   }

   /**
    * Sets timerOn to passed boolean value.
    * @param state boolean value that timerOn should be set to
    */
   public void setTimerOn(boolean state)
   {
      timerOn = state;
   }

   /**
    * Thread that iterates the seconds variable each second as long as timerOn 
    * is true.
    */
   public void run()
   {
      while(getTimerOn())
      {
         doNothing();
         seconds++;
      }
   }

   /**
    * Pauses the thread for a 1000 milliseconds.
    */
   public void doNothing()
   {
      try
      {
         Thread.sleep(1000);
      }
      catch(InterruptedException e)
      {

      }
   }
}

/**
 * Holds static array (masterPack) that represents all card values and suits. 
 * Each deck object holds an individual array of cards, representing 1 or more
 * packs of cards.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class Deck
{
   public final static int MAX_CARDS = 6*56;
   private static Card[] masterPack = new Card[52];
   private Card[] cards;
   private int topCard;
   /**
    * Constructor. Builds masterpack and initializes card[] to 1 pack
    */
   public Deck()
   {
      allocateMasterPack();
      cards = new Card[56];
      topCard = 0;

      for (int i = 0; i < masterPack.length; i++)
      {
         cards[topCard] = 
               new Card(masterPack[i].getValue(),masterPack[i].getSuit());
         topCard++;
      }
   }
   /**
    * Constructor. Initializes card array to number of packs in parameter
    * @param numPacks desired size of card array (numPacks * masterPack.size)
    */
   public Deck(int numPacks)
   {
      allocateMasterPack();
      topCard = 0;
      cards = new Card[56*numPacks];

      for(int i = 0; i < numPacks; i++)
      {
         for(int j = 0; j < masterPack.length; j++)
         {
            cards[topCard] = 
                  new Card(masterPack[j].getValue(),masterPack[j].getSuit());
            topCard++;
         }
      }
   }
   /**
    * Private helper function that builds reference array of cards
    */
   private static void allocateMasterPack()
   {      
      if (masterPack[0] != null)
         return;

      char[] cardValues = new char[] {'A','2','3','4',
            '5','6','7','8','9','T','J','Q','K'};
      Card.Suit cardSuits[] = new Card.Suit[] {Card.Suit.clubs, 
            Card.Suit.diamonds, Card.Suit.spades, Card.Suit.hearts};
      int currentCard = 0;

      for (int suit = 0; suit < cardSuits.length; suit++)
      {
         for(int value = 0; value < cardValues.length; value++)
         {
            masterPack[currentCard] = 
                  new Card(cardValues[value],cardSuits[suit]);
            currentCard++;
         }
      }    
   }
   /**
    * Rebuilds and fills deck to match desired pack size
    * @param numPacks int representing desired pack size of deck
    */
   public void init(int numPacks)
   {
      topCard = 0;

      if (cards.length != (numPacks * 56))
         cards = new Card[56*numPacks];

      for(int i = 0; i < numPacks; i++)
      {
         for(int j = 0; j < masterPack.length; j++)
         {
            cards[topCard] = 
                  new Card(masterPack[j].getValue(),masterPack[j].getSuit());
            topCard++;
         }
      }
   }
   /**
    * Randomizes card order in deck
    */
   public void shuffle()
   {
      Random rand = new Random();
      Card tempCard = new Card();
      int randomIndex;

      //shuffles deck 17 times (based on team suggestion)
      for(int j = 0; j < 17; j++)
      {
         for(int i = 0; i < topCard; i++)
         {
            randomIndex = rand.nextInt(topCard - i);
            tempCard = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = tempCard;
         }
      }
   }
   /**
    * Returns the card at the top of the deck and removes it from the array
    * @return Card deep copy of top card in deck
    */
   public Card dealCard()
   {
      //Prevents dealing from an empty deck
      if (cards[topCard-1] == null)
      {
         System.out.println("Fatal Error: Cannot deal from an empty deck");
         System.exit(0);
      }
      //removes top card from deck and returns deep copy of top card 
      char value = cards[topCard-1].getValue();
      Card.Suit suit = cards[topCard-1].getSuit();
      Card cardDeepCopy = new Card(value, suit);
      cards[topCard-1] = null;
      topCard--;

      return cardDeepCopy;
   }
   /**
    * Returns index entry of current top card
    * @return int value representing index entry of top card in deck
    */
   public int getTopCard()
   {
      return topCard;
   }
   /**
    * Returns deep copy of card at element k in cards array without removing it
    * @param k int value representing index in cards array
    * @return Card copy of object at array[k]
    */
   public Card inspectCard(int k)
   {
      //Returns an invalid card if index is not valid
      if(k < 0 || k >= topCard)
      {
         return new Card('1', Card.Suit.spades);
      }

      return new Card(cards[k].getValue(), cards[k].getSuit());
   }
   /**
    * Adds copy of passed Card to the top of the deck
    * @param card object to be added to the deck
    * @return boolean TRUE if successful, FALSE if not
    */
   public boolean addCard(Card card)
   {
      //returns false if deck is full
      if (topCard == cards.length)
         return false;
      //adds copy of parameter to top of deck and increases card count
      cards[topCard] = new Card(card.getValue(),card.getSuit());
      topCard++;
      return true;
   }
   /**
    * Checks to see if passed Card has an entry in the deck. Removes and
    * replaces with current top card if it exists. Returns false if it does
    * not.
    * @param card Object whose equal should be replaced in the deck
    * @return boolean TRUE if replaced, FALSE if not
    */
   public boolean removeCard(Card card)
   {
      //Compares parameter against all objects in deck and replaces if found
      //Exhaustive in case duplicates exist
      for (int i = 0; i < getTopCard(); i++)
      {
         if (cards[i].equals(card))
         {
            cards[i] = cards[topCard - 1];
            cards[topCard - 1] = null;
            topCard--;
            return true;
         }
      }
      return false;
   }
   /**
    * Calls Card class arraySort method to bubbleSort private member cards array
    */
   public void sort()
   {
      Card.arraySort(cards, topCard);
   }
   /**
    * Returns int value representing number of cards in the deck. Value is
    * equivalent to top card (top card is end of array and 0 is start)
    * @return int value of topCards private member
    */
   public int getNumCards()
   {
      return getTopCard();
   }

}

/**
 * Class representing individual cards that compose the Hand and Deck classes
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class Card
{
   private char value;
   enum Suit { clubs, diamonds, hearts, spades };
   private Suit suit;
   private boolean errorFlag;
   public static char[] valuRanks = {'A','2','3','4', '5','6','7','8','9','T',
         'J','Q','K','X'};

   /**
    * Default constructor
    */
   Card()
   {
      set('A',Suit.spades);
   }

   /**
    * Copy constructor
    * @param copyCard card whos values should be duplicated
    */
   Card(Card copyCard)
   {
      set(copyCard.getValue(), copyCard.getSuit());
   }

   /**
    * Constructor that tales a value and suit
    * @param val char presenting value of card
    * @param st enum representing suit of card
    */
   Card(char val, Suit st)
   {
      set(val,st);
   }

   /**
    * Sets value and suit. If successful, sets errorFlag to false and returns 
    * true, if not successful sets errorFlag true and returns false.
    * @param val char presenting value of card
    * @param st enum representing suit of card
    * @return boolean TRUE if passed parameters can be set and FALSE if not
    */
   public boolean set(char val, Suit st)
   {
      if (isValid(val,st))
      {
         value = val;
         suit = st;
         errorFlag = false;
         return true;
      }
      else
      {
         errorFlag = true;
         return false;
      }
   }

   /**
    * returns true if a valid value and suit are given, false otherwise
    * @param val char presenting value of card
    * @param st enum representing suit of card
    * @return boolean true if a valid value and suit are given, false otherwise
    */
   private boolean isValid(char val, Suit st)
   {
      if (st == Suit.clubs || st == Suit.diamonds || st == Suit.hearts 
            || st == Suit.spades)
      {
         if ((val >= '2' && val <= '9') || val == 'T' || val == 'J' 
               || val == 'Q' || val == 'K' || val == 'A' || val == 'X')
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return false;
      }
   }

   /**
    * Returns a string containing the value and suit of the card
    * if errorFlag is false, returns "[INVALID]" otherwise
    * @return Returns a string containing the value and suit of the card
    */
   public String toString()
   {
      String result;
      if (errorFlag)
      {
         result = "[INVALID]";
      }
      else if (value >= '2' && value <='9')
      {
         result = (value + " of " + suit.toString());
      }
      else
      {
         switch (value)
         {
         case 'T': result = ("10 of " + suit.toString());
         break;
         case 'J': result = ("Jack of " + suit.toString());
         break;
         case 'Q': result = ("Queen of " + suit.toString());
         break;
         case 'K': result = ("King of " + suit.toString());
         break;
         case 'A': result = ("Ace of " + suit.toString());
         break;
         case 'X': result = ("Joker of " + suit.toString());
         break;
         default: result = ("Ace of spades");
         }
      }
      return result;
   }

   /**
    * Accessor for value private member
    * @return char representing value of card
    */
   public char getValue()
   {
      return value;
   }

   /**
    * Accessor for suit private member
    * @return Suit enum representing suit of card
    */
   public Suit getSuit()
   {
      return suit;
   }

   /**
    * Accessor for errorFlag private member
    * @return boolean representing error status of the card
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   /**
    * returns true if all variables in this card match those in a given card
    * @param card  target object to be compared to calling object
    * @return boolean true equivalent, false otherwise
    */
   public boolean equals(Card card)
   {
      return (value == card.value && suit == card.suit 
            && errorFlag == card.errorFlag);
   }

   /**
    * Bubble sort method applied to incoming array of cards
    * @param cards array to be sorted
    * @param arraySize number of cards in cards parameter
    */
   public static void arraySort(Card[] cards, int arraySize)
   {
      Card temp;
      int firstRank, secondRank;

      //"bubbles" lowest values to the bottom of the array
      //non-optimized, loops for worst case scenario on each call
      for (int i = 0; i < arraySize; i++)
      {
         for(int j = 0; j < arraySize - i - 1; j++)
         {
            firstRank = findRank(cards[j].getValue());
            secondRank = findRank(cards[j+1].getValue());
            if (firstRank > secondRank)
            {
               temp = cards[j];
               cards[j] = cards [j+1];
               cards[j+1] = temp;
            }
         }
      }
   }

   /**
    * Returns integer representing relative value of card (low to high),
    * compared against position of valuRanks array
    * @param element char to be compared against valuRanks array
    * @return int 1 if element exists and -1 if it does not
    */
   private static int findRank(char element)
   {
      for(int i = 0; i < valuRanks.length; i++)
      {
         if(element == valuRanks[i])
            return i;
      }

      return -1;
   }
}

/**
 * Holds an array of Card objects representing a player's hand
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class Hand
{
   //MAX_CARDS is set to value based on spec.
   //Note: Max single hand size can be 6*52 in a 1 player game with 6 decks.
   public static final int MAX_CARDS = 50;
   private Card[] myCards;
   private int numCards;

   /**
    * Default constructor. Initializes array to size MAX_CARDS
    */
   Hand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   /**
    * Dumps cards by initializing new array and and setting numCards to 0
    */
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /**
    * Creates an object copy of card argument and assigns it to the top position
    * in the array 
    * @param card Object to be added to the hand
    * @return boolean FALSE if hand is full and TRUE if card is added
    */
   public boolean takeCard(Card card)
   {
      //Will return false if hand has reached MAX_CARDS
      if (numCards == MAX_CARDS)
      {
         System.out.printf("%nHand Full%n");
         return false;
      }
      else
      {
         //Creates new card based on argument and assigns to top position
         myCards[numCards] = new Card(card.getValue(), card.getSuit());
         numCards++;
         return true;
      }
   }

   /**
    * Returns object copy of top card and removes it from the hand
    * @return Card object deep copy of top element in array
    */
   public Card playCard()
   {
      //Will display an error message if playing card from an empty array
      if (numCards == 0)
      {
         System.out.println("Fatal Error:" + 
               " Cannot play card from an empty hand.");
         System.exit(0);
      }

      //Creates object copy of top most element. Note: Array starts at 0.
      Card.Suit suitValue = myCards[numCards-1].getSuit();
      char charValue = myCards[numCards-1].getValue();
      Card cardDeepCopy = new Card(charValue, suitValue);

      //Card is "removed" by setting reference to null and decrementing numCards
      myCards[numCards-1] = null;
      numCards--;

      return cardDeepCopy;
   }

   /**
    * Calls Card.tostring() on each element in the myCards array
    */
   public String toString()
   {
      String result = "Hand = (";

      if (numCards == 0)
         return result + " )";
      else
      {  
         //Will not add ", " if calling toString() on last element in the array
         for (int i = 0; i < numCards; i ++)
         {
            if (i == (numCards - 1))
               result = result + myCards[i].toString();
            else
               result = result + myCards[i].toString() + ", ";
         }
         return result + ")";
      }
   }

   /**
    * Accessor for numCards private member
    * @return int value of numCards
    */
   public int getNumCards()
   {
      return numCards;
   }

   /**
    * Will return object copy of Card present in element k of the myCards array
    * returns card with errorFlag = true if k is bad
    * @param k int representing index of card to be inspected
    * @return card object copy of array[k]
    */
   public Card inspectCard(int k)
   {
      //Returns card with errorFlag = true if k is out of bounds
      if(k < 0 || k >= numCards)
      {
         return new Card('1', Card.Suit.spades);
      }

      //Object copy will always set errorFlag = true if values are invalid
      return new Card(myCards[k].getValue(), myCards[k].getSuit());
   }

   /**
    * Removes and returns card at array[cardIndex]
    * @param cardIndex int representing index of card to be played
    * @return card reference copy of object to be played
    */
   public Card playCard(int cardIndex)
   {
      if ( numCards == 0 ) //error
      {
         //Creates a card that does not work
         return new Card('M', Card.Suit.spades);
      }
      //Decreases numCards.
      Card card = myCards[cardIndex];

      numCards--;
      for(int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i+1];
      }

      myCards[numCards] = null;

      return card;
   }

   /**
    * Calls Card class arraySort method to bubble sort card objects in hand
    */
   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }
}
