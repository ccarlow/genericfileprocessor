package genericfileprocessor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Format extends SuperField {
  private Map<String, FieldGroup> fieldGroups = new LinkedHashMap<String, FieldGroup>();
  private String type;
  private String name;

  public static enum Type {
    text, binary, netcdf
  }
  
  public Format() {

  }

  public Map<String, FieldGroup> getFieldGroups() {
    return fieldGroups;
  }

  public void setFieldGroups(Map<String, FieldGroup> fieldGroups) {
    this.fieldGroups = fieldGroups;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public GroupField getNextGroupField() {
    List<Next> nexts = getNexts();
    if (nexts != null) {
      for (Next next : nexts) {
        FieldGroup fieldGroup = getFieldGroups().get(next.getFieldGroup());
        if (fieldGroup != null) {
          return fieldGroup.getFields().get(next.getGroupField()); 
        } 
      }
    }
    return null;
  }
}
