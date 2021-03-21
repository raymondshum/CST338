/*******************************************************************************
 * Raymond Shum
 * Oct 3, 2020
 * CST 338
 * Assignment 1
 * 
 * This program consists of two parts. In Part 1, user input in the form of
 * their full name is concatenated, printed along with the length and printed in
 * both upper and lower case. In Part 2, two static variables are displayed,
 * showing the minimum and maximum ranges of hours that a student is expected to
 * study in a given week. The student is prompted to enter their own hours
 * studied, which is displayed in a rounded form.
 ******************************************************************************/

import java.util.Scanner;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Assign1
{
   public static final int MIN_HOURS = 12;
   public static final int MAX_HOURS = 20;

   public static void main(String[] args)
   {
      Scanner userInput = new Scanner(System.in);
      
      System.out.println("Please enter your first and last name " + 
         "(capitalize the first letter of each): ");
      
      //Student's first and last name are declared and initialized from user
      //inputs. Full name is declared and initialized to concatenated values
      String firstName = userInput.next();
      String lastName = userInput.next();
      String fullName = firstName + " " + lastName;
      
      //Full name, length (including white space), capital and lowercase full
      //names are displayed
      System.out.printf("Your name is: %s %s"
         + " (%d characters) %n", firstName, lastName, fullName.length());
      System.out.println("Full Name (Capitalized): " + fullName.toUpperCase());
      System.out.println("Full Name (Lower Case): " + fullName.toLowerCase());
      
      //Display formatted minimum & maximum hours studied per week range
      System.out.printf("%n(Hours spent studying per week)%n" + 
         "Minimum: %d%nMaximum: %d%n", MIN_HOURS, MAX_HOURS);
      System.out.println("Please enter number hours spent studying this week "
          + "(3 Decimal Places): ");
      
      //hoursStudied is initialized to user input of 3 decimal places and then
      //rounded to 1 decimal place. Trailing 0 is always displayed
      double hoursStudied = userInput.nextDouble();
      DecimalFormat hoursStudiedRound = new DecimalFormat("##.0");
      hoursStudiedRound.setRoundingMode(RoundingMode.HALF_UP);
      
      System.out.println("Your (rounded) hours spent studying this week: "
         + hoursStudiedRound.format(hoursStudied));
      
      userInput.close();
   }

}
/************************************OUTPUT*************************************

RUN 1
-----
Please enter your first and last name (capitalize the first letter of each): 
Raymond Shum
Your name is: Raymond Shum (12 characters) 
Full Name (Capitalized): RAYMOND SHUM
Full Name (Lower Case): raymond shum

(Hours spent studying per week)
Minimum: 12
Maximum: 20
Please enter number hours spent studying this week (3 Decimal Places): 
3.555
Your (rounded) hours spent studying this week: 3.6
-----

RUN 2
-----
Please enter your first and last name (capitalize the first letter of each): 
Ben
Gary
Your name is: Ben Gary (8 characters) 
Full Name (Capitalized): BEN GARY
Full Name (Lower Case): ben gary

(Hours spent studying per week)
Minimum: 12
Maximum: 20
Please enter number hours spent studying this week (3 Decimal Places): 
12
Your (rounded) hours spent studying this week: 12.0
-----

RUN 3
-----
Please enter your first and last name (capitalize the first letter of each): 
John Johnson
Your name is: John Johnson (12 characters) 
Full Name (Capitalized): JOHN JOHNSON
Full Name (Lower Case): john johnson

(Hours spent studying per week)
Minimum: 12
Maximum: 20
Please enter number hours spent studying this week (3 Decimal Places): 
12.406
Your (rounded) hours spent studying this week: 12.4

-----
*******************************************************************************/
