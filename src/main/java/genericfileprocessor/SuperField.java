package genericfileprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import genericfileprocessor.listener.FieldRefListener;
import genericfileprocessor.listener.NextFieldListener;

public class SuperField {
  private String name;
  private Length length;
  private Precision precision;
  private String delimiter;
  private String defaultValue;
  private Alignment alignment;
  private List<NextField> nextFields = new ArrayList<NextField>();
  private List<NextFieldListener> nextFieldListeners = new ArrayList<NextFieldListener>();
  
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
  
  public String toString() {
    return name;
  }

  @XmlElementWrapper(name="nextFields")
  @XmlElement(name="nextField")
  protected void setNextFields(List<NextField> nextFields) {
    this.nextFields = nextFields;
  }
  
  protected List<NextField> getNextFields() {
    return nextFields;
  }
  
  public List<NextField> getNextFieldsCopy() {
    return new ArrayList<NextField>(nextFields);
  }
  
  public void addNextFieldListener(NextFieldListener nextFieldListener) {
    if (!nextFieldListeners.contains(nextFieldListener)) {
      nextFieldListeners.add(nextFieldListener); 
    }
  }
  
  public void removeNextFieldListener(NextFieldListener nextFieldListener) {
    nextFieldListeners.remove(nextFieldListener);
  }
  
  public void addNextField(NextField nextField) {
    this.nextFields.add(nextField);
    nextField.setField(this);
    for (NextFieldListener nextFieldListener : nextFieldListeners) {
      nextFieldListener.nextFieldAdded(nextField);
    }
  }
  
  public static class Condition {
    public enum Operator {
      and, or, equals, less, greater
    }
    private Operator operator;
    private String field;
    private List<Condition> conditions;
    private String value;
    private Condition parentCondition;
    
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
    
    public Condition getParentCondition() {
      return parentCondition;
    }
    
    @XmlElementWrapper(name="conditions")
    @XmlElement(name="condition")
    protected void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
      for (Condition condition : conditions) {
        condition.parentCondition = this;
      }
    }
    
    public boolean addChildCondition(Condition condition) {
      if (!conditions.contains(condition)) {
        conditions.add(condition);
        return true;
      }
      return false;
    }
    
    public List<Condition> getConditionsCopy() {
      return conditions == null ? null : new ArrayList<Condition>(conditions);
    }
    
    public boolean removeChildCondition(Condition condition) {
      return conditions.remove(condition);
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
  
  public static class NextFieldCondition extends Condition {
    private NextField nextField;
    
    public void setNextField(NextField nextField) {
      this.nextField = nextField;
    }
    
    public NextField getNextField() {
      return nextField;
    }

    @Override
    public void setOperator(Operator operator) {
      super.setOperator(operator);
      nextField.notifyNextFieldConditionAdded(this);
    }
    
    @Override
    public boolean addChildCondition(Condition condition) {
      boolean notify = super.addChildCondition(condition);
      if (notify) {
        nextField.notifyNextFieldConditionAdded(this);
      }
      return notify;
    }
    
    @Override
    public boolean removeChildCondition(Condition condition) {
      boolean notify = super.removeChildCondition(condition);
      if (notify) {
        nextField.notifyNextFieldConditionRemoved(this);
      }
      return notify;
    }
  }
  
  public static class NextField extends FieldRef {
    private NextFieldCondition condition;
    private List<NextFieldListener> nextFieldListeners = new ArrayList<NextFieldListener>();
    
    public NextField() {
    }
    
    public void addNextFieldListener(NextFieldListener nextFieldListener) {
      if (!nextFieldListeners.contains(nextFieldListener)) {
        nextFieldListeners.add(nextFieldListener); 
      }
    }
    
    public void removeNextFieldListener(NextFieldListener nextFieldListener) {
      nextFieldListeners.remove(nextFieldListener);
    }
    
    public void setCondition(NextFieldCondition condition) {
      this.condition = condition;
      condition.setNextField(this);
      notifyNextFieldConditionAdded(condition);
    }
    
    public void notifyNextFieldConditionAdded(NextFieldCondition condition) {
      for (NextFieldListener nextFieldListener : nextFieldListeners) {
        nextFieldListener.nextFieldConditionAdded(condition);
      }
    }
    
    public void notifyNextFieldConditionRemoved(NextFieldCondition condition) {
      for (NextFieldListener nextFieldListener : nextFieldListeners) {
        nextFieldListener.nextFieldConditionRemoved(condition);
      }
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
    private String fieldRef;
    private SuperField field;
    private List<FieldRefListener> fieldRefListeners = new ArrayList<FieldRefListener>();

    public String getFieldRef() {
      return fieldRef;
    }

    public void setFieldRef(String fieldRef) {
      this.fieldRef = fieldRef;
      for (FieldRefListener fieldRefListener : fieldRefListeners) {
        fieldRefListener.fieldRefChanged(this);
      }
    }
    
    public void addFieldRefListener(FieldRefListener fieldRefListener) {
      if (!fieldRefListeners.contains(fieldRefListener)) {
        fieldRefListeners.add(fieldRefListener); 
      }
    }
    
    public void removeFieldRefListener(FieldRefListener fieldRefListener) {
      fieldRefListeners.remove(fieldRefListener);
    }
    
    public void setField(SuperField field) {
      this.field = field;
    }
    
    public SuperField getField() {
      return field;
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
