package genericfileprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class SuperField {
  private String name;
  private Length length;
  private Precision precision;
  private String delimiter;
  private String defaultValue;
  private Alignment alignment;
  private List<NextField> nextFields = new ArrayList<NextField>();
  
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
    if (superField.nextFields != null) {
      nextFields = new ArrayList<NextField>();
      nextFields.addAll(superField.nextFields);
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

  @XmlElementWrapper(name="nextFields")
  @XmlElement(name="nextField")
  public void setNextFields(List<NextField> nextFields) {
    this.nextFields = nextFields;
  }
  
  public List<NextField> getNextFields() {
    return nextFields;
  }
  
  public static class Condition {
    public enum Operator {
      and, or, equals, less, greater
    }
    private Operator operator;
    private String field;
    private List<Condition> conditions;
    private String value;
    
    public void setOperator(Operator operator) {
      this.operator = operator;
    }
    
    public Operator getOperator() {
      return operator;
    }
    
    public void setField(String field) {
      this.field = field;
    }
    
    public String getField() {
      return field;
    }
    
    @XmlElementWrapper(name="conditions")
    @XmlElement(name="condition")
    public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
    }
    
    public List<Condition> getConditions() {
      return conditions;
    }
    
    public void setValue(String value) {
      this.value = value;
    }
    
    public String getValue() {
      return value;
    }
    
    public boolean evaluate(Field field, Map<String, List<Field>> fieldMap) {
      if (Operator.equals.equals(operator)) {
        if (value != null) {
          List<Field> fieldList = fieldMap.get(this.field);
          if (fieldList != null) {
            field = fieldList.get(fieldList.size() - 1); 
          }
          if (field != null) {
            if (Field.Type.number.equals(field.getType())) {
              try {
                Double value = Double.parseDouble(field.getDefaultValue());
              } catch (NumberFormatException e) {

              }
            } else if (Field.Type.text.equals(field.getType())) {
              return value.equals(field.getDefaultValue());
            }
          }
        }
      } else if (Operator.or.equals(operator)) {
        for (Condition condition : conditions) {
          if (condition.evaluate(field, fieldMap)) {
            return true;
          }
        }
      } else if (Operator.and.equals(operator)) {
        for (Condition condition : conditions) {
          if (!condition.evaluate(field, fieldMap)) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
  }
  
  public static class NextField extends FieldRef {
    private Condition condition;
    
    public NextField() {
    }
    
    public void setCondition(Condition condition) {
      this.condition = condition;
    }
    
    public Condition getCondition() {
      return condition;
    }
  }
  
  public static class Length extends ValueOrFieldRef {
  }

  public static class Precision extends ValueOrFieldRef {
  }

  public static class FieldRef {
    private String field;

    public String getField() {
      return field;
    }

    public void setField(String field) {
      this.field = field;
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
