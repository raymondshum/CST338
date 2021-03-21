import java.util.Random;

public class Assig3
{

   public static void main(String[] args)
   {
      /*
      Deck firstDeck = new Deck(2);
      Deck secondDeck = new Deck();
      
      System.out.println("Dealing unshuffled deck (2 packs): ");
      System.out.printf("( ");
      while (firstDeck.getTopCard() != 0)
         System.out.printf("%s, ", firstDeck.dealCard());
      System.out.printf(" )%n%n");
      
      firstDeck.init(2);
      firstDeck.shuffle();
      
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
      
      Hand firstHand = new Hand();
      Card firstCard = new Card('A', Card.Suit.spades);
      Card secondCard = new Card('9', Card.Suit.diamonds);
      Card thirdCard = new Card('T', Card.Suit.hearts);
      Card invalidCard = new Card('1', Card.Suit.diamonds);

      //Taking cards until hand is full. Loop will attempt to take 51 cards.
      do
      {
         firstHand.takeCard(firstCard);
         firstHand.takeCard(secondCard);
         firstHand.takeCard(thirdCard);
      } while (firstHand.getNumCards() < Hand.MAX_CARDS);

      System.out.printf("After deal:%n%s%n" 
         + "Cards in hand:%d%n", firstHand.toString(), firstHand.getNumCards());
      System.out.printf("%nPlaying cards until hand is empty:%n");

      //Plays card from top position of array until hand is empty. Returns
      //string value from played card.
      do
      {
         System.out.println((firstHand.playCard()).toString());
      } while(firstHand.getNumCards() > 0);

      System.out.printf("%nHand after dealing: %n");
      System.out.println(firstHand.toString() + " \n" 
         + "Cards remaning after dealing: " + firstHand.getNumCards());

      //Populate array with invalid card as every third entry
      do
      {
         firstHand.takeCard(firstCard);
         firstHand.takeCard(secondCard);
         firstHand.takeCard(invalidCard);
      } while (firstHand.getNumCards() < Hand.MAX_CARDS);

      System.out.printf("After deal:%n%s%n"
         + "Cards in hand:%d%n", firstHand.toString(), firstHand.getNumCards());
      System.out.printf("%nInspecting cards from bottom up:%n");
      
      //Inspects each element of hand array and displays values.
      for(int i = 0; i < firstHand.getNumCards(); i++)
         System.out.println((firstHand.inspectCard(i)).toString());

      System.out.println(firstHand.toString() + " \n" 
            + "Cards remaning after inspection: " + firstHand.getNumCards());
      */
      Deck thirdDeck = new Deck();
      /*
      Card testCard = new Card('2', Card.Suit.clubs);
      int k = 0;
      thirdDeck.init(1);
      int topCard = thirdDeck.getTopCard();
      System.out.println("Top card is: " + thirdDeck.getTopCard());
      for (int i = 0; i < topCard; i++)
      {
         System.out.println(thirdDeck.inspectCard(i));
         k++;
      }
      System.out.println("Number of cards = " + k);
      thirdDeck.removeCard(testCard);
      k = 0;
      for (int i = 0; i < thirdDeck.getTopCard(); i++)
      {
         System.out.println(thirdDeck.inspectCard(i));
         k++;
      }
      System.out.println("Number of cards = " + k);
      */
      /*
      thirdDeck.shuffle();
      thirdDeck.sort();
      for (int i = 0; i < thirdDeck.getTopCard(); i++)
      {
         System.out.println(thirdDeck.inspectCard(i));
      }
      */
      
      Hand firstHand = new Hand();
      Card firstCard = new Card('A', Card.Suit.spades);
      Card secondCard = new Card('9', Card.Suit.diamonds);
      Card thirdCard = new Card('T', Card.Suit.hearts);
      Card invalidCard = new Card('1', Card.Suit.diamonds);

      //Taking cards until hand is full. Loop will attempt to take 51 cards.
      do
      {
         firstHand.takeCard(firstCard);
         firstHand.takeCard(secondCard);
         firstHand.takeCard(thirdCard);
      } while (firstHand.getNumCards() < Hand.MAX_CARDS);
      
      firstHand.sort();
      for(int i = 0; i < firstHand.getNumCards(); i++)
         System.out.println((firstHand.inspectCard(i)).toString());
      
   }
   

}

class Deck
{
   public final static int MAX_CARDS = 6*56;
   private static Card[] masterPack = new Card[56];
   private Card[] cards;
   private int topCard;
   
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
   
   private static void allocateMasterPack()
   {      
      if (masterPack[0] != null)
         return;
      
      char[] cardValues = new char[] {'A','2','3','4',
         '5','6','7','8','9','T','J','Q','K', 'X'};
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
   
   public void shuffle()
   {
      Random rand = new Random();
      Card tempCard = new Card();
      int randomIndex;
      
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
   
   public Card dealCard()
   {
      if (cards[topCard-1] == null)
      {
         System.out.println("Fatal Error: Cannot deal from an empty deck");
         System.exit(0);
      }
      
      char value = cards[topCard-1].getValue();
      Card.Suit suit = cards[topCard-1].getSuit();
      Card cardDeepCopy = new Card(value, suit);
      cards[topCard-1] = null;
      topCard--;
      
      return cardDeepCopy;
   }
   
   public int getTopCard()
   {
      return topCard;
   }
   
   public Card inspectCard(int k)
   {
      if(k < 0 || k >= topCard)
      {
         return new Card('1', Card.Suit.spades);
      }
      
      return new Card(cards[k].getValue(), cards[k].getSuit());
   }

   public boolean addCard(Card card)
   {
      if (topCard == cards.length)
         return false;
      
      cards[topCard] = new Card(card.getValue(),card.getSuit());
      topCard++;
      return true;
   }
   
   public boolean removeCard(Card card)
   {
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
   
   public void sort()
   {
      Card.arraySort(cards, topCard);
   }
   
   public int getNumCards()
   {
      return getTopCard();
   }
   
}

class Card
{
   private char value;
   enum Suit { clubs, diamonds, hearts, spades };
   private Suit suit;
   private boolean errorFlag;
   public static char[] valuRanks = {'A','2','3','4', '5','6','7','8','9','T',
      'J','Q','K','X'};
   
   //Default constructor
   Card()
   {
      set('A',Suit.spades);
   }
   //Constructor that tales a value and suit
   Card(char val, Suit st)
   {
      set(val,st);
   }
   /*Sets value and suit. If successful, sets errorFlag to false and returns 
     true, if not successful sets errorFlag true and returns false.*/
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
   //returns true if a valid value and suit are given, false otherwise
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
   /*Returns a string containing the value and suit of the card
     if errorFlag is false, returns "[INVALID]" otherwise*/
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
   //Accessors for variables
   public char getValue()
   {
      return value;
   }
   public Suit getSuit()
   {
      return suit;
   }
   public boolean getErrorFlag()
   {
      return errorFlag;
   }
   //returns true if all variables in this card match those in a given card
   public boolean equals(Card card)
   {
      return (value == card.value && suit == card.suit 
            && errorFlag == card.errorFlag);
   }
   
   public static void arraySort(Card[] cards, int arraySize)
   {
      Card temp;
      int firstRank, secondRank;
      
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

class Hand
{
   //MAX_CARDS is set to value based on spec.
   //Note: Max single hand size can be 6*52 in a 1 player game with 6 decks.
   public static final int MAX_CARDS = 50;
   private Card[] myCards;
   private int numCards;

   //Default constructor. Initializes array to size MAX_CARDS
   Hand()
   {
      numCards = 0;
      myCards = new Card[MAX_CARDS];
   }

   //Dumps cards by initializing new array and and setting numCards to 0
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /*Creates an object copy of card argument and assigns it to the top position
   in the array */
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

   //Returns object copy of top card & removes it from the hand
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

   //Calls Card.tostring() on each element in the myCards array
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

   //Accessor for numCards private member
   public int getNumCards()
   {
      return numCards;
   }

   /*Will return object copy of Card present in element k of the myCards array
   returns card with errorFlag = true if k is bad*/
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
   
   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }
}
