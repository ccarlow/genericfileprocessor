package genericfileprocessor;

import java.util.List;

public class GroupField extends Field {
  private FieldGroup fieldGroup;
  
  public GroupField() {
    super();
  }
  
  public GroupField(GroupField groupField) {
    super(groupField);
    fieldGroup = groupField.getFieldGroup();
  }
  
  public FieldGroup getFieldGroup() {
    return fieldGroup;
  }

  public void setFieldGroup(FieldGroup fieldGroup) {
    this.fieldGroup = fieldGroup;
  }
  
  public GroupField getNextGroupField() {
    List<Next> nexts = getNexts();
    if (nexts != null) {
      for (Next next : nexts) {
        FieldGroup fieldGroup = this.fieldGroup.getFormat().getFieldGroups().get(next.getFieldGroup());
        if (fieldGroup != null) {
          return fieldGroup.getFields().get(next.getGroupField()); 
        } 
      }
    }
    return null;
  }
}
