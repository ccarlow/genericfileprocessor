package genericfileprocessor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import genericfileprocessor.SuperField.Next;

public class FieldGroup extends Field {
  private Format format;
  private Map<String, GroupField> fields = new LinkedHashMap<String, GroupField>();

  public FieldGroup() {}
  
  public FieldGroup(FieldGroup fieldGroup) {
    super(fieldGroup);
    fields = new LinkedHashMap<String, GroupField>();
    for (Entry<String, GroupField> entry : fieldGroup.fields.entrySet()) {
      fields.put(entry.getKey(), new GroupField(entry.getValue())); 
    }
  }
  
  public Map<String, GroupField> getFields() {
    return fields;
  }

  public void setFields(Map<String, GroupField> fields) {
    this.fields = fields;
  }

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }
  
  public GroupField getNextGroupField() {
    Next next = getNext();
    if (next != null) {
      FieldGroup fieldGroup = format.getFieldGroups().get(next.getFieldGroup());
      if (fieldGroup != null) {
        return fieldGroup.getFields().get(next.getGroupField()); 
      } 
    }
    return null;
  }
}
