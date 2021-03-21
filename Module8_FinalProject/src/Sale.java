import java.time.*;

/**
 * Sales object holds the date, ID and amount of a sale. It provides a static
 * method to sort Sales arrays.
 * @author Raymond Shum
 *
 */
public class Sale
{
   //Final constants determine type of sorting to be performed on Sale[]
   public static final int HIGHEST_SALE_ID = 5;
   public static final int LOWEST_SALE_ID = 4;
   public static final int LOWEST_SALE = 3;
   public static final int HIGHEST_SALE = 2;
   public static final int MOST_RECENT = 1;
   public static final int OLDEST = 0;
   
   private LocalDate salesDate;
   private int salesFigure;
   private String saleID;
   
   /**
    * Default constructor
    */
   Sale()
   {
      salesDate = LocalDate.now();
      salesFigure = 0;
      saleID = "NONE";
   }
   
   /**
    * Constructor that takes date and salesFigure parameters
    * @param date value to be assigned to salesDate
    * @param salesFigure value to be assigned to salesFigure
    */
   Sale(String date, int salesFigure)
   {
      //validates that date is in LocalDate format
      try {
         salesDate = LocalDate.parse(date);
      }
      catch (Exception e) {
         salesDate = LocalDate.now();
      }
      
      this.salesFigure = salesFigure;
      saleID = "NONE";
   }
   
   /**
    * Constructor  that takes parameters for year, month, day and salesFigure
    * @param year value of year of sale
    * @param month value of month of sale
    * @param dayOfMonth value of day of sale
    * @param salesFigure value of sales amount
    */
   Sale(int year, int month, int dayOfMonth, int salesFigure)
   {
      //if date parameters are invalid, will assign current date as salesDate
      if(invalidDateParameters(year, month, dayOfMonth)) {
         salesDate = LocalDate.now();
      }
      else {
         salesDate = LocalDate.of(year, month, dayOfMonth);
      }
      
      this.salesFigure = salesFigure;
      saleID = "";
   }
   
   /**
    * Constructor that takes values for all variables
    * @param year value of year of sale
    * @param month value of month of sale
    * @param dayOfMonth value of day of sale
    * @param salesFigure value of sales amount
    * @param saleID saleID of sale
    */
   Sale(int year, int month, int dayOfMonth, int salesFigure, String saleID)
   {
      if(invalidDateParameters(year, month, dayOfMonth)) {
         salesDate = LocalDate.now();
      }
      else {
         salesDate = LocalDate.of(year, month, dayOfMonth);
      }
      
      this.salesFigure = salesFigure;
      this.saleID = saleID;
   }
   
   /**
    * Copy constructor for Sale
    * @param sale
    */
   Sale(Sale sale)
   {
      salesFigure = sale.salesFigure;
      saleID = sale.saleID;
      salesDate = LocalDate.of(sale.getYear(), sale.getMonth(), sale.getDay());
   }
   
   /**
    * Mutator that sets value of saleID
    * @param saleID value to be set as saleID
    * @return true if value is set
    */
   public boolean setSaleID(String saleID)
   {
      if(saleID == null) {
         return false;
      }
      
      this.saleID = saleID;
      return true;
   }
   
   /**
    * Accessor for saleID
    * @return saleID value of variable
    */
   public String getSaleID()
   {
      return saleID;
   }
   
   /**
    * Accessor for salesDate
    * @return LocalDate value of salesDate
    */
   public LocalDate getSalesDate()
   {
      return LocalDate.of(getYear(), getMonth(), getDay());
   }
   
   /**
    * Mutator for salesDate
    * @param year value of year of sale
    * @param month value of month of sale
    * @param dayOfMonth value of day of sale
    * @return true if value is set
    */
   public boolean setSalesDate(int year, int month, int dayOfMonth)
   {
      //return false if date is invalid
      if(invalidDateParameters(year, month, dayOfMonth)) {
         return false;
      }
      
      salesDate = LocalDate.of(year, month, dayOfMonth);
      return true;
   }
   
   /**
    * Accessor for month of salesDate
    * @return month value of salesDate
    */
   public int getMonth()
   {
      return salesDate.getMonthValue();
   }
   
   /**
    * Mutator for month of salesDate
    * @param month value to be set
    * @return false if value is invalid
    */
   public boolean setMonth(int month)
   {
      if(invalidMonth(month))
         return false;
      
      salesDate = LocalDate.of(getYear(), month, getDay());
      return true;
   }
   
   /**
    * Accessor for day value of salesDay
    * @return int value of day in salesDate
    */
   public int getDay()
   {
      return salesDate.getDayOfMonth();
   }
   
   /**
    * Mutator for day value of salesDay
    * @param dateOfMonth value to be set
    * @return true if value is set
    */
   public boolean setDay(int dateOfMonth)
   {
      //returns false if date value is invalid
      if(invalidDate(getYear(), getMonth(), dateOfMonth)) {
         return false;
      }
      
      salesDate = LocalDate.of(getYear(), getMonth(), dateOfMonth);
      return true;
   }
   
   /**
    * Accessor for year value of salesDate
    * @return int value of year of salesDate
    */
   public int getYear()
   {
      return salesDate.getYear();
   }
   
   /**
    * Mutator for year value of salesDate
    * @param year to be set as new year value of salesDate
    * @return true if year is set
    */
   public boolean setYear(int year)
   {
      //returns false if year false outside valid range
      if(invalidYear(year)){
         return false;
      }
      
      salesDate = LocalDate.of(year, getMonth(), getDay());
      return true;
   }
   
   /**
    * Checks if month exists
    * @param month value to be checked
    * @return true if month is valid
    */
   private boolean invalidMonth(int month)
   {
      //Month must be between Jan and Dec, inclusive
      return (month < 1 || month > 12);
   }
   
   /**
    * Checks to see if year is valid
    * @param year value to be checked
    * @return true if year falls between valid range
    */
   private boolean invalidYear(int year)
   {
      //Range is set arbitrarily for assignment, filters input error
      return (year < 2000 || year > 2050);
   }
   
   /**
    * Checks if date exists
    * @param year value of year to be checked
    * @param month value of month to be checked
    * @param dateOfMonth value of date to be checked
    * @return true if date is valid
    */
   private boolean invalidDate(int year, int month, int dateOfMonth)
   {
      LocalDate tempDate = LocalDate.of(year, month, 1);
      int maxDaysInMonth = tempDate.getMonth().length(tempDate.isLeapYear());
      
      //returns true if date exists within valid range of days in month
      return (dateOfMonth <= 0 || dateOfMonth > maxDaysInMonth); 
   }
   
   /**
    * Checks if date parameters are invalid as individual changes
    * @param year value to be checked
    * @param month value to be checked
    * @param dateOfMonth value to be checked
    * @return false if any parameter is invalid
    */
   private boolean invalidDateParameters(int year, int month, int dateOfMonth)
   {
      return (invalidMonth(month) 
            || invalidYear(year) 
            || invalidDate(year, month, dateOfMonth));
   }
   
   /**
    * Accessor for salesFigure variable
    * @return salesFigure int value
    */
   public int getSalesFigure()
   {
      return salesFigure;
   }
   
   /**
    * mutator for salesFigure value
    * @param salesFigure value to be set
    * @return true if value is set
    */
   public boolean setSalesFigure(int salesFigure)
   {
      //filters bad values (no entries for nonexistent sales, either)
      if(salesFigure <= 0) {
         return false;
      }
      
      this.salesFigure = salesFigure;
      return true;
   }
   
   /**
    * Returns formatted string of Sale variables
    */
   public String toString()
   {
      return String.format("%-20s %-20s $%-20d", 
            saleID, salesDate, salesFigure);
   }
   
   /**
    * Sorts Sale array based on type defined by Sale.CONSTANT, bubble sort.
    * @param array Sale[] to be sorted
    * @param arrayLength number of Sale object in Sale[]
    * @param type type of sorting to be performed
    */
   public static void sortSalesArray(Sale[] array, int arrayLength, int type)
   {
      for(int i = 0; i < arrayLength - 1; i++) {
         for(int j = 0; j < arrayLength - i - 1; j++) {
            if (type == MOST_RECENT) {
               sortByMostRecent(array, i, j);
            }
            else if(type == OLDEST) {
               sortByOldest(array, i, j);
            }
            else if(type == HIGHEST_SALE) {
               sortByTopSale(array, i, j);
            }
            else if(type == LOWEST_SALE) {
               sortByLowSale(array, i, j);
            }
            else if(type == LOWEST_SALE_ID) {
               sortByLowSaleID(array, i, j);
            }
            else if(type == HIGHEST_SALE_ID) {
               sortByTopSaleID(array, i, j);
            }
         }
      }
   }
   
   /**
    * Sorts array based on highest sales amount
    * @param array
    * @param i
    * @param j
    */
   private static void sortByTopSale(Sale[] array, int i, int j)
   {
      if(array[j].getSalesFigure() < array[j+1].getSalesFigure()) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Sorts array based on lowest sale amount
    * @param array
    * @param i
    * @param j
    */
   private static void sortByLowSale(Sale[] array, int i, int j)
   {
      if(array[j].getSalesFigure() > array[j+1].getSalesFigure()) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Sorts array based on oldest saleDate
    * @param array
    * @param i
    * @param j
    */
   private static void sortByOldest(Sale[] array, int i, int j)
   {
      if(array[j].getSalesDate().
            compareTo(array[j+1].getSalesDate()) > 0) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Sorts array based on newest saleDate
    * @param array
    * @param i
    * @param j
    */
   private static void sortByMostRecent(Sale[] array, int i, int j)
   {
      if(array[j].getSalesDate().
            compareTo(array[j+1].getSalesDate()) < 0) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Sorts array based on lowest saleID
    * @param array
    * @param i
    * @param j
    */
   private static void sortByLowSaleID(Sale[] array, int i, int j)
   {
      
      if(Integer.parseInt(array[j].getSaleID()) 
            > Integer.parseInt(array[j+1].getSaleID())) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Sorts array based on highest saleID
    * @param array
    * @param i
    * @param j
    */
   private static void sortByTopSaleID(Sale[] array, int i, int j)
   {
      if(Integer.parseInt(array[j].getSaleID()) 
            < Integer.parseInt(array[j+1].getSaleID())) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Swaps two elements in the array
    * @param array
    * @param i
    * @param j
    */
   private static void performSwap(Sale[] array, int i, int j)
   {
      Sale tempSale = new Sale(array[j]);
      array[j] = new Sale(array[j+1]);
      array[j+1] = tempSale;
   }
}
