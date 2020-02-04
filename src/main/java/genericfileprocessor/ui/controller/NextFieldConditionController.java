package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import genericfileprocessor.Field;
import genericfileprocessor.Format;
import genericfileprocessor.SuperField.Condition;
import genericfileprocessor.listener.FieldSelectorListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class NextFieldConditionController implements Initializable, FieldSelectorListener {
  @FXML
  private ComboBox field;

  public static final String CONDITIONS_TREE_ITEM = "Conditions";
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
  }
  
  public void setCondition(Condition condition) {
    if (condition.getField() != null) {
      field.getSelectionModel().select(condition.getField()); 
    }
  }

  @Override
  public void fieldSelected(Field field) {
    for (Field fieldItem : field.getFormat().getFields()) {
      this.field.getItems().add(fieldItem.getName());
    }
  }
}
