/**
 * SaleRecord is an object that holds an array of Sale objects and contains
 * a value for number of sales in the array. Its methods are based on ordering
 * and maintenance of the array.
 * @author rshum
 *
 */
public class SalesRecord
{
   //maximum size of the sales array
   public static final int MAX_SALES_SIZE = 100;
   
   //methods return this int value if index of sale is not found
   public static final int INDEX_NOT_FOUND = -2;
   
   private int numberOfSales;
   private Sale[] salesList;
   
   /**
    * Default constructor
    */
   SalesRecord()
   {
      numberOfSales = 0;
      salesList = new Sale[MAX_SALES_SIZE];
   }
   
   /**
    * Constructor that takes values for both variables
    * @param numberOfSales number of sale objects in the array
    * @param salesList array to be set as salesList
    */
   SalesRecord(int numberOfSales, Sale[] salesList)
   {
      this.numberOfSales = numberOfSales;
      this.salesList = salesList;
   }
   
   /**
    * Copy constructor
    * @param salesRecord
    */
   SalesRecord(SalesRecord salesRecord)
   {
      numberOfSales = salesRecord.getNumberOfSales();
      salesList = salesRecord.getSalesList();
   }
   
   /**
    * Accessor that returns object copy of salesList
    * @return
    */
   public Sale[] getSalesList()
   {
      Sale[] tempSalesList = new Sale[MAX_SALES_SIZE];
      for (int i = 0; i < numberOfSales; i++) {
         tempSalesList[i] = new Sale(salesList[i]);
      }
      
      return tempSalesList;
   }
   
   /**
    * Mutator for salesList, assumes deep copy of Sale[]
    * @param salesList Sale[] to be assigned as new value of salesList
    * @return
    */
   public boolean setSalesList(Sale[] salesList)
   {
      if (salesList == null) {
         return false;
      }
      
      this.salesList = salesList;
      return true;
   }
   
   /**
    * Mutator for salesList, adds Sale object to Sale[]
    * @param sale object to be added to salesList
    * @return true if Sale can be added
    */
   public boolean addSale(Sale sale)
   {
      //returns false if Sale is null or array is full
      if (sale == null || numberOfSales == MAX_SALES_SIZE) {
         return false;
      }
      
      //adds sale to current top element of array and iterates numberOfSales
      salesList[numberOfSales] = new Sale(sale);
      numberOfSales++;
      return true;
   }
   
   /**
    * Accessor for saleList, returns sale object at specified index
    * @param index of sale in Sale[] to be returned
    * @return Sale at index of salesList
    */
   public Sale getSale(int index)
   {
      return new Sale(salesList[index]);
   }
   
   /**
    * Mutator for salesList, removes sale at specified index
    * @param index of sale in Sale[] to be removed
    * @return true if sale is removed
    */
   public boolean removeSale(int index)
   {
      //return false if array is empty or value is invalid
      if(index < 0 || index > numberOfSales || numberOfSales == 0) {
         return false;
      }
      
      //shifts all following entries down to close gap
      for (int i = index; i < numberOfSales - 1; i++) {
         salesList[i] = salesList[i+1];
      }
      
      //sets top element to null and decrements numberOfSales
      salesList[numberOfSales - 1] = null;
      numberOfSales--;
      return true;
   }
   
   /**
    * Finds and returns index of sale in salesList if it exists
    * @param saleID of sale to be found
    * @return index of sale in salesList if exists or -2 if it does not
    */
   public int findSaleIndex(String saleID)
   {
      //returns -1 if array is empty
      if(salesList[0] == null) {
         return -1;
      }
      
      //returns index if sale is found
      for (int index = 0; index < numberOfSales; index++) {
         if(salesList[index].getSaleID().equals(saleID)) {
            return index;
         }
      }
      
      return INDEX_NOT_FOUND;
   }
   
   /**
    * Accessor for numberOfSales variable
    * @return value of numberOfSales
    */
   public int getNumberOfSales()
   {
      return numberOfSales;
   }
   
   /**
    * Mutator for numberOfSales
    * @param numberOfSales int to be set as new value
    * @return true if value is set
    */
   public boolean setNumberOfSales(int numberOfSales)
   {
      //returns fals if value is invalid
      if(numberOfSales < 0 || numberOfSales > MAX_SALES_SIZE) {
         return false;
      }
      
      this.numberOfSales = numberOfSales;
      return true;
   }
   
   /**
    * Set saleID of sale in salesList
    * @param index of sale in salesList
    * @param saleID value to be set
    * @return true if saleID is set
    */
   public boolean setSaleID(int index, String saleID)
   {
      //return false if saleID is null or index is invalid
      if(index < 0 || index > MAX_SALES_SIZE || saleID == null) {
         return false;
      }
      
      salesList[index].setSaleID(saleID);
      return true;
   }
   
   /**
    * Returns saleID of sale at salesList[index]
    * @param index of sale whose saleID should be returned
    * @return String of saleID
    */
   public String getSaleID(int index)
   {
      //returns empty string if index is invalid
      if(index < 0 || index > MAX_SALES_SIZE) {
         return "";
      }
      
      return salesList[index].getSaleID();
   }
   
   /**
    * Sorts array based on type set by Sale.CONSTANT
    * @param type of sorting to be performed
    */
   public void sortArray(int type)
   {
      Sale.sortSalesArray(salesList, numberOfSales, type);
   }
   
   /**
    * Returns string of all sales present in the salesList
    */
   public String toString()
   { 
      String salesString = "";
      
      for (int i = 0; i < numberOfSales; i++) {
         salesString = salesString + salesList[i] + "\n";
      }
      
      return salesString;
   }
}
