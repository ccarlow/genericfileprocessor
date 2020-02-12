package genericfileprocessor.listener;

import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.SuperField.NextFieldCondition;

public interface NextFieldListener {
  public void nextFieldAdded(NextField nextField);
  public void nextFieldConditionAdded(NextFieldCondition condition);
  public void nextFieldConditionRemoved(NextFieldCondition condition);
}