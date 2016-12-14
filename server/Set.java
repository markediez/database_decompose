
import java.util.ArrayList;

public class Set {
  private ArrayList<String> attributes;

  public Set() {
    attributes = new ArrayList<>();
  }

  public Set(ArrayList<String> attributes) {
    this();
    this.attributes.addAll(attributes);
  }

  public boolean contains(String attr) {
    return attributes.contains(attr);
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

  public ArrayList<String> getAttributes() { return this.attributes; }
  public void add(String str) { this.attributes.add(str); }
}
