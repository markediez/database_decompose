/**
 * Author: Mark Emmanuel Diez
 * Date: 09 December 2016
 * CLI for database
 */

 import java.util.Scanner;
 import java.util.ArrayList;
 import java.util.Arrays;

 public class Application {
   public static void main(String[] args) {
     // Initialize variables
     Scanner input = new Scanner(System.in);
     ArrayList<String> relation = new ArrayList<>();

     // Prompt for a valid relation
     String sRelation;
     do {
       sRelation = "";

       System.out.print("Enter relation R separated by a whitespace >>");
       sRelation = input.nextLine();
     } while (!isValidRelation(sRelation));

     // Store and output relation
     System.out.println("Relation saved.");
     relation = createRelation(sRelation);
     showRelation(relation);

     // Prompt for valid set of functional dependencies
     String functionalDependency;
     do {
       showCurrentFD();
       functionalDependency = "";

       System.out.println("Enter a single functional dependency 'A B -> C D' >>");
       functionalDependency = input.nextLine();

       // Exit loop on -1 flag
       if (functionalDependency.equalsIgnoreCase("-1")) {
         break;
       }

     } while (isValidFunctionalDependency(functionalDependency));
   }

   public static void showRelation(ArrayList<String> relation) {
     System.out.print("R(");
     for (String attribute : relation) {
       System.out.print(attribute + ",");
     }
     System.out.print(")");
   }

   public static void showCurrentFD() {

   }

   /**
    * Returns an ArrayList containing the attribute names
    *
    * @param  relationString [description]
    * @return                [description]
    */
   public static ArrayList<String> createRelation(String relationString) {
     return new ArrayList<String>(Arrays.asList(relationString.split("\\s+")));
   }

   /**
    * Return true if the string is a valid relation
    * Current valid format: "some_col another_col A neXt"
    * Separate function in case a new format is desired
    * @param relation - Attributes of the relation separated by whitespace
    * @return true / false
    */
   public static boolean isValidFunctionalDependency(String functionalDependency) {
     boolean valid = false;
     // Separate by ->
     // if size != 2 then the format is wrong

     // [0] = LHS , [1] = RHS, for both sides, ensure that the
     return valid;
   }

   /**
    * Return true if the string is a valid relation
    * Current valid format: "some_col another_col A neXt"
    * Separate function in case a new format is desired
    * @param relation - Attributes of the relation separated by whitespace
    * @return true / false
    */
   public static boolean isValidRelation(String relation) {
     return relation.length() > 0;
   }
 }
