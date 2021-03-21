/**
 * The "SimpleERP" Model manages employee and sales information, manipulated
 * through the Controller. It does not communicate with the View.
 * @author Raymond Shum
 *
 */
public class Model
{
   //Maximum size of the staff array defined by employee ID block
   public final static int MAX_STAFF 
      = Employee.EMPLOYEE_ID_END - Employee.EMPLOYEE_ID_START;
   
   //integer value returned when employee index cannot be found
   public static final int EMPLOYEE_DOES_NOT_EXIST = -1;
   
   Employee[] staff;
   SalesRecord masterSalesRecord;
   int staffNumber;
   int employeeID;
   int[] recoveredIDs;
   int numberOfRecoveredIDs;
   
   /**
    * Default constructor for Model
    */
   Model()
   {
      staff = new Employee[MAX_STAFF];
      masterSalesRecord = new SalesRecord();
      staffNumber = 0;
      employeeID = Employee.EMPLOYEE_ID_START;
      recoveredIDs = new int[MAX_STAFF];
      numberOfRecoveredIDs = 0;
   }
   
   /**
    * Updates master sales record with current employee sales histories
    */
   public void refreshMasterSalesRecord()
   {
      masterSalesRecord = new SalesRecord();
      Sale[] temp;
      int salesNumber = 0;
      
      //for each employee, add all sales entries to master sales record
      for(int employee = 0; employee < staffNumber; employee++) {
         
         salesNumber = staff[employee].getSalesHistory().getNumberOfSales();
         temp = staff[employee].getSalesHistory().getSalesList();
         
         for(int sale = 0; sale < salesNumber; sale++) {
            masterSalesRecord.addSale(new Sale(temp[sale]));
         }
      }
   }
   
   /**
    * Return deep copy of master sales record
    * @return SalesRecord object copy of masterSalesRecord
    */
   public SalesRecord getMasterSalesRecord()
   {
      SalesRecord temp = new SalesRecord();
      for(int i = 0; i < masterSalesRecord.getNumberOfSales(); i++) {
         temp.addSale(masterSalesRecord.getSale(i));
      }
      return temp;
   }
   
   /**
    * Set masterSalesRecord equal to parameter. Assumes object copy.
    * @param salesRecord object to be set as masterSalesRecord
    * @return true if salesRecord is not null
    */
   public boolean setMasterSalesRecord(SalesRecord salesRecord)
   {
      if(salesRecord == null) {
         return false;
      }
      
      masterSalesRecord = salesRecord;
      return true;
   }
   
   /**
    * Accessor for staffNumber private member
    * @return int value of staffNumber
    */
   public int getStaffNumber()
   {
      return staffNumber;
   }
   
   /**
    * Mutator for staffNumber private member
    * @param staffNumber int of new value
    * @return true if successful, false if value is invalid
    */
   public boolean setStaffNumber(int staffNumber)
   {
      //filter if value is impossible or exceed staff limits
      if(staffNumber < 0 || staffNumber > MAX_STAFF) {
         return false;
      }
      
      this.staffNumber = staffNumber;
      return true;
   }
   
   /**
    * Accessor for employeeID private member
    * @return int value of employeeID
    */
   public int getEmployeeID()
   {
      return employeeID;
   }
   
   /**
    * Mutator for employeeID private member
    * @param employeeID int of new value
    * @return true if value is not filtered
    */
   public boolean setEmployeeID(int employeeID)
   {
      //employee parameter ID must be within employeeID block
      if(employeeID < Employee.EMPLOYEE_ID_START
            || employeeID > Employee.EMPLOYEE_ID_END) {
         return false;
      }
      
      this.employeeID = employeeID;
      return true;
   }
   
   /**
    * Accessor to return an object copy of the staff private member
    * @return Employee[] object copy of staff array
    */
   public Employee[] getStaff()
   {
      Employee[] temp = new Employee[staffNumber];
      
      for(int i = 0; i < staffNumber; i++) {
         temp[i] = new Employee(staff[i]);
      }
      
      return temp;
   }
   
   /**
    * Mutator to set the parameter as new value of staff array. Assumes deep
    * copy.
    * @param staff Employee[] to be set as new value of staff
    * @return true if Employee[] staff is not null
    */
   public boolean setStaff(Employee[] staff)
   {
      if(staff == null) {
         return false;
      }
      
      this.staff = staff;
      return true;
   }
   
   /**
    * Mutator to add an employee to the staff array
    * @param employee object to be added to staff array
    * @return true if employee can be added
    */
   public boolean addEmployee(Employee employee)
   {
      //return false if parameter is null or max staff number has been reached
      if(employee == null || staffNumber == MAX_STAFF) {
         return false;
      }
      
      //Add an employee, set ID (if no pre-used IDs), iterate staff/employee#
      if(numberOfRecoveredIDs == 0) {
      staff[staffNumber] = new Employee(employee);
      staff[staffNumber].setEmployeeID(employeeID);
      addEmployeeSales(employeeID); //add employee sales to masterSalesRecord
      staffNumber++;
      employeeID++;
      }
      //if there are pre-owned id's, use those before generating a new one
      else {
      staff[staffNumber] = new Employee(employee);
      staff[staffNumber].setEmployeeID(recoveredIDs[numberOfRecoveredIDs - 1]);
      addEmployeeSales(recoveredIDs[numberOfRecoveredIDs - 1]);
      staffNumber++;
      numberOfRecoveredIDs--;
      }
      
      //sort staff array by lowest ID
      sortStaff(Employee.ID);
      return true;
      
   }
   
   /**
    * Add values of employee's salesHistory to masterSalesRecord
    * @param employeeID represents Employee.employeeID of object holding sales
    * to be added to masterSalesRecord
    * @return true if employee exists in staff array and sales can be added
    */
   private boolean addEmployeeSales(int employeeID)
   {
      //find index of employee in staff array
      int index = employeeExists(employeeID);
      
      //return false if employee not found
      if(index == EMPLOYEE_DOES_NOT_EXIST) {
         return false;
      }
      
      //create object copy of target employee's sales history
      SalesRecord temp = staff[index].getSalesHistory();
      
      //add all sales entries to masterSalesRecords
      for(int i = 0; i < temp.getNumberOfSales(); i++) {
         masterSalesRecord.addSale(temp.getSale(i));
      }
      
      //sort all sales by lowest sale ID
      sortMasterSalesRecord(Sale.LOWEST_SALE_ID);
      return true;
         
   }
   
   /**
    * Remove target employee's sales from masterSalesRecord
    * @param employeeID target employee whose sales should be removed
    * @return true if employee exists and false otherwise
    */
   private boolean removeEmployeeSales(int employeeID)
   {
      int index = employeeExists(employeeID);
      if(index == EMPLOYEE_DOES_NOT_EXIST) {
         return false;
      }
      
      int salesNumber = masterSalesRecord.getNumberOfSales();
      String saleID = "";
      
      //Searches masterSalesRecord from top down and removes sales associated
      //with employee. Must be top-down search: sales are sorted by lowest ID
      //after each removal.
      for(int i = salesNumber - 1; i >= 0 ; i--) {
         saleID = masterSalesRecord.getSaleID(i);
         
         //first 3 digits of SaleID are the employeeID - if match, then remove
         if((saleID.substring(0, 3).equals(Integer.toString(employeeID)))) {
            masterSalesRecord.removeSale(i);
         }
      }
      return true;
   }
   
   /**
    * Removes employee from staff list and associated sales from 
    * masterSalesRecord
    * @param employeeID target employee to be removed
    * @return true if employee is found
    */
   public boolean removeEmployee(int employeeID)
   {
      //returns false if employee is found and staff[] is not empty
      int employeeIndex = employeeExists(employeeID);
      if(staffNumber == 0 || employeeIndex == EMPLOYEE_DOES_NOT_EXIST) {
         return false;
      }
      
      removeEmployeeSales(employeeID);

      //shifts following employees down in the array after removing target 
      for (int i = employeeIndex; i < staffNumber - 1; i++) {
            staff[i] = staff[i+1];
      }
      
      //adds removed employee's ID to array of recovered IDs
      recoveredIDs[numberOfRecoveredIDs] = employeeID;
      numberOfRecoveredIDs++;
      
      //sets top position of array to null, decrement employee count and sort
      staff[staffNumber - 1] = null;
      staffNumber--;
      sortStaff(Employee.ID);;
      return true;
   }
   
   /**
    * Add sale to target employee's salesHistory private member
    * @param employeeID target employee to add a sale object to
    * @param sale object to be added to target employee's salesHistory
    * @return true if employee and sale exist
    */
   public boolean addSale(int employeeID, Sale sale) {
      
      //finds index of employee in staff[]
      int index = employeeExists(employeeID);
      if(index == EMPLOYEE_DOES_NOT_EXIST || sale == null) {
         return false;
      }
      
      //add sale to employee at index position
      staff[index].addSale(sale);
      
      //refresh employee's sales in masterSalesRecord, then sort
      removeEmployeeSales(employeeID);
      addEmployeeSales(employeeID);
      sortMasterSalesRecord(Sale.LOWEST_SALE_ID);
      return true;
   }
   
   /**
    * Remove sale from both employee and masterSalesRecord
    * @param saleID of sale to be removed
    * @return true if parameter and target sale exists
    */
   public boolean removeSale(String saleID)
   {
      //finds index of sale in masterSalesRecord
      int index = masterSalesRecord.findSaleIndex(saleID);
      
      //returns false if either parameter is null or sale is not found
      if(saleID == null || index < 0) {
         return false;
      }
      
      //finds employee based on SaleID and removes sale from its saleHistory
      int employeeID = Integer.parseInt(saleID.substring(0, 3));
      staff[employeeExists(employeeID)].removeSale(saleID);
      
      //removes sale from masterSalesRecord, updates array and sorts
      masterSalesRecord.removeSale(index);
      removeEmployeeSales(employeeID);
      addEmployeeSales(employeeID);
      return true;
   }
   
   /**
    * Checks to see if a sale exists in the masterSalesRecord
    * @param saleID of sale to check
    * @return true if sale exists
    */
   public boolean saleExists(String saleID)
   {
      //findSaleIndex will return -1 if sale is not found
      return masterSalesRecord.findSaleIndex(saleID) >= 0;
   }
   
   /**
    * Check if employee exists in staff array
    * @param employeeID of employee to check
    * @return index of employee in staff array or -1 if not found
    */
   public int employeeExists(int employeeID)
   {
      //check parameter against each employee's employeeID private member
      for(int i = 0; i < staffNumber; i++) {
         if(employeeID == staff[i].getEmployeeID())
            return i;
      }
      
      return EMPLOYEE_DOES_NOT_EXIST;
   }
   
   /**
    * Sorts masterSalesRecord by type specified by Sale.CONSTANT
    * @param type Sale.CONSTANT identifier of value to be sorted
    */
   public void sortMasterSalesRecord(int type)
   {
      masterSalesRecord.sortArray(type);
   }
   
   /**
    * Returns string of concatenated values of employee's first and last names
    * @param index of target employee in staff[]
    * @return String of concatenated values
    */
   public String getEmployeeName(int index)
   {
      return (staff[index].getFirstName() 
            + " " + staff[index].getLastName());
   }
   
   /**
    * Returns string of an employee's salesHistory.toString
    * @param employeeID target employee
    * @return String of target employee's salesHistory
    */
   public String getEmployeeSalesHistory(int employeeID)
   {
      return (staff[employeeExists(employeeID)].getSalesHistory().toString());
   }
   
   /**
    * Returns string of masterSalesRecord and total sales number
    * @return String representation of full sales report
    */
   public String getMasterSalesReport()
   {
      int totalSales = 0;
      String salesReport = masterSalesRecord.toString();
      
      //adds all salesFigure values for each sale in masterSalesRecord
      for (int i = 0; i < masterSalesRecord.getNumberOfSales(); i++) {
         totalSales += masterSalesRecord.getSale(i).getSalesFigure();
      }
      
      //format lower border
      for(int i = 0; i < 60; i++)
      {
         salesReport += "=";
      }
      
      //format total sales string
      salesReport += String.format("%n%-40s  $%-20d", 
            "Total Sales:", totalSales);
      
      return salesReport;
   }
   
   /**
    * toString method returns String containing employee and associated sales
    * @return String of model object
    */
   public String toString()
   {
      String model = "";
      
      for (int i = 0; i < staffNumber; i++) {
         model = model + staff[i];
      }
      
      return model;
   }
   
   /**
    * Sorts staff array based on parameter defined by Employee.CONSTANT
    * @param type Employee.CONSTANT of sort that should be performed
    */
   public void sortStaff(int type)
   {
      Employee.sortArray(staff, staffNumber, type);
   }
   
   /**
    * Sort salesHistory of an employee within the staff array
    * @param employeeID target employee 
    * @param type Sale.CONSTANT of sort to be performed
    */
   public void sortStaffSalesArray(int employeeID, int type)
   {
      //sort an employee's salesHistory if it exists
      int index = employeeExists(employeeID);
      if (index != EMPLOYEE_DOES_NOT_EXIST) {
         staff[index].sortSalesHistory(type);
      }
   }
   
   /**
    * Populate model with sample employee and sales entries
    */
   public void loadSampleData()
   {
      addEmployee(new Employee("Larry", "Chiem"));
      addSale(100, new Sale(2019, 1, 20, 1500));
      addSale(100, new Sale(2020, 5, 1, 100));
      addSale(100, new Sale(2020, 7, 23, 3000));
      
      addEmployee(new Employee("Raymond", "Shum"));
      addSale(101, new Sale(2019, 3, 5, 90));
      addSale(101, new Sale(2020, 5, 17, 2300));
      addSale(101, new Sale(2020, 10, 9, 150));
      
      addEmployee(new Employee("Ian", "Rowe"));
      addSale(102, new Sale(2020, 4, 2, 500));
      addSale(102, new Sale(2020, 5, 7, 5000));
      addSale(102, new Sale(2020, 7, 1, 2500));
      
      addEmployee(new Employee("Nicholas", "Stankovich"));
      addSale(103, new Sale(2019, 10, 4, 3500));
      addSale(103, new Sale(2020, 9, 27, 2500));
      addSale(103, new Sale(2020, 12, 15, 1500));
   }
   
}
