
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Set {
  private ArrayList<String> attributes;

  public Set() {
    attributes = new ArrayList<>();
  }

  public Set(String[] attributes) {
    this(Arrays.asList(attributes));
  }

  public Set(Set RHS) {
    this(RHS.getAttributes());
  }

  public Set(List<String> attributes) {
    this();
    this.attributes.addAll(attributes);
  }

  public boolean contains(String attr) {
    return attributes.contains(attr);
  }

  public boolean contains(Set set) {
    for (String attr : set.getAttributes()) {
      if (!contains(attr)) {
        return false;
      }
    }

    return true;
  }

  public boolean subsetOf(Set rhs) {
    for (String attr : attributes) {
      if (!rhs.contains(attr)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    String str = "{";
    for (int i = 0; i < attributes.size(); i++) {
      if (i != attributes.size() - 1) {
        str += attributes.get(i) + ",";
      } else {
        str += attributes.get(i);
      }
    }
    str += "}";

    return str;
  }

  @Override
  public Set clone() {
    return new Set(this.attributes);
  }

  public void subtract(Set rhs) {
    for (String attr : rhs.getAttributes()) {
      remove(attr);
    }
  }

  public ArrayList<String> getAttributes() { return this.attributes; }
  public void add(String str) { this.attributes.add(str); }
  public void add(Set set) { this.attributes.addAll(set.getAttributes()); }
  public boolean remove(String attr) { return attributes.remove(attr); }
}
