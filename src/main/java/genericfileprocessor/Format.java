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

@XmlRootElement(name = "format")
@XmlAccessorType(XmlAccessType.FIELD)
public class Format extends SuperField {
  @XmlElementWrapper(name="fields")
  @XmlElement(name="field")
  private List<Field> fields = new ArrayList<Field>();
  private Map<String, Field> fieldMap = new HashMap<String, Field>();
  private Type2 type;
  private String name;

  public static enum Type2 {
    text, binary, netcdf
  }
  
  public Format() {

  }
  
  public Map<String, Field> getFieldMap() {
    return fieldMap;
  }

  public List<Field> getFields() {
    return fields;
  }

  public void setFields(List<Field> fields) {
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
        return fieldMap.get(next.getField());
      }
    }
    return null;
  }
}
