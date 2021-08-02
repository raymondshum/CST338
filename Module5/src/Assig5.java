/******************************************************************************* 
Names: Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovichm
Group: Group 2 - InnovaTree
Assignment Name: (MM5) Write a Java program (GUI "Low Card" Game) - Phase 3
Due Date: Dec 2, 2020

Description:
Phase 3 further builds upon Phase 2, using the cardTable and GUICard, along with 
a CardGameFramework class to build an interactive game in which the goal is to 
play a card smaller in value than your opponent's. The game is played until 
hands of 7 can no longer be dealt, then a final tally is made to determine the 
overall winner.
*******************************************************************************/

import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Represents Client GUI for Low Card Game. Builds table with CardTable class
 * and displays it with GUICard class. Finally runs the game logic and
 * interactivity using swing.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
public class Assig5
{
   //All of the cards, buttons, labels, etc. used by the program
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   
   static boolean computerWentFirst = false;
   static JLabel cardSpot1 = new JLabel("",JLabel.CENTER);
   static JLabel cardSpot2 = new JLabel("",JLabel.CENTER);
   
   static final int numPacksPerDeck = 1;
   static final int numJokersPerPack = 2;
   static final int numUnusedCardsPerPack = 0;
   static Card[] unusedCardsPerPack = null;
   
   static JButton handButtons[] = new JButton[NUM_CARDS_PER_HAND];
   static JLabel computerCards[] = new JLabel[NUM_CARDS_PER_HAND];
   //This is where the players hands
   static Card[] playerWinnings = new Card[56*numPacksPerDeck];
   static Card[] computerWinnings = new Card[56*numPacksPerDeck];
   static int playerWinCount=0;
   static int computerWinCount=0;
   
   static Card playerCard;
   static Card computerCard;
   //This is the table where the game will be played
   static CardTable myCardTable 
   = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
   //this framework will handle the cards in the players hands and the deck
   static CardGameFramework LowCardGame = new CardGameFramework( 
         numPacksPerDeck, numJokersPerPack,  
         numUnusedCardsPerPack, unusedCardsPerPack, 
         NUM_PLAYERS, NUM_CARDS_PER_HAND);
   
   public static void main(String[] args)
   {
      /**
       * Listener for reading and interpreting button inputs and performing
       * necessary game logic in response
       * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
       *
       */
      class CardListener implements ActionListener
      {
         /**
          * Performs all of the game logic
          * @param e event fired by button press when user selects card
          */
         public void actionPerformed(ActionEvent e)
         {
            /*converts action command from button into integer for use in
              calling cards and other items*/
            int card = Integer.parseInt(e.getActionCommand());
            //takes card from hand and places it in object for use by card table
            playerCard = LowCardGame.getHand(0).playCard(card);
            //Place card in play area
            cardSpot1.setIcon(GUICard.getIcon(playerCard));
            myCardTable.pnlHumanHand.repaint();
            //Shift the cards down the button array to fill in the gap
            for(int i = card; i < LowCardGame.getHand(0).getNumCards(); i++)
            {
               handButtons[i].setIcon(handButtons[i+1].getIcon());
            }
            //Removes the last card
            myCardTable.pnlHumanHand.remove(
                  handButtons[LowCardGame.getHand(0).getNumCards()]);
            //If the computer didn't go first, it plays a card
            if (!computerWentFirst)
            {
               int i;
               //If it can't win or tie, uses up largest card
               if (GUICard.valueAsInt(LowCardGame.getHand(1).inspectCard(0))
                     >GUICard.valueAsInt(playerCard))
               {
                  computerCard = 
                        LowCardGame.getHand(1).playCard(
                              LowCardGame.getHand(1).getNumCards()-1);
               }
               //If it can only tie, goes for the tie
               else if (GUICard.valueAsInt(LowCardGame.getHand(1).
                     inspectCard(0)) == GUICard.valueAsInt(playerCard))
               {
                  computerCard = LowCardGame.getHand(1).playCard(0);
               }
               //If it can win, uses the largest card that will still win
               else
               {
                  for (i=0; i<LowCardGame.getHand(1).getNumCards()
                        && GUICard.valueAsInt(LowCardGame.getHand(1).
                              inspectCard(i)) < GUICard.valueAsInt(playerCard);
                        i++)
                  {}
                  computerCard = LowCardGame.getHand(1).playCard(i-1);
                  
               }
            }
            //Places computer card on the playing field
            cardSpot2.setIcon(GUICard.getIcon(computerCard));
            myCardTable.pnlComputerHand.remove(0);
            myCardTable.pnlComputerHand.repaint();
            //Determines winner. If computer wins, draws card for next round.
            if (GUICard.valueAsInt(playerCard)<GUICard.valueAsInt(computerCard))
            {
               JOptionPane.showMessageDialog(null, "You Win!");
               playerWinnings[playerWinCount]=playerCard;
               playerWinnings[playerWinCount+1]=computerCard;
               playerWinCount+=2;
               computerWentFirst=false;
               cardSpot1.setIcon(null);
               cardSpot2.setIcon(null);
            }
            else if (GUICard.valueAsInt(playerCard)>GUICard.
                  valueAsInt(computerCard))
            {
               JOptionPane.showMessageDialog(null, "You Lose!");
               computerWinnings[computerWinCount]=playerCard;
               computerWinnings[computerWinCount+1]=computerCard;
               computerWinCount+=2;
               if (LowCardGame.getHand(1).getNumCards()!=0)
               {
                  computerCard = 
                        LowCardGame.getHand(1).playCard(
                              LowCardGame.getHand(1).getNumCards()-1);
                  computerWentFirst=true;
               }
               cardSpot1.setIcon(null);
               cardSpot2.setIcon(GUICard.getIcon(computerCard));
            }
            else
            {
               JOptionPane.showMessageDialog(null, "Tie! No Winner!");
               playerWinnings[playerWinCount]=playerCard;
               computerWinnings[computerWinCount+1]=computerCard;
               playerWinCount++;
               computerWinCount++;
               
               if (computerWentFirst&&LowCardGame.getHand(0).getNumCards()!=0)
               {
                  computerCard = 
                        LowCardGame.getHand(1).playCard(
                              LowCardGame.getHand(1).getNumCards()-1);
                  cardSpot1.setIcon(null);
                  cardSpot2.setIcon(GUICard.getIcon(computerCard));
               }
               else if (!computerWentFirst)
               {
                  cardSpot1.setIcon(null);
                  cardSpot2.setIcon(null);
               }
               else
               {
                  computerWentFirst=false;
               }
            }
            //If hands are empty, deals new hands if possible and sets up table
            if (LowCardGame.getHand(0).getNumCards()==0)
            {
               //makes sure the deal was successful first
               if (LowCardGame.deal())
               {
                  cardSpot1.setIcon(null);
                  cardSpot2.setIcon(null);
                  setupCards();
                  myCardTable.pnlHumanHand.repaint();
                  myCardTable.pnlComputerHand.repaint();
               }
               /*If not enough cards left, determines game winner and displays
                *scores.
                */
               else
               {
                  if (playerWinCount>computerWinCount)
                  {
                     cardSpot1.setIcon(null);
                     cardSpot2.setIcon(null);
                     JOptionPane.showMessageDialog(null, "You won the game!\n"
                           + "The score was " + playerWinCount + " to " 
                           + computerWinCount + ".\nThanks for playing!");
                  }
                  else if (playerWinCount<computerWinCount)
                  {
                     cardSpot1.setIcon(null);
                     cardSpot2.setIcon(null);
                     JOptionPane.showMessageDialog(null, "Sorry, you lost the "
                           + "game.\nThe score was " + playerWinCount + " to " 
                           + computerWinCount + ".\nThanks for playing!");
                  }
                  else
                  {
                     cardSpot1.setIcon(null);
                     cardSpot2.setIcon(null);
                     JOptionPane.showMessageDialog(null, "The game was a Tie!\n"
                           + "The score was " + playerWinCount + " to " 
                           + computerWinCount + ".\nThanks for playing!");
                  }
               }
            }
         }
      }
      CardListener play = new CardListener();
      //If for some reason the first deal fails, the program exits.
      if(!LowCardGame.deal())
      {
         System.exit(1);
      }
      LowCardGame.sortHands();
      
      //Initial setup for card table
      myCardTable.setSize(900, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      myCardTable.pnlPlayArea.add(new JLabel("Player",JLabel.CENTER));
      myCardTable.pnlPlayArea.add(new JLabel("Computer",JLabel.CENTER));
      myCardTable.pnlPlayArea.add(cardSpot1);
      myCardTable.pnlPlayArea.add(cardSpot2);
      //Prepares all the buttons and adds them to the board
      for (int i=0; i<NUM_CARDS_PER_HAND; i++)
      {
         handButtons[i] =
            new JButton(GUICard.getIcon(LowCardGame.getHand(0).inspectCard(i)));
         handButtons[i].setActionCommand(Integer.toString(i));
         handButtons[i].addActionListener(play);
         myCardTable.pnlHumanHand.add(handButtons[i]);
         computerCards[i] = new JLabel(GUICard.getBackCardIcon());
         myCardTable.pnlComputerHand.add(computerCards[i]);
      }
      //make everything visible
      myCardTable.setVisible(true);
   }
   /**
    * Private helper to set up buttons and cards and place them on the board
    * for the next hand. Specifically for after the buttons and cards have
    * already been initialized
    */
   private static void setupCards()
   {
      LowCardGame.sortHands();
      for (int i=0; i<NUM_CARDS_PER_HAND; i++)
      {
         handButtons[i].setIcon(GUICard.getIcon(LowCardGame.getHand(0).
               inspectCard(i)));
            handButtons[i].setActionCommand(Integer.toString(i));
         myCardTable.pnlHumanHand.add(handButtons[i]);
         myCardTable.pnlComputerHand.add(computerCards[i]);
         
      }
   }

   
}



/**
 * Builds card game GUI using three main panels: pnlComputerHand, pnlHumanHand 
 * and pnlPlayArea.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class CardTable extends JFrame
{
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;  // for now, we only allow 2 person games
   
   private int numCardsPerHand;
   private int numPlayers;

   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
   
   /**
    * Constructor. Creates card table GUI.
    * @param title name displayed on the GUI window
    * @param numCardsPerHand cards allowed per player
    * @param numPlayers players in the game
    */
   CardTable(String title, int numCardsPerHand, int numPlayers)
   {
      super(title);
      setLayout(new BorderLayout());
      
      //Initialize three primary panels
      pnlComputerHand = new JPanel();
      pnlHumanHand = new JPanel();
      pnlPlayArea = new JPanel();
      
      //Set layouts for three panels
      pnlComputerHand.setLayout(new FlowLayout());
      pnlHumanHand.setLayout(new FlowLayout());
      pnlPlayArea.setLayout(new GridLayout(2, 2));
      
      //Created titled borders for three panels
      pnlComputerHand.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Computer Hand"));
      pnlHumanHand.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Human Hand"));
      pnlPlayArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Play Area"));
      
      //add three panels in orientation described in spec sheet
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlHumanHand, BorderLayout.SOUTH);
      
      //initialize members and card icons
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;
      GUICard.loadCardIcons();
   }
   
   /**
    * Accessor. Return maximum number of cards allowed for each player.
    * @return int maximum number of cards in each player's hand
    */
   public int getNumCardsPerHand()
   {      
      return numCardsPerHand;
   }
   
   /**
    * Accessor. Return total number of players in the game.
    * @return int number of players in the game
    */
   public int getNumPlayers()
   {
      return numPlayers;
   }
}

/**
 * 
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
class GUICard extends JFrame
{
   // 14 = A thru K + joker
   private static Icon[][] iconCards = new ImageIcon[14][4]; 
   private static Icon iconBack;
   static boolean iconsLoaded = false;
   
   /**
    * Populates iconCards array with card .gif files and iconBack with card
    * back .gif file. Image (downloaded) folder should be extracted to workspace
    * directory of this assignment. Will only run once if successful.
    */
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
    * Returns object copy of top card & removes it from the hand
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
/**
 * A framework that handles cards for multiple players and a deck in a card game
 * @author unknown
 *
 */
class CardGameFramework
{
 private static final int MAX_PLAYERS = 50;

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
/**
 * Constructor for CardGameFramework
 * @param numPacks number of packs per deck
 * @param numJokersPerPack number of jokers is each pack
 * @param numUnusedCardsPerPack number of unused card in each pack (certain 
 * games use fewer than the standard 52 cards)
 * @param unusedCardsPerPack array of any unused cards
 * @param numPlayers number of players
 * @param numCardsPerHand number of cards in each hand
 */
 public CardGameFramework( int numPacks, int numJokersPerPack,
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

    // prepare deck and shuffle
    newGame();
 }

 /**
  * constructor overload/default for game like bridge
  */
 public CardGameFramework()
 {
    this(1, 0, 0, null, 4, 13);
 }

 public Hand getHand(int k)
 {
    // hands start from 0 like arrays

    // on error return automatic empty hand
    if (k < 0 || k >= numPlayers)
       return new Hand();

    return hand[k];
 }
/**
 * Deals a card from the deck
 * @return Card that was dealt
 */
 public Card getCardFromDeck() { return deck.dealCard(); }
/**
 * Accessor for how many cards are left in the deck
 * @return int number of cards
 */
 public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }
/**
 * Initial setup for the game. Prepares the deck and shuffles it.
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
 }
/**
 * Deals the apropriate number of cards into the players' hands
 * @return true if deal was successful
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
 *Arranges the cards in the hands in order of value.
 */
 void sortHands()
 {
    int k;

    for (k = 0; k < numPlayers; k++)
       hand[k].sort();
 }
/**
 * Plays a card from a players hand
 * @param playerIndex int index to indicate which player is playing the card
 * @param cardIndex int index to indicate which card was played
 * @return Card that was played
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
 * Takes a card from the deck and places it in the player's hand.
 * @param playerIndex int index to indicate which player is taking the card
 * @return true is successful
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

}