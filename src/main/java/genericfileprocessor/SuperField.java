package genericfileprocessor;

public class SuperField {
  private String name;
  private Length length;
  private Precision precision;
  private String delimiter;
  private String defaultValue;
  private Alignment alignment;
  private Next next;
  
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
    if (superField.next != null) {
      next = new Next();
      next.setFieldGroup(superField.next.getFieldGroup());
      next.setGroupField(superField.next.getGroupField()); 
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

  public void setNext(Next next) {
    this.next = next;
  }
  
  public Next getNext() {
    return next;
  }
  
  public static class Next extends FieldRef {
    
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
