/**
* Author: Mark Emmanuel Diez
* Date: 09 December 2016
* CLI for database
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class Application {
  public static void main(String[] args) {
    // Initialize variables
    Scanner input = new Scanner(System.in);
    ArrayList<String> relation = new ArrayList<>();
    HashMap<String, ArrayList<String>> functionalDependencies = new HashMap<>();

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
    String functionalDependency = "";
    boolean isValidFD = false;
    do {
      if (isValidFD) {
        String[] fd = functionalDependency.split("->");
        String[] LHS = fd[0].trim().split("\\s+");
        ArrayList<String> RHS = new ArrayList<String>(Arrays.asList(fd[1].trim().split("\\s+")));

        String key = "";
        for (String attr : LHS) {
          if(!attr.equalsIgnoreCase(LHS[LHS.length - 1])) {
            attr += ",";
          }

          key += attr;
        }

        functionalDependencies.put(key, RHS);
      }

      showCurrentFD(functionalDependencies);
      System.out.print("Enter a single functional dependency 'A B -> C D' (-1 to end):");
      functionalDependency = input.nextLine();

      if (isValidFunctionalDependency(functionalDependency, relation)) {
        isValidFD = true;
      } else {
        isValidFD = false;
      }

    } while (isValidFD || !functionalDependency.equalsIgnoreCase("-1"));
  }

  public static void showRelation(ArrayList<String> relation) {
    System.out.print("R(");
    for (String attribute : relation) {
      System.out.print(attribute + ",");
    }
    System.out.println(")");
  }

  public static void showCurrentFD(HashMap<String, ArrayList<String>> fd) {
    System.out.println("Current FDs:");
    for (String key : fd.keySet() ) {
      for (String attr : fd.get(key)) {
        System.out.print(key + " -> ");
        System.out.println(attr);
      }
    }
  }

  /**
  * Returns an ArrayList containing the attribute names
  *
  * @param  relationString [description]
  * @return                [description]
  */
  public static ArrayList<String> createRelation(String relationString) {
    return new ArrayList<String>(Arrays.asList(relationString.trim().split("\\s+")));
  }

  /**
  * Return true if the string is a valid relation
  * Current valid format: "some_col another_col A neXt"
  * Separate function in case a new format is desired
  * @param relation - Attributes of the relation separated by whitespace
  * @return true / false
  */
  public static boolean isValidFunctionalDependency(String functionalDependency, ArrayList<String> relation) {
    boolean valid = true;
    // Separate by ->
    String[] fd = functionalDependency.split("->");

    // if size != 2 then the format is wrong
    if (fd.length != 2) {
      System.out.println("Please follow the format 'A B C -> D E F'");
      valid = false;
    } else {
      // Make sure each attribute used in the FD exists in the relation
      for (String side : fd) {
        System.out.println(side);
        for(String attribute : side.trim().split("\\s+")) {
          System.out.println(attribute);
          if (!relation.contains(attribute)) {
            System.out.println(attribute + " does not exist in the relation R");
            valid = false;
            break;
          }
        }
        if (!valid) {
          break;
        }
      }
    }

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
