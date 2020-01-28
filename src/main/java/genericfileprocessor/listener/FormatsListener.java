package genericfileprocessor.listener;

import genericfileprocessor.GroupField;
import genericfileprocessor.FieldGroup;
import genericfileprocessor.Format;

public interface FormatsListener {
  public void formatAdded(String key, Format format);

  public void fieldGroupAdded(String format, String key, FieldGroup fieldGroup);

  public void fieldAdded(String format, String fieldGroup, String key, GroupField field);
}
