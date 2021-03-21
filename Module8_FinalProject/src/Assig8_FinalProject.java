/* *****************************************************************************
 * Name: Raymond Shum
 * Assignment: Final Project
 * Due Date: Dec. 19, 2020
 * 
 * Description:
 * SimpleERP is an application built on the Model, View, Controller design
 * pattern. The model is built on 3 classes: Sales, SalesRecord, and Employee.
 * Controller manipulates model and calls methods from View to visualize its
 * current state. The application holds 3 modules, which allows a single user 
 * to manage employees, their associated sales and to view a comprehensive 
 * sales report.
 ******************************************************************************/

import java.time.*;
import java.util.Scanner;
public class Assig8_FinalProject
{
   public static void main(String[] args)
   {
      Controller managerController = new Controller(new Model(), new View());
   }
}

/**
 * The "SimpleERP" Controller manipulates Model through accessors and mutators.
 * It calls methods from View to provide a representation of Model.
 * @author Raymond Shum
 *
 */
class Controller
{
   private Model myModel;
   private View myView;
   private Scanner userInput;
   private String input;
   private boolean quitConfirmed;

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

class View
{
   /**
    * Default constructor, empty due to no instance variables to initialize
    */
   View()
   {
   }

   /**
    * Displays main menu prompt.
    */
   public void displayMainMenu()
   {
      System.out.println("\n"
            + "Main Menu\n"
            + "Please select your option:\n"
            + "1) Manage Employees\n"
            + "2) Manage Employee Sales\n"
            + "3) View Sales Report");
   }

   /**
    * Displays Manage Employees menu as header, body and options
    * @param employeeList formatted string that composes list of employees
    */
   public void displayManageEmployeesMenu(String employeeList)
   {
      displayManageEmployeesHeader();
      System.out.println(employeeList);
      displayManageEmployeesOptions();
   }

   /**
    * Displays Manage Employee Sales menu as header, body and options
    * @param employeeList formatted string that composes list of employees
    */
   public void displayManageEmployeeSalesMenu(String employeeList)
   {
      displayManageEmployeeSalesHeader();
      System.out.println(employeeList);
      displayManageEmployeeSalesOptions();
   }

   /**
    * Displays Employee Sales History menu as header, body and options
    * @param name 
    * @param salesHistory
    */
   public void displayEmployeeSalesHistoryMenu(String name, String salesHistory)
   {
      displayEmployeeSalesHistoryHeader(name);
      System.out.println(salesHistory);
      displayEmployeeSalesHistoryMenuOptions();
   }

   /**
    * Display's an employee's sales history menu.
    * @param name of employee to be displayed
    */
   public void displayEmployeeSalesHistoryHeader(String name)
   {
      System.out.println("\n==================================\n"
            + name + "'s Sales History\n"
            + "=================================="
            + "\n"
            + String.format("%-20s %-20s %-20s%n", 
                  "Sale ID", "Date of Sale", "Sales Amount"));
   }

   /**
    * Displays options for an employee's sales history menu.
    */
   public void displayEmployeeSalesHistoryMenuOptions()
   {
      System.out.println("(B) Back to View Employees "
            + "(T) Sort by Higest Total Sales "
            + "(S) Sort by Most Recent Sales \n"
            + "(O) Sort by Oldest Sales "
            + "(L) Sort by Lowest Total Sales "
            + "(A) Add Sale (R) Remove Sale");
   }

   /**
    * Displays header for Manage Employees Menu
    */
   private void displayManageEmployeesHeader()
   {
      System.out.println("\n================\n"
            + "Manage Employees\n"
            + "================\n"
            + "\n"
            + String.format("%-20s %-20s %-20s%n", 
                  "First Name", "Last Name", "Employee ID"));
   }

   /**
    * Displays header for Manage Employee Sales Menu
    */
   private void displayManageEmployeeSalesHeader()
   {
      System.out.println("\n====================\n"
            + "Mange Employee Sales\n"
            + "====================\n"
            + "\n"
            + String.format("%-20s %-20s %-20s%n", 
                  "First Name", "Last Name", "Employee ID"));
   }


   /**
    * Displays Sales Report Menu in three parts: header, body, options
    * @param report
    */
   public void displayViewSalesReport(String report)
   {
      displayViewSalesReportHeader();
      System.out.println(report);
      displayViewSalesReportOptions();
   }

   /**
    * Displays header for Sales Report Menu
    */
   private void displayViewSalesReportHeader()
   {
      System.out.println("\n===================\n"
            + "Master Sales Record\n"
            + "==================="
            + "\n"
            + String.format("%-20s %-20s %-20s%n", 
                  "Sale ID", "Date of Sale", "Sales Amount"));
   }

   /**
    * Displays options for Sales Report Menu
    */
   private void displayViewSalesReportOptions()
   {
      System.out.println("(B) Back to View Employees "
            + "(T) Sort by Higest Total Sales "
            + "(S) Sort by Most Recent Sales \n"
            + "(O) Sort by Oldest Sales "
            + "(L) Sort by Lowest Total Sales "
            + "(I) Sort by Sale ID");
   }

   /**
    * Displays options for Manage Employee Sales menu
    */
   private void displayManageEmployeeSalesOptions()
   {
      System.out.println("(B) Back to Main Menu (F) Sort by First Name" 
            + "(L)Sort by Last Name (E)Sort by ID\n"
            + "(V) View Employee Sales Record");
   }

   /**
    * Displays options for Manage Employees menu
    */
   private void displayManageEmployeesOptions()
   {
      System.out.println("(B) Back to Main Menu (F) Sort by First Name" 
            + " (L)Sort by Last Name (E)Sort by ID\n"
            + "(A) Add Employee (R) Remove Employee");
   }

   /**
    * Displays confirmation prompt for Remove Sale option
    */
   public void displayConfirmRemoveSale()
   {
      System.out.println("Do you wish to remove a sale entry? (Y/N)");
   }

   /**
    * Displays request for sale ID to be removed
    */
   public void displayRemoveSaleRequest()
   {
      System.out.println("Please enter the sale ID of the sale you wish "
            + "to remove: ");
   }

   /**
    * Displays confirmation prompt for sale ID entered
    * @param saleID String to be confirmed
    */
   public void displayConfirmRemoveSaleID(String saleID)
   {
      System.out.println("Remove sale number " + saleID + "? (Y/N)");
   }

   /**
    * Displays error when sale could not be found
    */
   public void displayRemoveSaleIDError()
   {
      System.out.println("No sale is found to be associated with the "
            + "provided sale ID.");
   }

   /**
    * Displays confirmation prompt for Add Sale option
    */
   public void displayConfirmAddSale()
   {
      System.out.println("Do you wish to add a sale entry? (Y/N)");
   }

   /**
    * Displays request for entry of Sale Date and format
    */
   public void displaySaleDateRequest()
   {
      System.out.println("Please enter the date of sale in the form of"
            + " YYYY-MM-DD");
   }

   /**
    * Error that is displayed if sale date does not pass input validation
    */
   public void displaySaleDateError()
   {
      System.out.println("You have entered an invalid date. Please try again.");
   }

   /**
    * Displays confirmation request for provided date
    * @param date to be confirmed by user
    */
   public void displayConfirmSaleDate(String date)
   {
      System.out.println("You have entered " + date 
            + ". Is this correct? (Y/N)" );
   }

   /**
    * Displays request for user to input sale amount
    */
   public void displaySaleAmountRequest()
   {
      System.out.println("Please enter the sale amount as a whole number: ");
   }

   /**
    * Error to be displayed if sale amount does not pass input validation
    */
   public void displaySaleAmountError()
   {
      System.out.println("Sale amount must be a whole number and only contain"
            + " digits. Please try again.");
   }

   /**
    * Displays request for user to confirm whether sale amount is correct
    * @param amount to be confirmed by user
    */
   public void displayConfirmSaleAmount(String amount)
   {
      System.out.println("You have entered " + amount 
            + ". Is this correct? (Y/N)");
   }

   /**
    * Displays request for user to input first name of employee
    */
   public void displayEmployeeFirstNameRequest()
   {
      System.out.println("Please enter the employee's first name: ");
   }

   /**
    * Displays request for user to input last name of employee
    */
   public void displayEmployeeLastNameRequest()
   {
      System.out.println("Please enter the employee's last name: ");
   }

   /**
    * Displays request for user to enter ID of employee
    */
   public void displayEmployeeIDRequest()
   {
      System.out.println("Please enter employee ID: ");
   }

   /**
    * Displays error if employee could not be added
    */
   public void displayAddEmployeeError()
   {
      System.out.println("Employee could not be added. Please contact your" +
            " administrator");
   }

   /**
    * Displays error for if employee could not be removed
    */
   public void displayRemoveEmployeeError()
   {
      System.out.println("Employee could not be removed. Please contact your" +
            " administrator");
   }

   /**
    * Displays request for confirmation that employee and ID are correct
    * @param employee name to be confirmed
    * @param id of employee to be confirmed
    */
   public void displayConfirmRemoveSelectedEmployee(String employee, int id)
   {
      System.out.println("Remove " + employee + " (" + id + ")? (Y/N)");
   }

   /**
    * Displays error message for names that do not pass validation
    */
   public void displayEmployeeNameError()
   {
      System.out.println("First/Last names should not contain digits.");
   }

   /**
    * Displays request for confirmation for removal of employee
    */
   public void displayRemoveEmployeeConfirmation()
   {
      System.out.println("Are you sure you want to remove an employee? (Y/N)");
   }

   /**
    * Displays error message for if no associated employee found matching ID
    */
   public void displayEmployeeIDNotFound()
   {
      System.out.println("No employees match the provided employee ID.");
   }

   /**
    * Error message to be displayed if ID does not pass validation
    */
   public void displayEmployeeIDInputError()
   {
      System.out.println("Employee ID must only contain digits and no spaces.");
   }

   /**
    * Displays confirmation request for adding an employee
    */
   public void displayAddEmployeeConfirmation()
   {
      System.out.println("Are you sure you want to add a new employee? (Y/N)");
   }

   /**
    * Displays confirmation request for employee's first and last name
    * @param first name to be confirmed
    * @param last name to be confirmed
    */
   public void displayAddEmployeeNameConfirmation(String first, String last)
   {
      System.out.println("You have entered: " + first + " " + last + "." +
            " Is this correct? (Y/N)");
   }

   /**
    * Displays option entry prompt for all menus
    */
   public void displayOptionEntryPrompt()
   {
      System.out.printf("Enter your selection (\"Q\" to quit): ");
   }

   /**
    * Error message to be displayed if entry is invalid 
    */
   public void displayInvalidEntryPrompt()
   {
      System.out.printf("Invalid entry. Please select one of the" + 
            " displayed options.\n");
   }

   /**
    * Displays request for user to confirm whether they wish to quit
    */
   public void displayQuitConfirmation()
   {
      System.out.println("Are you sure you want to quit? (Y/N)");
   }

   public void displayQuitMessage()
   {
      System.out.println("You have confirmed that you wish to quit. Goodbye!");
   }
}

/**
 * The "SimpleERP" Model manages employee and sales information, manipulated
 * through the Controller. It does not communicate with the View.
 * @author Raymond Shum
 *
 */
class Model
{
   //Maximum size of the staff array defined by employee ID block
   public final static int MAX_STAFF 
   = Employee.EMPLOYEE_ID_END - Employee.EMPLOYEE_ID_START;

   //integer value returned when employee index cannot be found
   public static final int EMPLOYEE_DOES_NOT_EXIST = -1;

   private Employee[] staff;
   private SalesRecord masterSalesRecord;
   private int staffNumber;
   private int employeeID;
   private int[] recoveredIDs;
   private int numberOfRecoveredIDs;

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

/**
 * Employee is an object that holds an employee's identifying information and
 * their sales history in the form of a SalesRecord object.
 * @author Raymond Shum
 *
 */
class Employee
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

/**
 * SaleRecord is an object that holds an array of Sale objects and contains
 * a value for number of sales in the array. Its methods are based on ordering
 * and maintenance of the array.
 * @author rshum
 *
 */
class SalesRecord
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

/**
 * Sales object holds the date, ID and amount of a sale. It provides a static
 * method to sort Sales arrays.
 * @author Raymond Shum
 *
 */
class Sale
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

/****************************OUTPUT********************************************

Main Menu
Please select your option:
1) Manage Employees
2) Manage Employee Sales
3) View Sales Report
Enter your selection ("Q" to quit): 1

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): t
Invalid entry. Please select one of the displayed options.

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): f

================
Manage Employees
================

First Name           Last Name            Employee ID         

Ian                  Rowe                 102  
Larry                Chiem                100  
Nicholas             Stankovich           103  
Raymond              Shum                 101  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): e

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): l

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Ian                  Rowe                 102  
Raymond              Shum                 101  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): a
Are you sure you want to add a new employee? (Y/N)
d
Invalid entry. Please select one of the displayed options.
Are you sure you want to add a new employee? (Y/N)
y
Please enter the employee's first name: 
123a
First/Last names should not contain digits.
Please enter the employee's first name: 
Bart
Please enter the employee's last name: 
Simpson
You have entered: Bart Simpson. Is this correct? (Y/N)
p
Invalid entry. Please select one of the displayed options.
You have entered: Bart Simpson. Is this correct? (Y/N)
y

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  
Bart                 Simpson              104  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): r
Are you sure you want to remove an employee? (Y/N)
y
Please enter employee ID: 
104
Remove Bart Simpson (104)? (Y/N)
y

================
Manage Employees
================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name (L)Sort by Last Name (E)Sort by ID
(A) Add Employee (R) Remove Employee
Enter your selection ("Q" to quit): b

Main Menu
Please select your option:
1) Manage Employees
2) Manage Employee Sales
3) View Sales Report
Enter your selection ("Q" to quit): 2

====================
Mange Employee Sales
====================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name(L)Sort by Last Name (E)Sort by ID
(V) View Employee Sales Record
Enter your selection ("Q" to quit): v
Please enter employee ID: 
5
No employees match the provided employee ID.

====================
Mange Employee Sales
====================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name(L)Sort by Last Name (E)Sort by ID
(V) View Employee Sales Record
Enter your selection ("Q" to quit): v
Please enter employee ID: 
Bart Simpson
Employee ID must only contain digits and no spaces.

====================
Mange Employee Sales
====================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name(L)Sort by Last Name (E)Sort by ID
(V) View Employee Sales Record
Enter your selection ("Q" to quit): Invalid entry. Please select one of the displayed options.

====================
Mange Employee Sales
====================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name(L)Sort by Last Name (E)Sort by ID
(V) View Employee Sales Record
Enter your selection ("Q" to quit): v
Please enter employee ID: 
100

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2019-01-20           $1500                
1001                 2020-05-01           $100                 
1002                 2020-07-23           $3000                

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): t

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-07-23           $3000                
1001                 2019-01-20           $1500                
1002                 2020-05-01           $100                 

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): s

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-07-23           $3000                
1001                 2020-05-01           $100                 
1002                 2019-01-20           $1500                

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): o

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2019-01-20           $1500                
1001                 2020-05-01           $100                 
1002                 2020-07-23           $3000                

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): l

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-05-01           $100                 
1001                 2019-01-20           $1500                
1002                 2020-07-23           $3000                

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): a
Do you wish to add a sale entry? (Y/N)
d
Invalid entry. Please select one of the displayed options.
Do you wish to add a sale entry? (Y/N)
y
Please enter the date of sale in the form of YYYY-MM-DD
today
You have entered an invalid date. Please try again.
Please enter the date of sale in the form of YYYY-MM-DD
2020-13-50
You have entered an invalid date. Please try again.
Please enter the date of sale in the form of YYYY-MM-DD
2020-05-10
You have entered 2020-05-10. Is this correct? (Y/N)
y
Please enter the sale amount as a whole number: 
one hundred dollars
Sale amount must be a whole number and only contain digits. Please try again.
Please enter the sale amount as a whole number: 
Sale amount must be a whole number and only contain digits. Please try again.
Please enter the sale amount as a whole number: 
Sale amount must be a whole number and only contain digits. Please try again.
Please enter the sale amount as a whole number: 
100
You have entered 100. Is this correct? (Y/N)
y

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-05-01           $100                 
1001                 2019-01-20           $1500                
1002                 2020-07-23           $3000                
1003                 2020-05-10           $100                 

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): r
Do you wish to remove a sale entry? (Y/N)
y
Please enter the sale ID of the sale you wish to remove: 
1003
Remove sale number 1003? (Y/N)
y

==================================
Larry Chiem's Sales History
==================================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-05-01           $100                 
1001                 2019-01-20           $1500                
1002                 2020-07-23           $3000                

(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (A) Add Sale (R) Remove Sale
Enter your selection ("Q" to quit): b

====================
Mange Employee Sales
====================

First Name           Last Name            Employee ID         

Larry                Chiem                100  
Raymond              Shum                 101  
Ian                  Rowe                 102  
Nicholas             Stankovich           103  

(B) Back to Main Menu (F) Sort by First Name(L)Sort by Last Name (E)Sort by ID
(V) View Employee Sales Record
Enter your selection ("Q" to quit): b

Main Menu
Please select your option:
1) Manage Employees
2) Manage Employee Sales
3) View Sales Report
Enter your selection ("Q" to quit): 3

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-05-01           $100                 
1001                 2019-01-20           $1500                
1002                 2020-07-23           $3000                
1010                 2019-03-05           $90                  
1011                 2020-05-17           $2300                
1012                 2020-10-09           $150                 
1020                 2020-04-02           $500                 
1021                 2020-05-07           $5000                
1022                 2020-07-01           $2500                
1030                 2019-10-04           $3500                
1031                 2020-09-27           $2500                
1032                 2020-12-15           $1500                
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): t

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1021                 2020-05-07           $5000                
1030                 2019-10-04           $3500                
1002                 2020-07-23           $3000                
1022                 2020-07-01           $2500                
1031                 2020-09-27           $2500                
1011                 2020-05-17           $2300                
1001                 2019-01-20           $1500                
1032                 2020-12-15           $1500                
1020                 2020-04-02           $500                 
1012                 2020-10-09           $150                 
1000                 2020-05-01           $100                 
1010                 2019-03-05           $90                  
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): s

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1032                 2020-12-15           $1500                
1012                 2020-10-09           $150                 
1031                 2020-09-27           $2500                
1002                 2020-07-23           $3000                
1022                 2020-07-01           $2500                
1011                 2020-05-17           $2300                
1021                 2020-05-07           $5000                
1000                 2020-05-01           $100                 
1020                 2020-04-02           $500                 
1030                 2019-10-04           $3500                
1010                 2019-03-05           $90                  
1001                 2019-01-20           $1500                
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): o

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1001                 2019-01-20           $1500                
1010                 2019-03-05           $90                  
1030                 2019-10-04           $3500                
1020                 2020-04-02           $500                 
1000                 2020-05-01           $100                 
1021                 2020-05-07           $5000                
1011                 2020-05-17           $2300                
1022                 2020-07-01           $2500                
1002                 2020-07-23           $3000                
1031                 2020-09-27           $2500                
1012                 2020-10-09           $150                 
1032                 2020-12-15           $1500                
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): l

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1010                 2019-03-05           $90                  
1000                 2020-05-01           $100                 
1012                 2020-10-09           $150                 
1020                 2020-04-02           $500                 
1001                 2019-01-20           $1500                
1032                 2020-12-15           $1500                
1011                 2020-05-17           $2300                
1022                 2020-07-01           $2500                
1031                 2020-09-27           $2500                
1002                 2020-07-23           $3000                
1030                 2019-10-04           $3500                
1021                 2020-05-07           $5000                
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): i

===================
Master Sales Record
===================
Sale ID              Date of Sale         Sales Amount        

1000                 2020-05-01           $100                 
1001                 2019-01-20           $1500                
1002                 2020-07-23           $3000                
1010                 2019-03-05           $90                  
1011                 2020-05-17           $2300                
1012                 2020-10-09           $150                 
1020                 2020-04-02           $500                 
1021                 2020-05-07           $5000                
1022                 2020-07-01           $2500                
1030                 2019-10-04           $3500                
1031                 2020-09-27           $2500                
1032                 2020-12-15           $1500                
============================================================
Total Sales:                              $22640               
(B) Back to View Employees (T) Sort by Higest Total Sales (S) Sort by Most Recent Sales 
(O) Sort by Oldest Sales (L) Sort by Lowest Total Sales (I) Sort by Sale ID
Enter your selection ("Q" to quit): q
Are you sure you want to quit? (Y/N)
y
You have confirmed that you wish to quit. Goodbye!

 ******************************************************************************/
