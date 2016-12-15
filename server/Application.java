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
    ArrayList<Dependency> functionalDependencies = new ArrayList<>();

    getRelationAndFD(relation, functionalDependencies, input);

    getClosures(relation, functionalDependencies);
    // getMinimalBasis(functionalDependencies);

  }

  public static void getClosures(ArrayList<String> relation, ArrayList<Dependency> functionalDependencies) {
    System.out.println("Start finding closures for: ");
    showRelation(relation);
    showCurrentFD(functionalDependencies);

    // Get all possible combination for the relation
    ArrayList<Set> combinations = new ArrayList<>();
    combinations.addAll(getAllCombinations(relation));

    System.out.println("Combinations: --");
    for (Set c : combinations) {
      System.out.println(c.toString());
    }

    System.out.println();
    System.out.println("Closures: --");
    // findClosure(functionalDependencies, <a combination>)
    // return possible combination
  }

  public static ArrayList<Set> getAllCombinations(ArrayList<String> relation) {
    ArrayList< ArrayList< Set > > combinations = new ArrayList<>();

    // initialize each arraylist
    for (int i = 0; i < relation.size(); i++) {
      combinations.add(new ArrayList<Set>());
    }

    // base case
    for (String attr : relation) {
      ArrayList<String> set = new ArrayList<>();
      set.add(attr);

      combinations.get(0).add(new Set(set));
    }

    // get combinations, index + 1 indicates the size of combinations
    // e.g for relation "a b c", index 1 would have (1+1) "{ab, ac, bc}"
    for (int i = 1; i < relation.size(); i++) {

      // get combinations for each possible base
      int size = i + 1;
      for (int index = 0; index < relation.size(); index++) {
        // Break out of the loop if new base goes out of bounds
        if (index + size > relation.size()) {
          break;
        }

        // Base
        String base = relation.get(index);
        ArrayList<Set> oldCombination = combinations.get(i-1);
        ArrayList<Set> newCombination = combinations.get(i);
        for (int j = 0; j < oldCombination.size(); j++) {
            Set currentSet = oldCombination.get(j).clone();

            if (hasCombination(currentSet, relation, index)) {
              continue;
            }

            currentSet.add(base);
            newCombination.add(currentSet);
        }
      }
    }

    ArrayList<Set> allCombinations = new ArrayList<>();
    for (ArrayList<Set> c : combinations) {
      allCombinations.addAll(c);
    }

    return allCombinations;
  }

  public static boolean hasCombination(Set set, ArrayList<String> relation, int index) {
    for (int i = 0; i <= index; i++) {
      if (set.contains(relation.get(i))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the relation and functional dependencies through reference
   *
   * @param  relation - the relation
   * @param  functionalDependencies - functional dependencies of the relation
   * @param  input - scanner
   */
  public static void getRelationAndFD(ArrayList<String> relation,
                                      ArrayList<Dependency> functionalDependencies,
                                      Scanner input) {
    // Prompt for a valid relation
    String sRelation;
    do {
      sRelation = "";

      System.out.print("Enter relation R separated by a whitespace >>");
      sRelation = input.nextLine();
      sRelation = sRelation.toLowerCase();

    } while (!isValidRelation(sRelation));

    // Store and output relation
    System.out.println("Relation saved.");
    relation.addAll(createRelation(sRelation));

    // Prompt for valid set of functional dependencies
    String functionalDependency = "";
    boolean isValidFD = false;
    do {
      if (isValidFD) {
        String[] fd = functionalDependency.split("->");
        ArrayList<String> LHS = new ArrayList<String>(Arrays.asList(fd[0].trim().split("\\s+")));
        ArrayList<String> RHS = new ArrayList<String>(Arrays.asList(fd[1].trim().split("\\s+")));

        functionalDependencies.add(new Dependency(new Set(LHS), new Set(RHS)));
      }

      showRelation(relation);
      showCurrentFD(functionalDependencies);

      System.out.print("Enter a single functional dependency 'A B -> C D' (-1 to end):");
      functionalDependency = input.nextLine();
      functionalDependency = functionalDependency.toLowerCase();

      if (isValidFunctionalDependency(functionalDependency, relation)) {
        isValidFD = true;
      } else {
        isValidFD = false;
      }

    } while (isValidFD || !functionalDependency.equalsIgnoreCase("-1"));

    System.out.println("Functional Dependencies saved.");
    System.out.println();
  }

  public static void showRelation(ArrayList<String> relation) {
    System.out.print("R(");
    for (String attribute : relation) {
      if (!attribute.equals(relation.get(relation.size() - 1))) {
        attribute += ",";
      }
      System.out.print(attribute);
    }
    System.out.println(")");
  }

  public static void showCurrentFD(ArrayList<Dependency> fd) {
    System.out.println("Current FDs:");
    for (Dependency d : fd ) {
      System.out.println(d.toString());
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
      if (!functionalDependency.equals("-1")) {
        System.out.println("Please follow the format 'A B C -> D E F'");
      }

      valid = false;
    } else {
      // Make sure each attribute used in the FD exists in the relation
      for (String side : fd) {
        for(String attribute : side.trim().split("\\s+")) {
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
