import java.util.ArrayList;

public class Relation {
  private Set mAttrs;
  private ArrayList<Dependency> mFunctionalDependencies;

  public Relation() {
    mAttrs = new Set();
    mFunctionalDependencies = new ArrayList<>();
  }

  public Relation(Set attrs, ArrayList<Dependency> functionalDependencies) {
    mAttrs = new Set(attrs);
    mFunctionalDependencies = new ArrayList<>();
    mFunctionalDependencies.addAll(functionalDependencies);
  }

  public Relation (Relation rhs) {
    this();
    setAttrs(rhs.getAttrs());
    setFunctionalDependencies(rhs.getFunctionalDependencies());
  }

  public void setAttrs(Set attrs) {
    mAttrs = new Set(attrs);
  }

  public void setFunctionalDependencies(ArrayList<Dependency> functionalDependencies) {
    mFunctionalDependencies = new ArrayList<>();
    mFunctionalDependencies.addAll(functionalDependencies);
  }

  public ArrayList<Dependency> getFunctionalDependencies() { return mFunctionalDependencies; }
  public Set getAttrs() { return mAttrs; }

  @Override
  public String toString() {
    String ret = "R(" + mAttrs.toString() + ")\n";
    for (Dependency d : mFunctionalDependencies) {
      ret += "  " + d.toString() + "\n";
    }
    return ret;
  }
}
