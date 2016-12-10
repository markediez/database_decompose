/**
 * Author: Mark Emmanuel Diez
 * Date: 09 December 2016
 * CLI for database
 */

 import java.util.Scanner;

 public class Application {
   public static void main(String[] args) {
     // Initialize variables
     Scanner input = new Scanner(System.in);

     // Prompt for a valid relation
     String relation;
     do {
       relation = "";
       System.out.print("Enter relation R separated by a whitespace >>");
       relation = input.nextLine();

     } while (!isValidRelation(relation));
   }

   public static boolean isValidRelation(String relation) {  
     return relation.length() > 0;
   }
 }
