import java.util.ArrayList;

public class Relation {
  private Set mAttrs;
  private ArrayList<Dependency> mFunctionalDependencies;

  public Relation(Set attrs, ArrayList<Dependency> functionalDependencies) {
    mAttrs = new Set(attrs);
    mFunctionalDependencies = new ArrayList<>();
    mFunctionalDependencies.addAll(functionalDependencies);
  }

  public void setAttrs(Set attrs) {
    mAttrs = new Set(attrs);
  }

  public void setFunctionalDependencies(ArrayList<Dependency> functionalDependencies) {
    mFunctionalDependencies = new ArrayList<>();
    mFunctionalDependencies.addAll(functionalDependencies);
  }

  @Override
  public String toString() {
    String ret = "R(" + mAttrs.toString() + ")\n";
    for (Dependency d : mFunctionalDependencies) {
      ret += "  " + d.toString() + "\n";
    }
    return ret;
  }
}
