import java.time.LocalDate;
import java.util.Scanner;

/**
 * The "SimpleERP" Controller manipulates Model through accessors and mutators.
 * It calls methods from View to provide a representation of Model.
 * @author Raymond Shum
 *
 */
public class Controller
{
   Model myModel;
   View myView;
   Scanner userInput;
   String input;
   boolean quitConfirmed;
   
   /**
    * Constructor. Initializes private members.
    * @param myModel
    * @param myView
    */
   Controller(Model myModel, View myView)
   {
      this.myModel = myModel;
      this.myView = myView;
      userInput = new Scanner(System.in);
      input = "";
      quitConfirmed = false;

      //loads sample data and enters program loop
      myModel.loadSampleData();
      enterMainMenu();
   }
   
   /**
    * Main loop of program, runs until user selects quit option in application
    */
   public void enterMainMenu()
   {
      do {
         //displays main menu options and asks for input until valid entry
         myView.displayMainMenu();
         myView.displayOptionEntryPrompt();
         mainMenuOptionSelection(getFormattedUserInput());
      }while(!quitConfirmed);

      userInput.close();
   }

   /**
    * Calls methods for corresponding menu selections, starting from the main
    * menu prompt.
    * @param input String of user's input
    * @return true if valid input is passed
    */
   private boolean mainMenuOptionSelection(String input)
   {
      switch(input){
      case "1":
         enterManageEmployeesMenu();
         return true;
      case "2":
         enterManageEmployeeSalesMenu();
         return true;
      case "3":
         enterViewSalesReport();
         return true;
      case "Q":
         enterQuitConfirmation();
         return true;
      default:
         myView.displayInvalidEntryPrompt();
         return false;
      }
   }
   
   /**
    * Calls methods for Manage Employees menu.
    */
   public void enterManageEmployeesMenu()
   {
      boolean backToMainMenu = false;
      
      //loop that controls Manage Employees menu
      do {
         
         //calls view to display menu information and option prompt
         myView.displayManageEmployeesMenu(getFormattedEmployeeList());
         myView.displayOptionEntryPrompt();
         
         //takes user input and calls corresponding methods
         backToMainMenu = 
               manageEmployeesOptionSelection(getFormattedUserInput());

         if(quitConfirmed == true) {
            backToMainMenu = true;
         }

      }while(!backToMainMenu);
   }

   /**
    * Calls methods for Manage Employee Sales Menu.
    */
   public void enterManageEmployeeSalesMenu()
   {
      boolean backToMainMenu = false;
      
      do {
         myView.displayManageEmployeeSalesMenu(getFormattedEmployeeList());
         myView.displayOptionEntryPrompt();
         backToMainMenu = 
               manageEmployeeSalesOptionSelection(getFormattedUserInput());
         if(quitConfirmed == true) {
            backToMainMenu = true;
         }

      }while(!backToMainMenu);
   }

   /**
    * Calls methods corresponding to Manage Employee Sales menu.
    * @param input String of user's input
    * @return true (breaks loop) if user chooses to leave menu
    */
   private boolean manageEmployeeSalesOptionSelection(String input)
   {
      switch(input){
      case "B":
         return true;
      case "F":
         myModel.sortStaff(Employee.FIRST_NAME);
         return false;
      case "L":
         myModel.sortStaff(Employee.LAST_NAME);
         return false;
      case "E":
         myModel.sortStaff(Employee.ID);
         return false;
      case "V":
         enterEmployeeSalesHistoryMenu();
         return false;
      case "Q":
         enterQuitConfirmation();
         return false;
      default:
         myView.displayInvalidEntryPrompt();
         return false;
      }
   }

   /**
    * Calls methods related to Employee Sales History menu
    */
   public void enterEmployeeSalesHistoryMenu()
   {
      //Asks user for employee ID of target employee
      myView.displayEmployeeIDRequest();
      input = getFormattedUserInput();

      //exits method if employee does not exist
      if(!confirmEmployeeID(input)) {
         return;
      }
      
      int index = Integer.parseInt(input);
      
      //loop that controls Employee Sales History Menu
      boolean backToPreviousMenu = false;
      do {
         //Outputs menu based on employee's staff[] index
         myView.displayEmployeeSalesHistoryMenu(
               myModel.getEmployeeName(myModel.employeeExists(index)),
               myModel.getEmployeeSalesHistory(index));
         myView.displayOptionEntryPrompt();
         
         //prompts user to select menu options
         backToPreviousMenu = 
               employeeSalesHistoryOptionSelection(
                     getFormattedUserInput(),
                     index);
         if(quitConfirmed == true) {
            backToPreviousMenu = true;
         }
      }while(!backToPreviousMenu);
   }

   /**
    * Calls methods related to Sales Report Menu
    */
   public void enterViewSalesReport()
   {
      boolean backToMainMenu = false;
      
      //Loop that controls Sales Report Menu
      do
      {
         //outputs menu based on master sales report string
         myView.displayViewSalesReport(myModel.getMasterSalesReport());
         myView.displayOptionEntryPrompt();
         
         //prompts user to select menu option
         backToMainMenu = 
               viewSalesReportOptionSelection(getFormattedUserInput());
         if(quitConfirmed == true) {
            backToMainMenu = true;
         }
      }while(!backToMainMenu);

   }
   
   /**
    * Calls methods corresponding to option entries in the Sales Report Menu.
    * @param input String of user's input
    * @return true (breaks loop) is option to leave menu is selected
    */
   private boolean viewSalesReportOptionSelection(String input)
   {
      switch(input){
      case "B":
         return true;
      case "T":
         myModel.sortMasterSalesRecord(Sale.HIGHEST_SALE);
         return false;
      case "S":
         myModel.sortMasterSalesRecord(Sale.MOST_RECENT);
         return false;
      case "O":
         myModel.sortMasterSalesRecord(Sale.OLDEST);
         return false;
      case "L":
         myModel.sortMasterSalesRecord(Sale.LOWEST_SALE);
         return false;
      case "I":
         myModel.sortMasterSalesRecord(Sale.LOWEST_SALE_ID);
         return false;
      case "Q":
         enterQuitConfirmation();
         return false;
      default:
         myView.displayInvalidEntryPrompt();
         return false;
      }
   }

   /**
    * Calls methods corresponding to option entries in the Employee Sales
    * History menu.
    * @param input String of user input
    * @param employeeID target employee whose salesHistory should be displayed
    * @return true (breaks loop) is option to leave menu is selected
    */
   private boolean employeeSalesHistoryOptionSelection(
         String input, int employeeID)
   {
      switch(input){
      case "B":
         return true;
      case "T":
         myModel.sortStaffSalesArray(employeeID, Sale.HIGHEST_SALE);
         return false;
      case "S":
         myModel.sortStaffSalesArray(employeeID, Sale.MOST_RECENT);
         return false;
      case "O":
         myModel.sortStaffSalesArray(employeeID, Sale.OLDEST);
         return false;
      case "L":
         myModel.sortStaffSalesArray(employeeID, Sale.LOWEST_SALE);
         return false;
      case "A":
         addSale(employeeID);
         return false;
      case "R":
         removeSale(employeeID);
         return false;
      case "Q":
         enterQuitConfirmation();
         return false;
      default:
         myView.displayInvalidEntryPrompt();
         return false;
      }
   }

   /**
    * Calls methods to prompt user for input, confirming whether they wish
    * to remove a sale entry.
    * @return true if "Y", false if "N"
    */
   private boolean confirmRemoveSale()
   {
      String confirmation = "";
      
      //loop is active until valid input is selected
      while(true)
      {
         //asks user if they want to remove a sale and prompts for input
         myView.displayConfirmRemoveSale();
         confirmation = getFormattedUserInput();
         
         //displays error if invalid input, return true if valid, false if not
         if(!yesNoInputValidation(confirmation)) {
            myView.displayInvalidEntryPrompt();
         }
         else if(confirmation.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(confirmation.equalsIgnoreCase("N")) {
            return false;
         }
      }
   }

   /**
    * Asks user to confirm whether they want to remove a sale associated with
    * a target employee and either removes it or exits method.
    * @param employeeID target employee whose sale should be removed
    * @return false if sale cannot be removed, sale does not exist or user
    * decides not to remove sale
    */
   public boolean removeSale(int employeeID)
   {
      //returns false if user decides not to remove a sale
      if(!confirmRemoveSale()) {
         return false;
      }

      String saleID = "";

      //asks user to input sale ID of sale to be removed
      myView.displayRemoveSaleRequest();
      saleID = getFormattedUserInput();

      //removes sale if it exists and user confirms that it is correct
      if(myModel.saleExists(saleID)
            && confirmRemoveSaleID(saleID)) {
         
         //returns true if sale is removed and false if not
         return myModel.removeSale(saleID);
      }
      else {
         
         //if sale does not exist or user decides not to remove, display error
         myView.displayRemoveSaleIDError();
         return false;
      }

   }

   /**
    * Asks user to confirm whether they want to remove the sale associated
    * with displayed sale ID.
    * @param saleID identifier of sale to be removed
    * @return true if "Y", false if "N"
    */
   private boolean confirmRemoveSaleID(String saleID)
   {
      String confirmation = "";
      
      //loop that controls confirmation request
      while(true)
      {
         //asks user if they want to remove sale with displayed saleID
         myView.displayConfirmRemoveSaleID(saleID);
         confirmation = getFormattedUserInput();
         
         //return true if "Y", false if "N", displays error if invalid input
         if(!yesNoInputValidation(confirmation)) {
            myView.displayInvalidEntryPrompt();
         }
         else if(confirmation.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(confirmation.equalsIgnoreCase("N")) {
            return false;
         }
      }
   }
   
   /**
    * Asks user to confirm whether a sale should be added.
    * @return true if "Y", false if "N"
    */
   private boolean confirmAddSale()
   {
      String confirmation = "";
      
      // main loop that controls confirmation request
      while(true)
      {
         //asks user to confirm if they want to add a sale
         myView.displayConfirmAddSale();
         confirmation = getFormattedUserInput();
         
         //return true if "Y", false if "N", displays error if invalid input
         if(!yesNoInputValidation(confirmation)) {
            myView.displayInvalidEntryPrompt();
         }
         else if(confirmation.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(confirmation.equalsIgnoreCase("N")) {
            return false;
         }
      }
   }

   /**
    * Asks user to input date of sale to be added.
    * @return String of date
    */
   private String requestSaleDate()
   {
      //prompts user to enter date until valid input
      while(true)
      {
         myView.displaySaleDateRequest();
         input = getFormattedUserInput();
         
         //return date if valid format and user confirms it is correct
         if(saleDateInputValidation(input)) { 
            if(confirmSaleDate(input)) {
               return input;
            }
         }
      }
   }

   /**
    * Asks user to input sales value of sale to be added.
    * @return String of sales value
    */
   private String requestSaleAmount()
   {
      //prompts user to enter sales amount until valid
      while(true) {
         myView.displaySaleAmountRequest();
         input = getFormattedUserInput();
         
         //return sales amount if valid format and user confirms it is correct
         if(requestSaleAmountInputValidation(input)) {
            if(confirmSaleAmount(input)) {
               return input;
            }
         }
      }
   }

   /**
    * Input validation for sales amount.
    * @param input String of user input
    * @return true if valid and false if not
    */
   private boolean requestSaleAmountInputValidation(String input)
   {
      //returns true if input can be parsed as integer
      try {
         Integer.parseInt(input.trim());
         return true;
      }
      //displays error message and returns false if input cannot be parsed
      catch (Exception e) {
         myView.displaySaleAmountError();
         return false;
      }
   }

   /**
    * Asks user to confirm with sales amount is correct.
    * @param input String of user input
    * @return true if "Y", false if "N"
    */
   private boolean confirmSaleAmount(String input)
   {
      String confirmation = "";
      
      //asks user to confirm until valid input is passed
      while(true) {
         myView.displayConfirmSaleAmount(input);
         confirmation = getFormattedUserInput();
         
         //returns true if "Y", false if "N", displays error if input invalid
         if(!yesNoInputValidation(confirmation)) {
            myView.displayInvalidEntryPrompt();
         }
         else if(confirmation.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(confirmation.equalsIgnoreCase("N")) {
            return false;
         }
      }
   }

   /**
    * Asks user to confirm if sale date is correct.
    * @param input String of user's input
    * @return true if "Y", false if "N"
    */
   private boolean confirmSaleDate(String input)
   {
      String confirmation = "";
      
      //asks user to confirm until valid input is passed
      while(true)
      {
         myView.displayConfirmSaleDate(input);
         confirmation = getFormattedUserInput();
         
         //returns true if "Y", false if "N", displays error if input invalid
         if(!yesNoInputValidation(confirmation)) {
            myView.displayInvalidEntryPrompt();
         }
         else if(confirmation.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(confirmation.equalsIgnoreCase("N")) {
            return false;
         }
      }
   }

   /**
    * Input validation for sale date string.
    * @param input String of user input
    * @return true if date is valid, false if not
    */
   private boolean saleDateInputValidation(String input)
   {
      //returns true if date can be parsed in localdate format
      try {
         LocalDate.parse(input);
         return true;
      }
      //displays error and returns false if invalid format
      catch(Exception e) {
         myView.displaySaleDateError();
         return false;
      }

   }
   
   /**
    *  Calls methods to confirm, validate input and add sale to target employee
    * @param employeeID target employee to add sale to
    * @return true if sale is added, false otherwise
    */
   public boolean addSale(int employeeID)
   {
      //confirm whether user wants to add sale to employee
      if(!confirmAddSale()) {
         return false;
      }

      //requests and validates input for sales date and amounts
      String date = requestSaleDate();
      String amount = requestSaleAmount(); 
      
      //adds sale to target employee and returns true if successful
      return myModel.addSale(employeeID, 
            new Sale(date, Integer.parseInt(amount)));
   }

   /**
    * Confirms if employee exists in staff array
    * @param employeeID target employee to be confirmed
    * @return true if exists, false otherwise
    */
   private boolean confirmEmployeeID(String employeeID)
   {
      //returns false if input is not valid
      if(!employeeIDInputValidation(employeeID)) {
         return false;
      }

      int id = Integer.parseInt(input);
      int index = myModel.employeeExists(id);
      
      //returns false if employee does not exist
      if(index == Model.EMPLOYEE_DOES_NOT_EXIST) {
         myView.displayEmployeeIDNotFound();
         return false;
      }
      return true;
   }

   /**
    * Calls method based on user selection in Manage Employees Menu
    * @param input String of user input
    * @return true (breaks loop) if user selects to leave menu
    */
   private boolean manageEmployeesOptionSelection(String input)
   {
      switch(input){
      case "B":
         return true;
      case "F":
         myModel.sortStaff(Employee.FIRST_NAME);
         return false;
      case "L":
         myModel.sortStaff(Employee.LAST_NAME);
         return false;
      case "E":
         myModel.sortStaff(Employee.ID);
         return false;
      case "A":
         addEmployeeMenuOption();
         return false;
      case "R":
         removeEmployeeMenuOption();
         return false;
      case "Q":
         enterQuitConfirmation();
         return false;
      default:
         myView.displayInvalidEntryPrompt();
         return false;
      }
   }

   /**
    * Confirms whether user wishes to remove employee and then removes
    * @return true if employee can be removed, false if otherwise
    */
   private boolean removeEmployeeMenuOption()
   {
      //returns false if user decides not to remove employee
      if(!removeEmployeeConfirmation()) {
         return false;
      }
      
      //attempts to remove employee and returns false if unsuccessful
      if(!removeEmployee()) {
         myView.displayRemoveEmployeeError();
         return false;
      }
      else {
         return true; //if successful, returns true
      }
   }

   /**
    * Ask user to confirm whether user wants to remove employee
    * @return true if "Y", false if "N"
    */
   private boolean removeEmployeeConfirmation()
   {
      //prompts user for input until "Y" or "N" is entered
      while(true) {
         myView.displayRemoveEmployeeConfirmation();
         input = getFormattedUserInput();

         if(input.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("Q")) {
            return false;
         }
         else {
            myView.displayInvalidEntryPrompt();
         }
      }
   }

   /**
    * Asks user to confirm if they want to remove an employee  
    * @param name String firstName & lastName of employee to remove
    * @param id int employeeID of employee to be removed
    * @return true if Y, false if N
    */
   private boolean confirmRemoveSelectedEmployee(String name, int id)
   {
      //prompts user for input until valid
      while(true) {
         //asks user to confirm whether name and id are correct
         myView.displayConfirmRemoveSelectedEmployee(name, id);
         input = getFormattedUserInput();

         if(input.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("Q")) {
            return false;
         }
         else {
            myView.displayInvalidEntryPrompt();
         }
      }
   }

   /**
    * Removes employee after prompting user for ID
    * @return true if employee is removed, false otherwise
    */
   private boolean removeEmployee()
   {
      //requests user for ID until valid input provided
      do {
         myView.displayEmployeeIDRequest();
         input = getFormattedUserInput();
      } while(!employeeIDInputValidation(input));

      int id = Integer.parseInt(input);
      int index = myModel.employeeExists(id);

      //returns false if employee does not exist
      if(index == Model.EMPLOYEE_DOES_NOT_EXIST) {
         myView.displayEmployeeIDNotFound();
         return false;
      }

      //asks user to confirm if employee is correct and then removes if so
      if(confirmRemoveSelectedEmployee(myModel.getEmployeeName(index), id)){
         return myModel.removeEmployee(id);
      }
      return false;
   }

   /**
    * Validates input for employee ID.
    * @param input String of user input
    * @return true if valid, false if invalid
    */
   private boolean employeeIDInputValidation(String input)
   {
      //returns true if input can be parsed, false if not
      try {
         Integer.parseInt(input);
         return true;
      }
      catch(NumberFormatException e) {
         myView.displayEmployeeIDInputError();
         return false;
      }
   }

   /**
    * Attempts to add employee to model's staff array.
    * @return true if added, false if not
    */
   private boolean addEmployeeMenuOption()
   {
      //returns fals if user decides not to add an employee
      if(!addEmployeeConfirmation()) {
         return false;
      }

      //attempts to add an employee and returns false if unsuccessful
      if(!addEmployee()) {
         myView.displayAddEmployeeError();
         return false;
      }
      else {
         return true;
      }
   }
   
   /**
    * Adds employee to myModel staff[] after validating and confirming names
    * @return true if successful, false if not
    */
   private boolean addEmployee()
   {
      String firstName = "";
      String lastName = "";

      //loop continues until user quits or provides valid input
      do {
         //prompts user for first/last names and performs input validations
         firstName = processEmployeeName(enterFirstName());
         lastName = processEmployeeName(enterLastName());
      } while (!nameConfirmed(firstName, lastName));

      return myModel.addEmployee(new Employee(firstName, lastName));
   }

   /**
    * Asks user to confirm if name is correct
    * @param first firstName to be confirmed
    * @param last lastName to be confirmed
    * @return true if Y, false if N
    */
   private boolean nameConfirmed(String first, String last)
   {
      while(true)
      {
         //asks user to confirm if name is correct
         myView.displayAddEmployeeNameConfirmation(first, last);
         input = getFormattedUserInput();

         if(input.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("Q")) {
            return false;
         }
         else {
            myView.displayInvalidEntryPrompt();
         }
      }
   }

   /**
    * Capitalizes first letter of name and turns rest of string to lowercase
    * @param input name to be processed
    * @return String processed into name
    */
   private String processEmployeeName(String input)
   {
      String name = input.substring(0, 1).toUpperCase() 
            + input.substring(1).toLowerCase();
      return name;
   }

   /**
    * Prompts user to input first name and returns String if valid
    * @return String if passes validation
    */
   private String enterFirstName()
   {
      //loop prompts user for name until it passes validation
      while(true) {
         myView.displayEmployeeFirstNameRequest();
         input = getFormattedUserInput();
         if(!employeeNameInputValidation(input)) {
            myView.displayEmployeeNameError();
         }
         else {
            return input;
         }
      }

   }
   
   /**
    * Prompts user to enter last name and returns String if valid
    * @return String if passes validation
    */
   private String enterLastName()
   {
      while(true) {
         myView.displayEmployeeLastNameRequest();
         input = getFormattedUserInput();
         if(!employeeNameInputValidation(input)) {
            myView.displayEmployeeNameError();
         }
         else {
            return input;
         }
      }
   }

   /**
    * Checks to see if name is valid
    * @param name to be checked
    * @return true if name is valid, false if not
    */
   private boolean employeeNameInputValidation(String name)
   {
      //returns false if there are digits or symbols in name
      return name.matches("^[a-zA-Z]+$");
   }

   /**
    * Asks user to confirm whether they wish to add an employee.
    * @return true if Y, false if N
    */
   private boolean addEmployeeConfirmation()
   {
      //prompts user to confirm whether they wish to add until valid input
      while(true) {
         myView.displayAddEmployeeConfirmation();
         input = getFormattedUserInput();

         if(input.equalsIgnoreCase("Y")) {
            return true;
         }
         else if(input.equalsIgnoreCase("N") || input.equalsIgnoreCase("Q")) {
            return false;
         }
         else {
            myView.displayInvalidEntryPrompt();
         }
      }
   }

   /**
    * Asks user to confirm whether they want to quit the program.
    */
   private void enterQuitConfirmation()
   {
      //asks user if they want to quit until valid input is passed
      do {
         myView.displayQuitConfirmation();
         input = getFormattedUserInput();
         
         //if user confirms that they want to quit, will exit main loop
         if(yesNoInputValidation(input)) {
            quitMenuOptionSelection(input);
            return;
         }
         else {
            myView.displayInvalidEntryPrompt();
         }
      } while(!yesNoInputValidation(input));
   }

   /**
    * Exits program by setting boolean quitConfirmed to true if "Y" is passed
    * as input.
    * @param input to be compared to "Y"
    */
   private void quitMenuOptionSelection(String input)
   {
      if(input.equalsIgnoreCase("Y")) {
         myView.displayQuitMessage();
         quitConfirmed = true;
      }
      else {
         return;
      }
   }

   /**
    * Validates if input is "Y" or "N" regardless of case
    * @param input to be validated
    * @return true if Y, false if N
    */
   private boolean yesNoInputValidation(String input)
   {
      return(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"));
   }

   /**
    * Returns formatted employee list for Manage Employees menu.
    * @return String of formatted employees
    */
   public String getFormattedEmployeeList()
   {
      String employeeList = "";
      Employee[] temp = myModel.getStaff();
      
      //formats first, last names and employee ID for all elements of staff[]
      for(int i = 0; i < myModel.getStaffNumber(); i++) {
         employeeList = employeeList +
               String.format("%-20s %-20s %-5s%n", 
                     temp[i].getFirstName(), 
                     temp[i].getLastName(), 
                     temp[i].getEmployeeID());
      }
      return employeeList;
   }

   /**
    * Requests, trims user input and converts to uppercase letters
    * @return String of formatted input
    */
   private String getFormattedUserInput()
   {
      return userInput.next().trim().toUpperCase();
   }
}
