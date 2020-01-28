package genericfileprocessor;

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
    Next next = getNext();
    if (next != null) {
      FieldGroup fieldGroup = this.fieldGroup.getFormat().getFieldGroups().get(next.getFieldGroup());
      if (fieldGroup != null) {
        return fieldGroup.getFields().get(next.getGroupField()); 
      } 
    }
    return null;
  }
}
