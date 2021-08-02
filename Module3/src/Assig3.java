/* 
Names: Ian Rowe, Larry Chiem, Nick Stankovich, Raymond Shum
Group: Group 2 - InnovaTree
Assignment Name: (M3) Write a Java program: Decks of Cards (7 hrs)
Due Date: November 17, 2020
*/

import java.util.*;

/* Description for Main Class:
This main class handles output three and output four for the assignment. 
Output three is supposed to show the user two decks unshuffled and shuffled. 
Output four is expected to show the players the cards dealt with in their hands, 
proporational to their party size ranging from one to ten.
*/

public class Assig3 {
   public static void main(String[] args) {

      // [PHASE 3 OUTPUT]
      Deck firstDeck = new Deck(2);
      Deck secondDeck = new Deck();

      // Deals unshuffled deck of two packs
      System.out.println("Dealing unshuffled deck (2 packs): ");
      System.out.printf("( ");
      while (firstDeck.getTopCard() != 0)
         System.out.printf("%s, ", firstDeck.dealCard());
      System.out.printf(" )%n%n");

      firstDeck.init(2);
      firstDeck.shuffle();

      // Deals shuffled deck of two packs
      System.out.println("Dealing initialized & shuffled deck (2 packs): ");
      System.out.printf("( ");
      while (firstDeck.getTopCard() != 0)
         System.out.printf("%s, ", firstDeck.dealCard());
      System.out.printf(" )%n%n");
      System.out.println("Dealing unshuffled deck (1 packs): ");
      System.out.printf("( ");
      while (secondDeck.getTopCard() != 0)
         System.out.printf("%s, ", secondDeck.dealCard());
      System.out.printf(" )%n%n");

      secondDeck.init(1);
      secondDeck.shuffle();

      System.out.println("Dealing initialized & shuffled deck (1 packs): ");
      System.out.printf("( ");
      while (secondDeck.getTopCard() != 0)
         System.out.printf("%s, ", secondDeck.dealCard());
      System.out.printf(" )%n%n");

      // [PHASE 4 & PHASE 4 OUTPUT]

      System.out.printf("How many hands? (1 - 10, please): ");
      Scanner playerInput = new Scanner(System.in);
      int numPlayers = playerInput.nextInt();

      while (numPlayers < 1 || numPlayers > 10) {
         System.out.printf("Sorry, That is not a valid answer.\n\nHow many hands? (1 - 10, please): ");
         numPlayers = playerInput.nextInt();
      }
      playerInput.close();

      Deck mainDeck = new Deck();
      Hand[] playerHands = new Hand[numPlayers];

      for (int i = 0; i < playerHands.length; i++) {
         playerHands[i] = new Hand();
      }

      // Dealing the deck to players unshuffled
      while (mainDeck.getTopCard() != 0) {
         for (int i = 0; i < playerHands.length; i++) {
            if (mainDeck.getTopCard() != 0) {
               playerHands[i].takeCard(mainDeck.dealCard());
            }

         }
      }

      // Printing cards to hands, Unshuffled
      System.out.println("\nHere are our hands, from unshuffled deck: ");
      for (int i = 0; i < playerHands.length; i++) {
         System.out.println(playerHands[i].toString() + "\n");
      }

      // Resetting Deck of Cards to be handed out to players, shuffled
      for (int i = 0; i < playerHands.length; i++) {
         playerHands[i].resetHand();
      }

      mainDeck.init(1);
      mainDeck.shuffle();

      // Dealing the Sceond Deck to players shuffled
      while (mainDeck.getTopCard() != 0) {
         for (int i = 0; i < playerHands.length; i++) {
            if (mainDeck.getTopCard() != 0) {
               playerHands[i].takeCard(mainDeck.dealCard());
            }

         }
      }
      // Printing cards to hands, shuffled
      System.out.println("\nHere are our hands, from SHUFFLED deck: ");
      for (int i = 0; i < playerHands.length; i++) {
         System.out.println(playerHands[i].toString() + "\n");
      }
   }

}

/*
 * Description for Card Class: The card class for this assignment is expexted to
 * create cards for the corresponding classes, and to create a card, with a suit
 * and value.
 */

// [PART ONE]
class Card {
   private char value;

   enum Suit {
      clubs, diamonds, hearts, spades
   };

   private Suit suit;
   private boolean errorFlag;

   // Default constructor
   Card() {
      set('A', Suit.spades);
   }

   // Constructor that tales a value and suit
   Card(char val, Suit st) {
      set(val, st);
   }

   /*
    * Sets value and suit. If successful, sets errorFlag to false and returns true,
    * if not successful sets errorFlag true and returns false.
    */
   public boolean set(char val, Suit st) {
      if (isValid(val, st)) {
         value = val;
         suit = st;
         errorFlag = false;
         return true;
      } else {
         errorFlag = true;
         return false;
      }
   }

   // returns true if a valid value and suit are given, false otherwise
   private boolean isValid(char val, Suit st) {
      if (st == Suit.clubs || st == Suit.diamonds || st == Suit.hearts || st == Suit.spades) {
         if ((val >= '2' && val <= '9') || val == 'T' || val == 'J' || val == 'Q' || val == 'K' || val == 'A') {
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   /*
    * Returns a string containing the value and suit of the card if errorFlag is
    * false, returns "[INVALID]" otherwise
    */
   public String toString() {
      String result;
      if (errorFlag) {
         result = "[INVALID]";
      } else if (value >= '2' && value <= '9') {
         result = (value + " of " + suit.toString());
      } else {
         switch (value) {
            case 'T':
               result = ("10 of " + suit.toString());
               break;
            case 'J':
               result = ("Jack of " + suit.toString());
               break;
            case 'Q':
               result = ("Queen of " + suit.toString());
               break;
            case 'K':
               result = ("King of " + suit.toString());
               break;
            case 'A':
               result = ("Ace of " + suit.toString());
               break;
            default:
               result = ("Ace of spades");
         }
      }
      return result;
   }

   // Accessors for variables
   public char getValue() {
      return value;
   }

   public Suit getSuit() {
      return suit;
   }

   public boolean getErrorFlag() {
      return errorFlag;
   }

   // returns true if all variables in this card match those in a given card
   public boolean equals(Card card) {
      return (value == card.value && suit == card.suit && errorFlag == card.errorFlag);
   }
}

/*
 * Description for Hand Class: The hand class is expected to create hands, based
 * on the number of players given on the main class.
 * 
 */

// [PART TWO]
class Hand {
   // MAX_CARDS is set to value based on spec.
   // Note: Max single hand size can be 6*52 in a 1 player game with 6 decks.
   public static final int MAX_CARDS = 50;
   private Card[] myCards;
   private int numCards;

   // Default constructor. Initializes array to size MAX_CARDS
   Hand() {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   // Dumps cards by initializing new array and and setting numCards to 0
   public void resetHand() {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /*
    * Creates an object copy of card argument and assigns it to the top position in
    * the array
    */
   public boolean takeCard(Card card) {
      // Will return false if hand has reached MAX_CARDS
      if (numCards == MAX_CARDS) {
         System.out.printf("%nHand Full%n");
         return false;
      } else {
         // Creates new card based on argument and assigns to top position
         myCards[numCards] = new Card(card.getValue(), card.getSuit());
         numCards++;
         return true;
      }
   }

   // Returns object copy of top card & removes it from the hand
   public Card playCard() {
      // Will display an error message if playing card from an empty array
      if (numCards == 0) {
         System.out.println("Fatal Error:" + " Cannot play card from an empty hand.");
         System.exit(0);
      }
      // Creates object copy of top most element. Note: Array starts at 0.
      Card.Suit suitValue = myCards[numCards - 1].getSuit();
      char charValue = myCards[numCards - 1].getValue();
      Card cardDeepCopy = new Card(charValue, suitValue);

      // Card is "removed" by setting reference to null and decrementing numCards
      myCards[numCards - 1] = null;
      numCards--;

      return cardDeepCopy;
   }

   // Calls Card.tostring() on each element in the myCards array
   public String toString() {
      String result = "Hand = (";
      if (numCards == 0)
         return result + " )";
      else {
         // Will not add ", " if calling toString() on last element in the array
         for (int i = 0; i < numCards; i++) {
            if (i == (numCards - 1))
               result = result + myCards[i].toString();
            else
               result = result + myCards[i].toString() + ", ";
         }
         return result + ")";
      }
   }

   // Accessor for numCards private member
   public int getNumCards() {
      return numCards;
   }

   /*
    * Will return object copy of Card present in element k of the myCards array
    * returns card with errorFlag = true if k is bad
    */
   public Card inspectCard(int k) {
      // Returns card with errorFlag = true if k is out of bounds
      if (k < 0 || k >= numCards) {
         return new Card('1', Card.Suit.spades);
      }
      // Object copy will always set errorFlag = true if values are invalid
      return new Card(myCards[k].getValue(), myCards[k].getSuit());
   }
}

/*
 * Description for Deck Class: The deck class is handling all 52 cards in a
 * deck, and to hold all cards, with passing the cards to the players when
 * necessary.
 */

// [PART THREE]
class Deck {
   // Initial declarations
   public final static int MAX_CARDS = 6 * 52;

   private static Card[] masterPack = new Card[52];
   private Card[] cards;
   private int topCard;

   // Declaration with no overloaders defaults to one pack
   public Deck() {
      // Perpare variables
      allocateMasterPack();
      cards = new Card[52];
      topCard = 0;
      // Loop to create the deck.
      for (int i = 0; i < masterPack.length; i++) {
         cards[topCard] = new Card(masterPack[i].getValue(), masterPack[i].getSuit());
         topCard++;
      }
   }

   // Declaration with Int value
   public Deck(int numPacks) {
      // Prepare variables
      allocateMasterPack();
      topCard = 0;
      cards = new Card[52 * numPacks];
      // Repeat for each pack
      for (int i = 0; i < numPacks; i++) {
         for (int j = 0; j < masterPack.length; j++) {
            cards[topCard] = new Card(masterPack[j].getValue(), masterPack[j].getSuit());
            topCard++;
         }
      }
   }

   // Creates the masterpack.
   private static void allocateMasterPack() {
      // If the masterpack already exists, end method.
      if (masterPack[0] != null)
         return;
      // Declare arrays to make initalization easier.
      char[] cardValues = new char[] { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' };
      Card.Suit cardSuits[] = new Card.Suit[] { Card.Suit.clubs, Card.Suit.diamonds, Card.Suit.spades,
            Card.Suit.hearts };
      // Set card counter
      int currentCard = 0;

      // Iterate through the suits.
      for (int suit = 0; suit < cardSuits.length; suit++) {
         // Iterate through each value in a suit.
         for (int value = 0; value < cardValues.length; value++) {
            masterPack[currentCard] = new Card(cardValues[value], cardSuits[suit]);
            currentCard++;
         }
      }
   }

   // Resets the array and reintializes it.
   public void init(int numPacks) {
      topCard = 0;
      // Check if need to adjust array size.
      if (cards.length != (numPacks * 52))
         cards = new Card[52 * numPacks];

      for (int i = 0; i < numPacks; i++) {
         for (int j = 0; j < masterPack.length; j++) {
            cards[topCard] = new Card(masterPack[j].getValue(), masterPack[j].getSuit());
            topCard++;
         }
      }
   }

   //
   public void shuffle() {
      Random rand = new Random();
      Card tempCard = new Card();
      int randomIndex;
      // 17 is the amount of times it apparently takes to shuffle something.
      for (int j = 0; j < 17; j++) {
         for (int i = 0; i < topCard; i++) {
            // Swaps the place of the current card and a random card.
            randomIndex = rand.nextInt(topCard - i);
            tempCard = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = tempCard;
         }
      }
   }

   // Copies card value down, removes card, and returns the copied value.
   public Card dealCard() {
      // Exception handler.
      if (cards[topCard - 1] == null) {
         System.out.println("Fatal Error: Cannot deal from an empty deck");
         System.exit(0);
      }

      char value = cards[topCard - 1].getValue();
      Card.Suit suit = cards[topCard - 1].getSuit();
      Card cardDeepCopy = new Card(value, suit);
      cards[topCard - 1] = null;
      topCard--;

      return cardDeepCopy;
   }

   // Gets the topcard value.
   public int getTopCard() {
      return topCard;
   }

   // Returns the card at k
   public Card inspectCard(int k) {
      // Exception handler.
      if (k < 0 || k >= topCard) {
         return new Card('1', Card.Suit.spades);
      }

      return new Card(cards[k].getValue(), cards[k].getSuit());
   }
}

/*
 * [OUTPUT FOR PART THREE & PART FOUR]
 * 
 * [PART THREE] 
 * Dealing unshuffled deck (2 packs): ( King of hearts, Queen of
 * hearts, Jack of hearts, 10 of hearts, 9 of hearts, 8 of hearts, 7 of hearts,
 * 6 of hearts, 5 of hearts, 4 of hearts, 3 of hearts, 2 of hearts, Ace of
 * hearts, King of spades, Queen of spades, Jack of spades, 10 of spades, 9 of
 * spades, 8 of spades, 7 of spades, 6 of spades, 5 of spades, 4 of spades, 3 of
 * spades, 2 of spades, Ace of spades, King of diamonds, Queen of diamonds, Jack
 * of diamonds, 10 of diamonds, 9 of diamonds, 8 of diamonds, 7 of diamonds, 6
 * of diamonds, 5 of diamonds, 4 of diamonds, 3 of diamonds, 2 of diamonds, Ace
 * of diamonds, King of clubs, Queen of clubs, Jack of clubs, 10 of clubs, 9 of
 * clubs, 8 of clubs, 7 of clubs, 6 of clubs, 5 of clubs, 4 of clubs, 3 of
 * clubs, 2 of clubs, Ace of clubs, King of hearts, Queen of hearts, Jack of
 * hearts, 10 of hearts, 9 of hearts, 8 of hearts, 7 of hearts, 6 of hearts, 5
 * of hearts, 4 of hearts, 3 of hearts, 2 of hearts, Ace of hearts, King of
 * spades, Queen of spades, Jack of spades, 10 of spades, 9 of spades, 8 of
 * spades, 7 of spades, 6 of spades, 5 of spades, 4 of spades, 3 of spades, 2 of
 * spades, Ace of spades, King of diamonds, Queen of diamonds, Jack of diamonds,
 * 10 of diamonds, 9 of diamonds, 8 of diamonds, 7 of diamonds, 6 of diamonds, 5
 * of diamonds, 4 of diamonds, 3 of diamonds, 2 of diamonds, Ace of diamonds,
 * King of clubs, Queen of clubs, Jack of clubs, 10 of clubs, 9 of clubs, 8 of
 * clubs, 7 of clubs, 6 of clubs, 5 of clubs, 4 of clubs, 3 of clubs, 2 of
 * clubs, Ace of clubs, )
 * 
 * Dealing initialized & shuffled deck (2 packs): ( 7 of hearts, 5 of hearts, 9
 * of hearts, 6 of spades, 8 of diamonds, 4 of diamonds, 2 of diamonds, 10 of
 * hearts, Ace of hearts, Queen of diamonds, 9 of clubs, 6 of hearts, Queen of
 * hearts, Jack of clubs, 4 of hearts, 5 of hearts, Queen of spades, 9 of
 * spades, 10 of diamonds, 10 of spades, 4 of diamonds, Jack of spades, 2 of
 * spades, 7 of spades, Ace of spades, 3 of diamonds, 6 of clubs, 9 of diamonds,
 * 3 of clubs, Ace of clubs, 8 of hearts, 4 of clubs, 10 of spades, 7 of hearts,
 * King of diamonds, 3 of diamonds, 3 of spades, 3 of hearts, King of diamonds,
 * 10 of clubs, 2 of hearts, 6 of hearts, 7 of spades, 4 of spades, King of
 * hearts, Jack of hearts, Queen of hearts, 2 of clubs, 8 of diamonds, 8 of
 * clubs, 3 of hearts, Ace of spades, Jack of clubs, Jack of hearts, 10 of
 * clubs, Jack of spades, 8 of clubs, 6 of spades, 10 of hearts, 9 of hearts,
 * Jack of diamonds, 7 of diamonds, 2 of spades, 9 of diamonds, 9 of clubs, 2 of
 * diamonds, Ace of clubs, 2 of hearts, 5 of diamonds, Ace of diamonds, Queen of
 * diamonds, 8 of spades, King of clubs, King of hearts, Queen of clubs, Ace of
 * diamonds, 9 of spades, 6 of diamonds, King of clubs, 10 of diamonds, 6 of
 * diamonds, Jack of diamonds, 5 of clubs, Queen of clubs, 5 of diamonds, 7 of
 * diamonds, King of spades, 5 of clubs, 5 of spades, 8 of hearts, 7 of clubs, 8
 * of spades, Queen of spades, 3 of spades, 7 of clubs, Ace of hearts, 5 of
 * spades, King of spades, 6 of clubs, 3 of clubs, 4 of spades, 2 of clubs, 4 of
 * hearts, 4 of clubs, )
 * 
 * Dealing unshuffled deck (1 packs): ( King of hearts, Queen of hearts, Jack of
 * hearts, 10 of hearts, 9 of hearts, 8 of hearts, 7 of hearts, 6 of hearts, 5
 * of hearts, 4 of hearts, 3 of hearts, 2 of hearts, Ace of hearts, King of
 * spades, Queen of spades, Jack of spades, 10 of spades, 9 of spades, 8 of
 * spades, 7 of spades, 6 of spades, 5 of spades, 4 of spades, 3 of spades, 2 of
 * spades, Ace of spades, King of diamonds, Queen of diamonds, Jack of diamonds,
 * 10 of diamonds, 9 of diamonds, 8 of diamonds, 7 of diamonds, 6 of diamonds, 5
 * of diamonds, 4 of diamonds, 3 of diamonds, 2 of diamonds, Ace of diamonds,
 * King of clubs, Queen of clubs, Jack of clubs, 10 of clubs, 9 of clubs, 8 of
 * clubs, 7 of clubs, 6 of clubs, 5 of clubs, 4 of clubs, 3 of clubs, 2 of
 * clubs, Ace of clubs, )
 * 
 * Dealing initialized & shuffled deck (1 packs): ( Ace of hearts, 5 of hearts,
 * 8 of clubs, King of hearts, 2 of hearts, 4 of hearts, King of clubs, 8 of
 * hearts, 4 of spades, Queen of hearts, 10 of spades, 3 of hearts, 10 of
 * diamonds, 8 of spades, Queen of spades, 8 of diamonds, 2 of diamonds, 9 of
 * clubs, 10 of hearts, Jack of hearts, 7 of diamonds, 7 of hearts, 7 of spades,
 * 5 of diamonds, 9 of hearts, 2 of clubs, 3 of spades, Queen of clubs, 5 of
 * spades, 5 of clubs, 6 of clubs, 6 of hearts, 9 of diamonds, 10 of clubs, Ace
 * of diamonds, Queen of diamonds, 3 of diamonds, Ace of spades, 7 of clubs, 4
 * of clubs, Jack of spades, 4 of diamonds, Ace of clubs, Jack of diamonds, 2 of
 * spades, King of diamonds, King of spades, 3 of clubs, 9 of spades, Jack of
 * clubs, 6 of spades, 6 of diamonds, )
 * 
 * [PART FOUR] 
 * How many hands? (1 - 10, please): 4
 * 
 * Here are our hands, from unshuffled deck: Hand = (King of hearts, 9 of
 * hearts, 5 of hearts, Ace of hearts, 10 of spades, 6 of spades, 2 of spades,
 * Jack of diamonds, 7 of diamonds, 3 of diamonds, Queen of clubs, 8 of clubs, 4
 * of clubs)
 * 
 * Hand = (Queen of hearts, 8 of hearts, 4 of hearts, King of spades, 9 of
 * spades, 5 of spades, Ace of spades, 10 of diamonds, 6 of diamonds, 2 of
 * diamonds, Jack of clubs, 7 of clubs, 3 of clubs)
 * 
 * Hand = (Jack of hearts, 7 of hearts, 3 of hearts, Queen of spades, 8 of
 * spades, 4 of spades, King of diamonds, 9 of diamonds, 5 of diamonds, Ace of
 * diamonds, 10 of clubs, 6 of clubs, 2 of clubs)
 * 
 * Hand = (10 of hearts, 6 of hearts, 2 of hearts, Jack of spades, 7 of spades,
 * 3 of spades, Queen of diamonds, 8 of diamonds, 4 of diamonds, King of clubs,
 * 9 of clubs, 5 of clubs, Ace of clubs)
 * 
 * 
 * Here are our hands, from SHUFFLED deck: Hand = (Ace of spades, King of clubs,
 * 7 of diamonds, 6 of clubs, 8 of hearts, 5 of diamonds, Queen of clubs, King
 * of hearts, 10 of spades, 7 of hearts, 9 of clubs, 10 of hearts, 5 of spades)
 * 
 * Hand = (2 of clubs, Jack of clubs, 9 of spades, 10 of diamonds, 2 of
 * diamonds, 3 of clubs, Ace of clubs, Ace of hearts, 4 of spades, Jack of
 * diamonds, Jack of spades, 3 of hearts, 5 of clubs)
 * 
 * Hand = (Queen of spades, 7 of clubs, King of diamonds, Jack of hearts, 2 of
 * spades, 3 of diamonds, 3 of spades, Queen of diamonds, 5 of hearts, King of
 * spades, 7 of spades, 4 of diamonds, 8 of clubs)
 * 
 * Hand = (6 of hearts, 6 of diamonds, 2 of hearts, Queen of hearts, 8 of
 * diamonds, 10 of clubs, 8 of spades, Ace of diamonds, 9 of diamonds, 9 of
 * hearts, 4 of clubs, 6 of spades, 4 of hearts)
 */