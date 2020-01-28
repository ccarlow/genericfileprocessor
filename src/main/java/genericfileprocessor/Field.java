package genericfileprocessor;

public class Field extends SuperField {
  private Type type;
  
  public static enum Type {
    text, number
  }
  
  public Field() {
    super();
  }
  
  public Field(Field field) {
    super(field);
    type = field.type;
  }
  
  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }
}
