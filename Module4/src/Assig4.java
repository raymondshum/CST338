/* 
Names: Ian Rowe, Larry Chiem, Nick Stankovich, Raymond Shum
Group: Group 2 - InnovaTree
Assignment Name: (M4) Write a Java program (Optical Barcode Readers) (8 hrs)
Due Date: November 24, 2020

Description for Main Class:
This assignment uses two classes and two interfaces to scan, translate and
generate barcode images. The BarcodeImage class, which implements cloneable,
defines the dimensions of the array that holds the barcode image. It reads
and stores the barcode image. The DataMatrix class, which implements the
BarcodeIO interface, processes the image by pulling it to the lower left hand
corner of the BarcodeImage array and outputs the relevant portion to the
console. It can also generate a text string from a barcode and vice versa.
*/

public class Assig4
{

   public static void main(String[] args)
   {
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",  
         "                                               ",
         "                                               ",
         "                                               "

      };      
            
         
      
      String[] sImageIn_2 =
      {
            "                                          ",
            "                                          ",
            "* * * * * * * * * * * * * * * * * * *     ",
            "*                                    *    ",
            "**** *** **   ***** ****   *********      ",
            "* ************ ************ **********    ",
            "** *      *    *  * * *         * *       ",
            "***   *  *           * **    *      **    ",
            "* ** * *  *   * * * **  *   ***   ***     ",
            "* *           **    *****  *   **   **    ",
            "****  *  * *  * **  ** *   ** *  * *      ",
            "**************************************    ",
            "                                          ",
            "                                          ",
            "                                          ",
            "                                          "

      };
      
      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);
     
      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      
      System.out.println("\nDataMatrix image cleanup test.");
      BarcodeImage bTest = new BarcodeImage(sImageIn);
      System.out.println("Displaying raw barcode image: ");
      bTest.displayToConsole();
      DataMatrix dTest = new DataMatrix(bTest);
      System.out.println("\nDisplaying raw, shifted image: ");
      dTest.displayRawImage();
      System.out.println("\nDisplaying processed image: ");
      dTest.displayImageToConsole();
      
   }

}

/**
 * Classes that implement BarcodeIO are expected to store some version of an
 * image and some version of associated text.
 * @author Ian Rowe, Larry Chiem, Nick Stankovich, Raymond Shum
 *
 */
interface BarcodeIO
{
   /**
    * Accepts and stores an image. Image can be stored as clone or as a refined
    * and processed image.
    * @param bc BarcodeImage object to be stored
    * @return TRUE if successful and FALSE if not
    */
   public boolean scan(BarcodeImage bc);
   
   /**
    * Accepts text string for translation. Method does not perform translation.
    * @param text String object to be translated
    * @return TRUE if successful and FALSE if not
    */
   public boolean readText(String text);
   
   /**
    * Looks at internal text stored in implementing class and translates to an
    * object of class BarcodeImage. 
    * 
    * POSTCONDITION: Implementing object contains fully-defined image and text
    * that are in agreement of each other.
    * @return TRUE if successful and FALSE if not
    */
   public boolean generateImageFromText();
   
   /**
    * Looks at image stored in implementing class and produces a companion text
    * string, internally.
    * 
    * POSTCONDITION: Implementing object contains fully-defined image and text
    * that are in agreement of each other.
    * @return TRUE if successful and FALSE if not 
    */
   public boolean translateImageToText();
   
   /**
    * Prints text string to console.
    */
   public void displayTextToConsole();
   
   /**
    * Prints image to console in the form of dot-matrix composed of blanks and
    * asterisks.
    */
   public void displayImageToConsole();
}

/**
 * BarcodeImage is meant to retrieve and store 2D patterns. Does not perform
 * image shift or white space removal.
 * @author Ian Rowe, Larry Chiem, Nick Stankovich, Raymond Shum
 *
 */
class BarcodeImage
   implements Cloneable
{
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   
   //2D array meant to store image. FALSE for white elements, TRUE for black
   private boolean[][]imageData;
   
   /**
    * Default constructor initializes 2D array to MAX_HEIGHT and MAX_WIDTH
    */
   public BarcodeImage()
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      
      //imageData is initialized to false for all elements
      for(int height = 0; height < MAX_HEIGHT; height++)
      {
         for (int width = 0; width < MAX_WIDTH; width++)
            imageData[height][width] = false;
      }   
   }
   
   /**
    * Default constructor that converts 1D array of Strings, representing the
    * barcode, to the internal 2D array of boolean values.
    * imageData[0][0].
    * @param strData 1D array of strings to be stored
    */
   public BarcodeImage(String[] strData)
   {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      
      //imageData is initialized to false for all elements
      for(int height = 0; height < MAX_HEIGHT; height++)
      {
         for (int width = 0; width < MAX_WIDTH; width++)
            imageData[height][width] = false;
      } 
      
      //For each element in strData, traverses length of string and updates
      //imageData with associated boolean value at corresponding position
      for (int height = 0; height < strData.length; height++)
      {
         for (int width = 0; width < strData[height].length(); width++)
         {
            if(strData[height].charAt(width) == '*')
               imageData[height][width] = true;
         }
      }
   }
   
   /**
    * Returns true if imageData[row][col] is true ('*' present at corresponding
    * location in input string). Based on spec, will return false if element is
    * false or null.
    * @param row value of row of target element in imageData[][]
    * @param col value of column of target element in imageData[][]
    * @return TRUE if element is true. FALSE if element is anything else
    */
   public boolean getPixel(int row, int col)
   {
      //Will return false if element is false or null
      if (imageData[row][col] == true)
         return imageData[row][col];
      else
         return false;
   }
   
   /**
    * Sets boolean value of imageData[row][col]. 
    * @param row value of row of target element in imageData[][]
    * @param col value of column of target element in imageData[][]
    * @param value boolean value that element should be set to
    * @return TRUE if element successfully changed. FALSE if out of bounds.
    */
   public boolean setPixel(int row, int col, boolean value)
   {
      if ( (row >= MAX_HEIGHT) || (col >= MAX_WIDTH) )
         return false;
      else
         imageData[row][col] = value;
         return true;
   }
   
   /**
    * Display current contents of internal array to console.
    */
   public void displayToConsole()
   {
      //Draw upper border with offset
      for (int i = 0; i < MAX_WIDTH + 2; i++)
         System.out.printf("-");
      System.out.printf("%n");
      
      for(int height = 0; height < MAX_HEIGHT; height++)
      {
         //Draw left side border per row
         System.out.printf("|");
         for (int width = 0; width < MAX_WIDTH; width++)
         {
            //Draw '*' if element is true and ' ' if false
            if (imageData[height][width] == true)
               System.out.printf("*");
            else if(imageData[height][width] == false)
               System.out.printf(" ");
            
            //Also draw right side border if end of row reached
            if ((width + 1) == MAX_WIDTH)
               System.out.printf("|%n");
         }

      } 
      
      //Draw lower border with offset
      for (int i = 0; i < MAX_WIDTH + 2; i++)
         System.out.printf("-");
   }
   
   /**
    * Implementation of Cloneable interface. Creates and returns copy of calling
    * object. Returns BarcodeImage object if successful (not Object object).
    */
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      try {
         //BarcodeImage object is cloned
         BarcodeImage objectCopy = (BarcodeImage)super.clone();

         //clone() only creates shallow references of multidimensional arrays,
         //even if data is primitive type. Initialize, assign and populate new
         //array object.
         objectCopy.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
         for (int height = 0; height < MAX_HEIGHT; height++)
         {
            for (int width = 0; width < MAX_WIDTH; width++)
               objectCopy.imageData[height][width] = imageData[height][width];
         }
         return objectCopy;
      }
      catch(CloneNotSupportedException e)
      {
         return null;
      }

   }
   
}

/**
 * Scans and processes an array of boolean values representing a barcode with 
 * limit lines on all sides. Can translate this barcode to text or text into
 * a barcode.
 * @author Ian Rowe, Larry Chiem, Nick Stankovich, Raymond Shum
 *
 */
class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;
   
   /**
    * Default constructor for DataMatrix
    */
   public DataMatrix()
   {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }
   
   /**
    * Constructor. Clones BarcodeImage object and assigns to private image 
    * members. Performs image cleanup (moves barcode to lower left side of the
    * array. Does not translate image to text.
    * @param image BarcodeImage object to be cloned/assigned to private array
    */
   public DataMatrix(BarcodeImage image)
   {
      scan(image);
      text = "";
   }
   
   /**
    * Constructor. Assigns text parameter to private text member. Does not
    * generate or process BarcodeImage. Sets actualWidth and actualHeight to 0.
    * @param text String object to be assigned to private text member
    */
   public DataMatrix(String text)
   {
      image = new BarcodeImage();
      readText(text);
      actualWidth = 0;
      actualHeight = 0;
   }
   
   /**
    * Assigns value of String parameter to private text member if the argument 
    * is not null or empty.
    * @param text non-null and non-empty string variable
    * @return TRUE if successful and FALSE if not
    */
   public boolean readText(String text)
   {
      //assigns value of argument to text private member if !null and not empty
      if ((text != null) && (text != ""))
      {
         this.text = text;
         return true;
      }
      else
         return false;
   }
   
   /**
    * Clones BarcodeImage parameter and assigns its value to private 
    * image object. Performs barcode image processing and computes actual
    * width and height.
    * @param image non-null BarcodeImage object to be cloned
    * @return FALSE if image is null and TRUE otherwise
    */
   
   public boolean scan(BarcodeImage image)
   {
      try
      {
         this.image = image.clone();
         cleanImage();
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
         return true;
      }
      catch (CloneNotSupportedException e)
      {
         return false;
      }
   }
   
   /**
    * Accessor. Returns value of private member "actualWidth".
    * @return integer value of actualWidth
    */
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   /**
    * Accessor. Returns value of private member "actualHeight".
    * @return integer value of actualHeight
    */
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   /**
    * Clears values from image array and translates private text member to
    * barcode image (stored in private image array). The barcode is generated
    * in the "upper left" portion of the array first and then pulled to the
    * "bottom left".
    */
   public boolean generateImageFromText()
   {
      //returns false if image array is not initialized
      if (image==null)
         return false;
      
      //tracks the current rightmost edge of the barcode
      int columnPosition = 0;
      
      //image array is set to all false and left border is generated
      clearImage();
      writeLeftBorder();
      
      //writes a column for each translated character in private member "text"
      for (columnPosition = 0; columnPosition < text.length(); columnPosition++)
      {
         //columnPosition is +1 to skip left border
         //ASCII value of current character in text index is passed to method
         writeCharToCol(columnPosition+1, (int)text.charAt(columnPosition));   
      }
      
      //right border is drawn one position to the right of the current edge
      writeRightBorder(columnPosition+1);
      
      //barcode is pulled to lower left of the array and private members updated
      cleanImage();
      actualWidth = computeSignalWidth();
      actualHeight = computeSignalHeight();
      return true;
   }
   
   /**
    *Updates private member "text" with the translated text of the barcode
    *currently stored in image.
    */
   public boolean translateImageToText()
   {
      //returns false is image is not initialized
      if (image == null)
         return false;
      
      //sets text to empty in case previous values exist or text is null
      text = "";
      
      //appends translated chars to text, skipping left and right limit lines
      for(int i = 1; i < actualWidth - 1; i++)
      {
         //bottom and top limit lines skipped by readCharFromCol
         text += readCharFromCol(i);
      }
      
      return true;
   }
   
   /**
    * Translates binary values of the barcode at paramter col into a char value,
    * ignoring upper and lower limit lines. Should not be called on the left and
    * right limit lines. Also translates extended ASCII values.
    * @param col column on private member image array to be translated
    * @return char value of translated column
    */
   private char readCharFromCol(int col)
   {
      //sets initial position of method. Adjusts for edge of array and skips
      //bottom limit line
      int messageStart = BarcodeImage.MAX_HEIGHT - 2;
      
      int charValue = 0;
      int bitPosition = 0; //tracks position on binary octet
      int byteValue = 8; //sets the "height" of the message. is a binary octet
      
      //calculates and adds binary values on the message portion of the column
      for(int i = messageStart; i > messageStart - byteValue; i--)
      {
         //adds values based on the equation 2^(N-1) with N = bitPosition
         bitPosition++;
         if(image.getPixel(i, col))
            charValue += (int)Math.pow(2, bitPosition-1);
      }
    
      //returns character with associated calculated ASCII value
      return (char)charValue;
   }
   
   /**
    * Assigns boolean values to the parameter "col" of the array contained in
    * the private member "image". Also writes the top and bottom borders. Array
    * should be initialized (all elements set to false). Writes column to upper
    * left hand corner of the array.
    * @param col column of array that should be written
    * @param code ASCII value of character to be written
    * @return FALSE if column cannot be written, TRUE after writing
    */
   private boolean writeCharToCol(int col, int code)
   {
      //returns false if array or ASCII values out of bounds & array is null
      if (col >= BarcodeImage.MAX_WIDTH || col < 0 || code > 255 || code < 0
         || image == null)
         return false;
      
      //writes top and bottom borders for column
      writeColumnBorders(col);
      
      //converts character to binary values, not including leading zeros
      String binaryPosition = Integer.toBinaryString(code);
      
      int charBitValue = 8; //represents binary octet
      int leadingZeroOffset = charBitValue - binaryPosition.length();
      int borderOffset;
      
      //writes column from "top down"
      for (int row = 0; row < binaryPosition.length(); row++)
      {
         //borderOffset starts at top edge & updates to show current position
         borderOffset = row + 1;
         
         //if element in binary string is 1, update element in array
         if (binaryPosition.charAt(row) == '1')
         {
            //border and leading 0's are skipped
            image.setPixel(borderOffset + leadingZeroOffset, col, true);
         }
      }
      
      return true;
   }
   
   /**
    * Writes top and bottom borders for parameter column in image array.
    * @param col column who's borders should be written
    * @return FALSE if borders cannot be written, TRUE otherwise
    */
   private boolean writeColumnBorders(int col)
   {
      //check if index is out of bounds or array is not initialized
      if (col >= BarcodeImage.MAX_WIDTH || col < 0 || image == null)
         return false;
      
      //upper border is written for every other column in the array
      if (col%2 == 0)
      {
         image.setPixel(0, col, true);
      }
      
      //lower border is always drawn
      image.setPixel(9, col, true);
      return true;
   }
   
   /**
    * Draws left border of image array on upper left hand corner.
    * @return FALSE if border cannot be written, TRUE otherwise
    */
   private boolean writeLeftBorder()
   {
      if (image==null)
         return false;
               
      //draws black border for the height of the array. 10 represents 8 elements
      //for the message and 1 element each for the upper and lower border
      for (int i = 0; i < 10; i++)
         image.setPixel(i, 0, true);
      
      return true;
   }
   
   /**
    * Draws right border on the top of the array, at the parameter "col".
    * @param col column that should contain the right limit line
    * @return FALSE if border cannot be written, TRUE after writing
    */
   private boolean writeRightBorder(int col)
   {
      if (image==null)
         return false;
      
      //draws upper border on even columns
      if(col%2 == 0)
         image.setPixel(0, col, true);
      
      //sets every other pixel (including limit line) to true
      for (int row = 0; row < 10; row++)
      {
         if((row+1)%2 == 0)
            image.setPixel(row, col, true);
      }
      
      return true;
   }
   
   /**
    * Sets all elements in image array to false.
    */
   private void clearImage()
   {
      //traverses entirety of array and sets each element to false
      for (int i = 0; i < BarcodeImage.MAX_HEIGHT; i++)
         for (int j = 0; j < BarcodeImage.MAX_WIDTH; j++)
            image.setPixel(i, j, false);
   }
   
   /**
    * Displays value of private member "Text" string to console.
    */
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
   
   /**
    * Displays processed image (no whitespace) to console.
    */
   public void displayImageToConsole()
   {
      //Draw upper border with offset
      for (int i = 0; i < actualWidth + 2; i++)
         System.out.printf("-");
      System.out.printf("%n");
      
      //writes each row and border of array with borders, stopping at right edge
      int bottomEdge = BarcodeImage.MAX_HEIGHT;
      for(int i = bottomEdge - actualHeight; i < bottomEdge; i++)
      {
         System.out.printf("|");
         for(int j = 0; j < actualWidth; j++)
         {
            if(image.getPixel(i, j))
               System.out.printf("%c", BLACK_CHAR);
            else
               System.out.printf("%c", WHITE_CHAR);
         }
         System.out.printf("|%n");
      }
      
      //Draw lower border with offset
      for (int i = 0; i < actualWidth + 2; i++)
         System.out.printf("-");
      System.out.printf("%n");
   }
   
   /**
    * For debugging purposes. Displays unprocessed image by calling 
    * BarcodeImage's display method.
    */
   public void displayRawImage()
   {
      image.displayToConsole();
   }
   
   /**
    * Identifies lower left hand corner of the barcode and shifts it to the
    * bottom left corner of the array.
    */
   private void cleanImage()
   {
      int left = 0, bottom = BarcodeImage.MAX_HEIGHT - 1;
      
      //Finds the left and bottom edge of the barcode
      if (!image.getPixel(bottom, left))
      {
         for (int bottomLoop = BarcodeImage.MAX_HEIGHT - 1 ; bottomLoop >= 0 ;
            bottomLoop--)
         {
            for (int leftLoop = 0 ; leftLoop < BarcodeImage.MAX_WIDTH; 
               leftLoop++)
            {
               if (image.getPixel(bottomLoop, leftLoop))
               {
                  left=leftLoop;
                  leftLoop= BarcodeImage.MAX_WIDTH;
                  bottom=bottomLoop;
                  bottomLoop=-1;
               }
            }
         }
         //draws barcode on bottom left corner of the array 
         for (int i = bottom ; i >= 0 ; i--)
         {
            for (int j = left ; j < BarcodeImage.MAX_WIDTH ; j++)
            {
               image.setPixel((BarcodeImage.MAX_HEIGHT-1)-(bottom-i),j-left, 
                  image.getPixel(i, j));
               image.setPixel(i, j, false);
            }
         }
      }
   }
   /**
    * Determines the width of the barcode image (excludes white space in the
    * array). Calculated from left to right limit lines. Should be run after
    * image is pulled to the lower left corner.
    * @return the width of the barcode, including limit lines
    */
   private int computeSignalWidth()
   {
      boolean edge = true;
      
      //loop traverses the bottom limit line from left to right
      for(int i = 0; i < BarcodeImage.MAX_WIDTH && edge ; i++)
      {
         //boolean value is tested and column is returned once edge is found
         edge = image.getPixel(BarcodeImage.MAX_HEIGHT - 1, i);
         if(!edge)
            actualWidth = i;
      }
      return actualWidth;
   }
   
   /**
    * Determines the height of the barcode image (excludes white space in the
    * array). Calculated from lower to top limit lines, using the left most
    * limit line. Leftmost limit line is a solid block of TRUE values until
    * height is reached. Should be run after image is pulled to the lower left
    * corner.
    * @return the height of the barcode, including limit lines
    */
   private int computeSignalHeight()
   {
      boolean edge = true;
      
      //tests each element on the left most column, going "up" (towards 0)
      for(int i = BarcodeImage.MAX_HEIGHT - 1; i >= 0 && edge ; i--)
      {
         edge = image.getPixel(i, 0);
         //once edge is found, calculates height as the difference between the
         //lower limit line and the top limit line
         if(!edge)
         {
            actualHeight = BarcodeImage.MAX_HEIGHT - 1 - i;
         }
      }
      return actualHeight;
   }
}
/* [Output]
CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
-------------------------------------------
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
What a great resume builder this is!
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|***** * ***** ****** ******* **** **  |
|* ************************************|
|**  *    *  * * **    *    * *  *  *  |
|* *               *    **     **  *  *|
|**  *   * * *  * ***  * ***  *        |
|**      **    * *    *     *    *  * *|
|** *  * * **   *****  **  *    ** *** |
|**************************************|
----------------------------------------

DataMatrix image cleanup test.
Displaying raw barcode image: 
-------------------------------------------------------------------
|                                                                 |
|                                                                 |
|                                                                 |
|     * * * * * * * * * * * * * * * * * * * * *                   |
|     *                                       *                   |
|     ****** **** ****** ******* ** *** *****                     |
|     *     *    ******************************                   |
|     * **    * *        **  *    * * *   *                       |
|     *   *    *  *****    *   * *   *  **  ***                   |
|     *  **     * *** **   **  *    **  ***  *                    |
|     ***  * **   **  *   ****    *  *  ** * **                   |
|     *****  ***  *  * *   ** ** **  *   * *                      |
|     *****************************************                   |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
-------------------------------------------------------------------
Displaying raw, shifted image: 
-------------------------------------------------------------------
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|                                                                 |
|* * * * * * * * * * * * * * * * * * * * *                        |
|*                                       *                        |
|****** **** ****** ******* ** *** *****                          |
|*     *    ******************************                        |
|* **    * *        **  *    * * *   *                            |
|*   *    *  *****    *   * *   *  **  ***                        |
|*  **     * *** **   **  *    **  ***  *                         |
|***  * **   **  *   ****    *  *  ** * **                        |
|*****  ***  *  * *   ** ** **  *   * *                           |
|*****************************************                        |
-------------------------------------------------------------------
Displaying processed image: 
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
-------------------------------------------
*/
