
public class Assig8
{

   public static void main(String[] args)
   {
      //testSale();
      //testSalesRecord();
      //testSort();
      //testSalesRecordSort();
      //testEmployee();
      //modelDemo();

      Controller managerController = new Controller(new Model(), new View());

   }
   public static void modelDemo()
   {
      Model testModel = new Model();

      System.out.println("Testing Add Employees.");

      testModel.addEmployee(new Employee("Ben", "Franklin"));
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.addEmployee(new Employee("Tom", "Edison"));
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.addEmployee(new Employee("John", "Johnson"));
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      System.out.println("Testing Remove Employees.");
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.removeEmployee(100);
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.addEmployee(new Employee("Ed", "Edison"));
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.addEmployee(new Employee("Jack", "Johnson"));
      System.out.printf("%s Employees: %n", testModel.getStaffNumber());
      System.out.println(testModel);

      testModel.sortStaff(Employee.ID);
      System.out.println(testModel);

      Employee emp1 = new Employee("James", "Jameson");
      Employee emp2 = new Employee("John", "Jackson");

      Sale testSale1 = new Sale(2020, 1, 1, 5000);
      Sale testSale2 = new Sale(2020, 2, 2, 1000);
      Sale testSale3 = new Sale(2020, 10, 10, 500);
      Sale testSale4 = new Sale(2020, 10, 1, 17000);
      Sale testSale5 = new Sale(2020, 3, 3, 800);

      emp1.addSale(testSale1);
      emp1.addSale(testSale2);
      emp2.addSale(testSale3);
      emp2.addSale(testSale4);
      emp2.addSale(testSale5);

      testModel.addEmployee(emp1);
      testModel.addEmployee(emp2);

      System.out.println("Adding new employees with sales records.");
      System.out.println(testModel);

      System.out.println("Refresh sales record and display");
      testModel.refreshMasterSalesRecord();
      System.out.println(testModel.getMasterSalesRecord());

      System.out.println("Testing remove employee and printing master record.");
      testModel.removeEmployee(104);

      System.out.println(testModel.getMasterSalesRecord());
      System.out.println(testModel);

      testModel.addSale(100, new Sale(2019, 5, 20, 100));
      testModel.addSale(100, new Sale(2020, 1, 2, 1500));
      testModel.addSale(101, new Sale(2020, 8, 17, 3500));
      testModel.addSale(102, new Sale(2020, 12, 10, 500));
      testModel.addSale(102, new Sale(2020, 11, 5, 7500));

      System.out.println(testModel);
      System.out.println(testModel.getMasterSalesRecord());
      testModel.sortMasterSalesRecord(Sale.OLDEST);
      System.out.println(testModel.getMasterSalesRecord());

      testModel.removeSale("1021");
      testModel.removeSale("1000");
      System.out.println(testModel);
      System.out.println(testModel.getMasterSalesRecord());


   }
   public static void testEmployee()
   {
      Sale testSale1 = new Sale(2020, 1, 1, 5000, "0");
      Sale testSale2 = new Sale(2020, 2, 2, 1000, "1");
      Sale testSale3 = new Sale(2020, 10, 10, 500, "2");
      Sale testSale4 = new Sale(2020, 10, 1, 17000, "3");
      Sale testSale5 = new Sale(2020, 3, 3, 800, "4");

      SalesRecord test1 = new SalesRecord();
      test1.addSale(testSale1);
      test1.addSale(testSale2);
      test1.addSale(testSale3);
      test1.addSale(testSale4);
      test1.addSale(testSale5);

      Employee testEmployee = new Employee("First", "Last", 100, test1);
      System.out.println(testEmployee);

      testEmployee.addSale(new Sale(2020, 12, 15, 30000));
      System.out.println(testEmployee);

      testEmployee.removeSale("1000");
      testEmployee.removeSale("1002");
      testEmployee.removeSale("1004");

      System.out.println(testEmployee);

   }

   public static void testSalesRecordSort()
   {
      Sale testSale1 = new Sale(2020, 1, 1, 5000, "0");
      Sale testSale2 = new Sale(2020, 2, 2, 1000, "1");
      Sale testSale3 = new Sale(2020, 10, 10, 500, "2");
      Sale testSale4 = new Sale(2020, 10, 1, 17000, "3");
      Sale testSale5 = new Sale(2020, 3, 3, 800, "4");

      SalesRecord test1 = new SalesRecord();
      test1.addSale(testSale1);
      test1.addSale(testSale2);
      test1.addSale(testSale3);
      test1.addSale(testSale4);
      test1.addSale(testSale5);

      System.out.println("Orignal Array: \n" + test1);
      test1.sortArray(Sale.HIGHEST_SALE);
      System.out.println("Highest Sale: \n" + test1);
      test1.sortArray(Sale.MOST_RECENT);
      System.out.println("Most Recent: \n" + test1);
      test1.sortArray(Sale.OLDEST);
      System.out.println("Oldest: \n" + test1);
      test1.sortArray(Sale.LOWEST_SALE);
      System.out.println("Lowest Sale: \n" + test1);

   }

   public static void testSort()
   {
      Sale testSale1 = new Sale(2020, 1, 1, 5000, "0");
      Sale testSale2 = new Sale(2020, 2, 2, 1000, "1");
      Sale testSale3 = new Sale(2020, 10, 10, 500, "2");
      Sale testSale4 = new Sale(2020, 10, 1, 17000, "3");
      Sale testSale5 = new Sale(2020, 3, 3, 800, "4");

      SalesRecord test1 = new SalesRecord();
      test1.addSale(testSale1);
      test1.addSale(testSale2);
      test1.addSale(testSale3);
      test1.addSale(testSale4);
      test1.addSale(testSale5);

      Sale[] testArray = test1.getSalesList();
      Sale.sortSalesArray(testArray, test1.getNumberOfSales(), Sale.MOST_RECENT);
      test1.setSalesList(testArray);
      System.out.println(test1);

      testArray = test1.getSalesList();
      Sale.sortSalesArray(testArray, test1.getNumberOfSales(), Sale.HIGHEST_SALE);
      test1.setSalesList(testArray);
      System.out.println(test1);

      testArray = test1.getSalesList();
      Sale.sortSalesArray(testArray, test1.getNumberOfSales(), Sale.OLDEST);
      test1.setSalesList(testArray);
      System.out.println(test1);
   }

   public static void testSalesRecord()
   {
      Sale testSale1 = new Sale(2020, 1, 1, 5000, "0");
      Sale testSale2 = new Sale(2020, 2, 2, 1000, "1");
      Sale testSale3 = new Sale(2020, 10, 10, 500, "2");
      Sale testSale4 = new Sale(2020, 10, 1, 17000, "3");
      Sale testSale5 = new Sale(2020, 3, 3, 800, "4");

      SalesRecord test1 = new SalesRecord();
      test1.addSale(testSale1);
      test1.addSale(testSale2);
      test1.addSale(testSale3);
      test1.addSale(testSale4);
      test1.addSale(testSale5);

      SalesRecord test2 = new SalesRecord(test1);

      System.out.println(test1);
      test1.removeSale(2);
      System.out.println(test1);
      test1.removeSale(4);
      System.out.println(test1);
      test1.removeSale(1);
      test1.removeSale(2);
      System.out.println(test1);
      test1.removeSale(0);
      System.out.println(test1);
      System.out.println(test2);
      // SalesRecord.sortByMostRecent(test2);
      System.out.println(test2);
      test2.removeSale(test2.findSaleIndex("2"));
      System.out.println(test2);
      test2.removeSale(test2.findSaleIndex("0"));
      test2.removeSale(test2.findSaleIndex("4"));
      test2.removeSale(test2.findSaleIndex("3"));
      System.out.println(test2);
      test2.removeSale(test2.findSaleIndex("3"));
      System.out.println(test2);
      test2.removeSale(test2.findSaleIndex("1"));
      System.out.println(test2 + " " + "Empty");
   }

   public static void testSale()
   {
      Sale test = new Sale(2020, 2, 200, 1000, "NONE");
      System.out.println(test);
      Sale salesTest2 = new Sale(test);
      System.out.println(salesTest2);
      System.out.println(salesTest2.setYear(1));
   }

}
