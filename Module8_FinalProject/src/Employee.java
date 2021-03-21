
/**
 * Employee is an object that holds an employee's identifying information and
 * their sales history in the form of a SalesRecord object.
 * @author Raymond Shum
 *
 */
public class Employee
{
   //these final variables are used to determine the type of sorting to be
   //performed by a static sorting method in this class
   public static final int MAX_SALES_PER_EMPLOYEE = 10;
   public static final int EMPLOYEE_ID_START = 100;
   public static final int EMPLOYEE_ID_END = 199;
   public static final int ID = 0;
   public static final int FIRST_NAME = 1;
   public static final int LAST_NAME = 2;
   
   private String firstName;
   private String lastName;
   private int employeeID;
   private SalesRecord salesHistory;
   
   /**
    * Default constructor
    */
   Employee()
   {
      firstName = "";
      lastName = "";
      employeeID = 0;
      salesHistory = new SalesRecord();
   }
   
   /**
    * Constructor that accepts first and last names as parameters
    * @param firstName value to be assigned to firstName
    * @param lastName value to be assigned to lastName
    */
   Employee(String firstName, String lastName)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      employeeID = 0;
      salesHistory = new SalesRecord();
   }
   
   /**
    * Constructor that accepts first/last names and employee ID
    * @param firstName value to be assigned to firstName
    * @param lastName value to be assigned to lastName
    * @param employeeID value to be assigned to employeeID
    */
   Employee(String firstName, String lastName, int employeeID)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.employeeID = employeeID;
      salesHistory = new SalesRecord();
   }
   /**
    * Constructor that accepts parameters for all instance variables
    * @param firstName value to be assigned to firstName
    * @param lastName value to be assigned to lastName
    * @param employeeID value to be assigned to employeeID
    * @param salesHistory value to be assigned to salesHistory
    */
   Employee(String firstName, String lastName, int employeeID, 
         SalesRecord salesHistory)
   {
      this.firstName = firstName;
      this.lastName = lastName;
      this.employeeID = employeeID;
      setSalesHistory(salesHistory);
      setSaleIDs();
   }
   
   /**
    * Copy constructor
    * @param employee object to be copied
    */
   Employee(Employee employee)
   {
      firstName = employee.getFirstName();
      lastName = employee.getLastName();
      employeeID = employee.getEmployeeID();
      salesHistory = employee.getSalesHistory();
   }
   
   /**
    * Mutator. Sets first name as parameter
    * @param name to be set as firstName
    * @return
    */
   public boolean setFirstName(String name)
   {
      //returns false if parameter is invalid
      if(name == null || name == "") {
         return false;
      }
      
      firstName = name;
      return true;
   }
   
   /**
    * Accessor for firstName
    * @return value of firstName
    */
   public String getFirstName()
   {
      return firstName;
   }
   
   /**
    * Mutator for lastName
    * @param name value to be set as lastNAme
    * @return true if name is set
    */
   public boolean setLastName(String name)
   {
      //returns false if parameter is invalid
      if(name == null || name == "") {
         return false;
      }
      
      lastName = name;
      return true;
   }
   
   /**
    * Accessor for lastName
    * @return lastName value of variable
    */
   public String getLastName()
   {
      return lastName;
   }
   
   /**
    * Mutator for employeeID
    * @param employeeID to be set as new value
    * @return true if ID is set
    */
   public boolean setEmployeeID(int employeeID)
   {
      //returns false if ID is outside of ID block
      if(employeeID < EMPLOYEE_ID_START || employeeID > EMPLOYEE_ID_END) {
         return false;
      }
      
      //sets value of employee ID and then updates sale IDs in salesHistory
      this.employeeID = employeeID;
      setSaleIDs();
      return true;
   }
   
   /**
    * Accessor for employeeID
    * @return employeeID value of variable
    */
   public int getEmployeeID()
   {
      return employeeID;
   }
   
   /**
    * Mutator for salesHistory. Creates deep copy.
    * @param salesHistory new value for this.salesHistory
    * @return true if salesHistory can be set
    */
   public boolean setSalesHistory(SalesRecord salesHistory)
   {
      if(salesHistory == null) {
         return false;
      }
      
      this.salesHistory = new SalesRecord(salesHistory);
      return true;
   }
   
   /**
    * Accessor for salesHistory
    * @return salesHistory value of variable
    */
   public SalesRecord getSalesHistory()
   {
      return new SalesRecord(salesHistory);
   }
   
   /**
    * Adds sale to salesHistory
    * @param sale to be added to saleshistory
    * @return true if sale is can be added
    */
   public boolean addSale(Sale sale)
   {
      //if sale is not null and max sales not reached, add and update saleIDs
      if(salesHistory.getNumberOfSales() < MAX_SALES_PER_EMPLOYEE
            && sale != null) {
         salesHistory.addSale(sale);
         setSaleIDs();
         return true;
      }
      return false;
   }
   
   /**
    * Remove sale from salesHistory
    * @param saleID id of sale to be removed
    * @return true if sale can be removed
    */
   public boolean removeSale(String saleID) 
   {
      //remove sale if it exists and return true
      if(salesHistory.removeSale(salesHistory.findSaleIndex(saleID))) {
         setSaleIDs();
         return true;
      }
      
      return false;
   }
   
   /**
    * Sort the salesHistory array by type defined by Sale.CONSTANT 
    * @param type sort order for salesHistory
    */
   public void sortSalesHistory(int type)
   {
      salesHistory.sortArray(type);
      setSaleIDs();
   }
   
   /**
    * Set all saleIDs for saleHistory. Sale IDs are the employee ID plus the
    * index of the sale in saleHistory
    */
   private void setSaleIDs()
   {
      String saleID = "";
      for(int i = 0; i < salesHistory.getNumberOfSales(); i++) {
         saleID = Integer.toString(employeeID) + Integer.toString(i);
         salesHistory.setSaleID(i, saleID);
      }
   }
   
   /**
    * Returns string of value of all instance variables
    */
   public String toString()
   {
      return String.format("%s %s %d%n%s", 
            firstName, lastName, employeeID, salesHistory);
   }
   
   /**
    * Unoptimized bubble sort of employee[] 
    * @param array employee[] to be sorted
    * @param arrayLength employees in employee[]
    * @param type type of sort to be performed
    */
   public static void sortArray(Employee[] array, int arrayLength, int type)
   {
      //calls different sort methods based on Employee.CONSTANT
      for(int i = 0; i < arrayLength - 1; i++) {
         for(int j = 0; j < arrayLength - i - 1; j++) {
            if (type == ID) {
               sortByID(array, i, j);
            }
            else if (type == FIRST_NAME) {
               sortByFirstName(array, i, j);
            }
            else if (type == LAST_NAME) {
               sortByLastName(array, i, j);
            }
         }
      }
   }
   
   /**
    * Sort by lowest ID
    * @param array
    * @param i
    * @param j
    */
   private static void sortByID(Employee[] array, int i, int j)
   {
      if(array[j].getEmployeeID() > array[j+1].getEmployeeID()) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Lexicographic sort of array by first name
    * @param array
    * @param i
    * @param j
    */
   private static void sortByFirstName(Employee[] array, int i, int j)
   {
      if(array[j].getFirstName().compareToIgnoreCase(array[j+1].getFirstName()) 
            > 0 ) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Lexicographic sort of array by last name
    * @param array
    * @param i
    * @param j
    */
   private static void sortByLastName(Employee[] array, int i, int j)
   {
      if(array[j].getLastName().compareToIgnoreCase(array[j+1].getLastName()) 
            > 0 ) {
         performSwap(array, i, j);
      }
   }
   
   /**
    * Swaps elements in an array
    * @param array
    * @param i
    * @param j
    */
   private static void performSwap(Employee[] array, int i, int j)
   {
      Employee tempEmployee = new Employee(array[j]);
      array[j] = new Employee(array[j+1]);
      array[j+1] = tempEmployee;
   }
}
