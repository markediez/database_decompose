/**
* Author: Mark Emmanuel Diez
* Date: 09 December 2016
* CLI for database
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;

public class Application {
  public static void main(String[] args) {
    // Initialize variables
    Scanner input = new Scanner(System.in);
    ArrayList<String> relation = new ArrayList<>();
    ArrayList<Dependency> functionalDependencies = new ArrayList<>();

    Relation R = createRelationCLI(relation, functionalDependencies, input);
    System.out.println("Created Relation --");
    System.out.println(R.toString());

    // Find minimal cover
    R.setFunctionalDependencies(findMinimalBasis(R));
    System.out.println(R.toString());

    decomposeBCNF(R);
    // decompose3NF(relation)
  }

  public static ArrayList<Relation> decomposeBCNF(Relation r) {
    ArrayList<Relation> relations = new ArrayList<>();

    r.setFunctionalDependencies(findMinimalBasis(r));
    Dependency violation = findBCNFViolation(r);
    System.out.println("R ----");
    System.out.println(r.toString());

    if(violation == null) {
      relations.add(r);
    } else {
      // {X}+
      Set r1Attr = findClosure(r.getFunctionalDependencies(), violation.getLHS());

      // R not in {X}+ + X
      Set r2Attr = r.getAttrs();
      r2Attr.subtract(r1Attr);

      System.out.println(r2Attr.toString());
      Relation r1 = new Relation(r1Attr, new ArrayList<>());
      Relation r2 = new Relation(r2Attr, new ArrayList<>());

      r1.setFunctionalDependencies(projectFD(r, r1));
      r2.setFunctionalDependencies(projectFD(r, r2));

      System.out.println("R1 -- ");
      System.out.println(r1.toString());

      System.out.println("R2 -- ");
      System.out.println(r2.toString());
      //
      // relations.addAll(decomposeBCNF(r1));
      // relations.addAll(decomposeBCNF(r2));
    }
    System.out.println("Violation ---");
    System.out.println(violation.toString());
    return relations;
  }

  public static ArrayList<Dependency> projectFD(Relation original, Relation decomposed) {
    ArrayList<Dependency> projectedFD = new ArrayList<>();

    ArrayList<Set> possibleLHS = findCombination(decomposed.getAttrs().getAttributes());
    for (Set lhs : possibleLHS) {
      Set rhs = findClosure(original.getFunctionalDependencies(), lhs);
      // Only add non-trivial FDs
      if (!rhs.subsetOf(lhs)) {
        Set projectedRHS = new Set();
        System.out.println("NON TRIVIAL :: ");
        System.out.println(lhs + " -> " + rhs);
        for (String attr : rhs.getAttributes()) {
          if (!lhs.contains(attr) && decomposed.getAttrs().contains(attr)) {
            projectedRHS.add(attr);
          }
        }

        if (!projectedRHS.isEmpty()) {
          projectedFD.add(new Dependency(lhs, projectedRHS));
        }
      }
    }

    return projectedFD;
  }

  public static Dependency findBCNFViolation(Relation r) {
    for (Dependency d : r.getFunctionalDependencies()) {
      Set x = findClosure(r.getFunctionalDependencies(), d.getLHS());

      // If RHS of the dependency is not a key, then it is a bcnf violation
      if (!r.getAttrs().subsetOf(x)) {
        return d;
      }

    }

    return null;
  }

  public static ArrayList<Dependency> findMinimalBasis(Relation r) {
    System.out.println("Finding Minimal Basis ---");

    // Singleton RHS
    System.out.println("... Singleton RHS");
    ArrayList<Dependency> newDependency = new ArrayList<>();
    newDependency = setSingletonRHS(r.getFunctionalDependencies());
    r.setFunctionalDependencies(newDependency);

    System.out.println(r.toString());

    // Remove erroneous FD
    System.out.println("... Removing erroenous functional dependencies");
    newDependency = removeErroneousFD(newDependency);
    r.setFunctionalDependencies(newDependency);


    return newDependency;
  }

  public static ArrayList<Dependency> removeErroneousFD(ArrayList<Dependency> dependencies) {
    for(Iterator<Dependency> iterator = dependencies.iterator(); iterator.hasNext(); ) {
      Dependency d = iterator.next();

      // Check for erroneous attributes
      ArrayList<String> toRemove = new ArrayList<>();
      for (String attr : d.getLHS().getAttributes()) {
        // get closures
        Set closure = findClosure(dependencies, new Set(new String[]{attr}));
        Set currSet = new Set(d.getLHS());
        currSet.remove(attr);

        for (String remainingAttr : currSet.getAttributes()) {
          if (closure.contains(remainingAttr)) {
            toRemove.add(remainingAttr);
          }
        }
      }

      for (String remove : toRemove) {
        d.getLHS().remove(remove);
      }

      // Check for erroneous FD
      if (isErroneousFD(d, dependencies)) {
        iterator.remove();
      }

      // Check for erroenous attributes
    }

    return dependencies;
  }

  public static boolean isErroneousFD(Dependency currFD, ArrayList<Dependency> dependencies) {
    ArrayList<Dependency> tempDependency = new ArrayList<>(dependencies);
    tempDependency.remove(currFD);
    Set closure = findClosure(tempDependency, currFD.getLHS());

    return closure.contains(currFD.getRHS());
  }

  public static ArrayList<Dependency> setSingletonRHS(ArrayList<Dependency> dependencies) {
    // This is necessary because we cannot modify the list while we are iterating over it
    ArrayList<Dependency> toAdd = new ArrayList<>();

    for (Iterator<Dependency> iterator = dependencies.iterator(); iterator.hasNext();) {
      Dependency d = iterator.next();
      if (d.getRHS().getAttributes().size() > 1) {
        for (String attr : d.getRHS().getAttributes()) {
          Dependency newDependency = new Dependency(d.getLHS(), new Set(new String[]{attr}) );
          toAdd.add(newDependency);
        }

        // Only way to safely delete an object from the list safely
        iterator.remove();
      }
    }

    dependencies.addAll(toAdd);
    return dependencies;
  }

  public static void getClosures(ArrayList<String> relation, ArrayList<Dependency> functionalDependencies) {
    System.out.println("Start finding closures for: ");
    showRelation(relation);
    showCurrentFD(functionalDependencies);

    // Get all possible combination for the relation
    ArrayList<Set> combinations = new ArrayList<>();
    combinations.addAll(findCombination(relation));

    System.out.println("Combinations: --");
    for (Set c : combinations) {
      System.out.println(c.toString());
    }

    System.out.println();
    System.out.println("Closures: --");
    for (Set s : combinations) {
      System.out.println(s.toString() + "+:");
      Set close = findClosure(functionalDependencies, s);
    }
    // return possible combination
  }

  public static Set findClosure(ArrayList<Dependency> functionalDependencies, Set set) {
    Set closure = new Set(set);

    boolean added = false;
    do {
      added = false;

      for (Dependency d : functionalDependencies) {
        if (d.getLHS().subsetOf(closure)) {
          for (String newAttr : d.getRHS().getAttributes()) {
            if (!closure.contains(newAttr)) {
              closure.add(newAttr);
              added = true;
            }
          }
        }
      }

    } while(added);

    return closure;
  }

  public static ArrayList<Set> findCombination(ArrayList<String> relation) {
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
  public static Relation createRelationCLI(ArrayList<String> relation,
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
    relation.addAll(stringRelations(sRelation));

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

    return new Relation(new Set(relation), functionalDependencies);
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
  public static ArrayList<String> stringRelations(String relationString) {
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
