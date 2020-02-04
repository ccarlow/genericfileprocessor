package genericfileprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field extends SuperField {
  private Type type;
  private String name;
  private Format format;
  
  private Field nextField;
  private Field previousField;
  
  public static enum Type {
    text, number
  }
  
  public Field() {
    super();
  }
  
  public Field(Field field) {
    super(field);
    type = field.type;
    format = field.getFormat();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
  
  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }
  
  public Field getNextField(Map<String, List<Field>> fieldMap) {
    List<NextField> nexts = getNextFields();
    if (nexts != null) {
      for (NextField next : nexts) {
        if (next.getCondition() != null) {
          if (next.getCondition().evaluate(this, fieldMap)) {
            return format.getFieldMap().get(next.getField()); 
          } 
        } else {
          return format.getFieldMap().get(next.getField());
        }
      }
    }
    return null;
  }
  
  public void setActualNextField(Field nextField) {
    this.nextField = nextField;
    nextField.previousField = this;
  }
  
  public Field getActualNextField() {
    return nextField;
  }
  
  public Field getActualPreviousField() {
    return previousField;
  }
}
