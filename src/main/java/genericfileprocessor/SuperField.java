package genericfileprocessor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class SuperField {
  private String name;
  private Length length;
  private Precision precision;
  private String delimiter;
  private String defaultValue;
  private Alignment alignment;
  private List<Next> nexts = new ArrayList<Next>();
  
  public static enum Alignment {
    left, right, center
  }
  
  public SuperField() {

  }
  
  public SuperField(SuperField superField) {
    name = superField.name;
    length = superField.length;
    precision = superField.precision;
    delimiter = superField.delimiter;
    defaultValue = superField.defaultValue;
    alignment = superField.alignment;
    if (superField.nexts != null) {
      nexts = new ArrayList<Next>();
      nexts.addAll(superField.nexts);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public void setAlignment(Alignment alignment) {
    this.alignment = alignment;
  }
  
  public Alignment getAlignment() {
    return alignment;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public String getDelimiter() {
    return delimiter;
  }
  
  public Length getLength() {
    return length;
  }

  public void setLength(Length length) {
    this.length = length;
  }

  public Precision getPrecision() {
    return precision;
  }

  public void setPrecision(Precision precision) {
    this.precision = precision;
  }
  
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  public String getDefaultValue() {
    return defaultValue;
  }

  @XmlElementWrapper(name="nexts")
  @XmlElement(name="next")
  public void setNexts(List<Next> nexts) {
    this.nexts = nexts;
  }
  
  public List<Next> getNexts() {
    return nexts;
  }
  
  public static class Condition {
    enum Operation {
      and, or, equals, less, greater
    }
    private Operation operation;
    private String fieldGroup;
    private String groupField;
    private List<Condition> conditions;
    
    public boolean evaluate() {
      return true;
    }
  }
  
  public static class Next extends FieldRef {
    public Next() {
      
    }
  }
  
  public static class Length extends ValueOrFieldRef {
  }

  public static class Precision extends ValueOrFieldRef {
  }

  public static class FieldRef {
    private String fieldGroup;
    private String groupField;

    public String getFieldGroup() {
      return fieldGroup;
    }

    public void setFieldGroup(String fieldGroup) {
      this.fieldGroup = fieldGroup;
    }

    public String getGroupField() {
      return groupField;
    }

    public void setGroupField(String groupField) {
      this.groupField = groupField;
    }
  }

  public static class ValueOrFieldRef extends FieldRef {
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }
}
