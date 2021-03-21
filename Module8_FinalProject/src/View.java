
public class View
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
