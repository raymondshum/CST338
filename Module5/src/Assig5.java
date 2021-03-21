/******************************************************************************* 
Names: Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovichm
Group: Group 2 - InnovaTree
Assignment Name: (MM5) Write a Java program (GUI "Low Card" Game) - Phase 2
Due Date: Dec 2, 2020

Description:
Phase 2 builds on Phase 1 by creating and displaying the client GUI of the Low
Card Game. In this phase, the Computer's hand is represented by card back icons
at the top of the screen, played cards in the middle of the screen and icons
representing the cards present in the player's hand at the bottom of the screen.
The icons are non-interactive labels in this phase. This phase also builds on
revised Deck, Hand and Card classes.
*******************************************************************************/

import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * Represents Client GUI for Card Game. Builds table with CardTable class &
 * displays cards with CardGUI class.
 * @author Larry Chiem, Ian Rowe, Raymond Shum, Nicholas Stankovich
 *
 */
public class Assig5
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 
   
   public static void main(String[] args)
   {
      Icon tempIcon;
      
      // establish main frame in which program will run
      CardTable myCardTable 
         = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);

      // CREATE LABELS ----------------------------------------------------
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         tempIcon = GUICard.getBackCardIcon();
         computerLabels[i] = new JLabel(tempIcon);
      }
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
      {
         tempIcon = GUICard.getIcon(randomCardGenerator());
         humanLabels[i] = new JLabel(tempIcon);
      }
      
      for (int i = 0; i < NUM_PLAYERS; i++)
      {
         tempIcon = GUICard.getIcon(randomCardGenerator());
         playedCardLabels[i] = new JLabel(tempIcon, JLabel.CENTER);
      }
  
      // ADD LABELS TO PANELS -----------------------------------------
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
         myCardTable.pnlComputerHand.add(computerLabels[i]);
      
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
         myCardTable.pnlHumanHand.add(humanLabels[i]);            
         
      // and two random cards in the play region (simulating a computer/hum ply)
      for (int i = 0; i < NUM_PLAYERS; i++)
         myCardTable.pnlPlayArea.add(playedCardLabels[i]);
      
      myCardTable.pnlPlayArea.add(new JLabel("Computer", JLabel.CENTER));
      myCardTable.pnlPlayArea.add(new JLabel("Player", JLabel.CENTER));
      
   }

   /**
    * Returns randomly generated card.
    * @return Card new card object initialized with random number generator
    */
   static Card randomCardGenerator()
   {
      Random randomInt = new Random();
      int cardValue = randomInt.nextInt(14);
      int cardSuit = randomInt.nextInt(4);
      
      //returns random card based on random suit (switch case) and value
      switch(cardSuit)
      {
      case 0: return new Card(GUICard.turnIntIntoCardValue(cardValue).charAt(0), 
         Card.Suit.clubs);
      case 1: return new Card(GUICard.turnIntIntoCardValue(cardValue).charAt(0), 
            Card.Suit.diamonds);
      case 2: return new Card(GUICard.turnIntIntoCardValue(cardValue).charAt(0), 
            Card.Suit.hearts);
      default: return new Card(GUICard.turnIntIntoCardValue(cardValue).
            charAt(0), Card.Suit.spades);
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
   private static Card[] masterPack = new Card[56];
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
      
      //initializes card array with topCard being most recently added card
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
      
      //builds card array using deep copies of masterPack card objects
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
         '5','6','7','8','9','T','J','Q','K', 'X'};
      Card.Suit cardSuits[] = new Card.Suit[] {Card.Suit.clubs, 
         Card.Suit.diamonds, Card.Suit.spades, Card.Suit.hearts};
      int currentCard = 0;
      
      //builds array for all possible values and suits
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
      
      //rebuilds card array if size doesn't match passed parameter
      if (cards.length != (numPacks * 56))
         cards = new Card[56*numPacks];
      
      //fills card array with deep copies of masterPack cards until full
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
