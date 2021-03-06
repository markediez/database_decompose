public class Dependency {
  private Set mLHS, mRHS;

  public Dependency() {
    mLHS = new Set();
    mRHS = new Set();
  }

  public Dependency(Set LHS, Set RHS) {
    mLHS = LHS;
    mRHS = RHS;
  }

  public boolean lhsContains(Set s) {
    for (String attr : s.getAttributes()) {
      if (mLHS.contains(attr)) {
        return true;
      }
    }

    return false;
  }

  public void setLHS(Set LHS) {
    mLHS = LHS;
  }

  public void setRHS(Set RHS) {
    mRHS = RHS;
  }

  public Set getLHS() { return mLHS; }
  public Set getRHS() { return mRHS; }

  @Override
  public String toString() {
    return mLHS.toString() + " -> " + mRHS.toString();
  }
}
