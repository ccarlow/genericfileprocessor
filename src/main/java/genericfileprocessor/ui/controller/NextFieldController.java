package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import genericfileprocessor.Field;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.listener.FieldSelectorListener;
import genericfileprocessor.listener.NextFieldSelectorListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class NextFieldController implements Initializable, FieldSelectorListener, NextFieldSelectorListener {
  @FXML
  private ComboBox<String> fieldRef;
  
  private Field field;

  private NextField nextField;
  
  public static final String CONDITIONS_TREE_ITEM = "Conditions";
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
  }
  
  public void applyChanges() {
    if (nextField != null) {
      nextField.setFieldRef(this.fieldRef.getSelectionModel().getSelectedItem());
    }
  }
  
  public void saveAsNew() {
    nextField = new NextField();
    applyChanges();
    field.addNextField(nextField);
  }

  @Override
  public void fieldSelected(Field field) {
    this.field = field;
    this.fieldRef.getItems().clear();
    if (field != null) {
      for (Field fieldItem : field.getFormat().getFieldsCopy()) {
        this.fieldRef.getItems().add(fieldItem.getName());
      } 
    }
  }

  @Override
  public void nextFieldSelected(NextField nextField) {
    this.nextField = nextField;
    if (nextField != null) {
      fieldSelected((Field)nextField.getField());
      fieldRef.getSelectionModel().select(nextField.getFieldRef()); 
    }
  }
}
