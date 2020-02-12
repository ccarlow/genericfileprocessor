package genericfileprocessor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import genericfileprocessor.listener.FormatFieldIndexListener;
import genericfileprocessor.listener.FormatListener;

@XmlRootElement(name = "format")
@XmlAccessorType(XmlAccessType.FIELD)
public class Format extends SuperField {
  @XmlElementWrapper(name="fields")
  @XmlElement(name="field")
  private List<Field> fields = new ArrayList<Field>();
  private Type2 type;
  private String name;
  
  @XmlTransient
  private Map<String, Field> fieldMap = new HashMap<String, Field>();
  
  @XmlTransient
  private List<FormatListener> formatListeners = new ArrayList<FormatListener>();
  
  @XmlTransient
  private List<FormatFieldIndexListener> formatFieldIndexListeners = new ArrayList<FormatFieldIndexListener>();

  public static enum Type2 {
    text, binary, netcdf
  }
  
  public Format() {

  }
  
  public List<Field> getFieldsCopy() {
    return new ArrayList<Field>(fields);
  }
  
  public void removeField(Field field) {
    this.fields.remove(field);
  }
  
//  public void addField(Field field) {
//    if (!fields.contains(field)) {
//     fields.add(field); 
//     field.setFormat(this);
//     for (FormatListener formatListener : formatListeners) {
//       formatListener.fieldAdded(field);
//     }
//    }
//  }
  
  protected Map<String, Field> getFieldMap() {
    return fieldMap;
  }

  protected List<Field> getFields() {
    return fields;
  }

  protected void setFields(List<Field> fields) {
    for (Field field : fields) {
      field.setFormat(this);
    }
    this.fields = fields;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public Type2 getType() {
    return type;
  }

  public void setType(Type2 type) {
    this.type = type;
  }
  
  public Field getNextField() {
    List<NextField> nexts = getNextFields();
    if (nexts != null) {
      for (NextField next : nexts) {
        return fieldMap.get(next.getFieldRef());
      }
    }
    return null;
  }
  
  public void setFieldIndexAfter(Field field, String afterField) {
    int newIndex = 0;
    fields.remove(field);
    for (int i = 0; i < fields.size(); i++) {
      if (fields.get(i).getName().equals(afterField)) {
        newIndex = i;
        break;
      }
    }
    field.setFormat(this);
    fields.add(newIndex + 1, field);
    for (FormatFieldIndexListener listener : formatFieldIndexListeners) {
      listener.fieldIndexChanged(field, newIndex + 1);
    }
  }
  
  public void addFormatListener(FormatListener listener) {
    if (!formatListeners.contains(listener)) {
      formatListeners.add(listener); 
    }
  }
  
  public void removeFormatListener(FormatListener listener) {
    formatListeners.remove(listener);
  }
  
  public void addFormatFieldIndexListener(FormatFieldIndexListener listener) {
    if (!formatFieldIndexListeners.contains(listener)) {
      formatFieldIndexListeners.add(listener); 
    }
  }
  
  public void removeFormatFieldIndexListener(FormatFieldIndexListener listener) {
    formatFieldIndexListeners.remove(listener);
  }
}
