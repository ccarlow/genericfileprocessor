package genericfileprocessor.listener;

import genericfileprocessor.Field;
import genericfileprocessor.Format;

public interface FormatFieldIndexListener {
  public void fieldIndexChanged(Field field, int index);
}
