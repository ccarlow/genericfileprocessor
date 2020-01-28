package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import genericfileprocessor.GroupField;
import genericfileprocessor.SuperField;
import genericfileprocessor.FieldGroup;
import genericfileprocessor.Format;
import genericfileprocessor.listener.FieldListener;
import genericfileprocessor.listener.FormatsListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class FieldController implements Initializable, FieldListener {
  @FXML
  private TextField name;
  
  @FXML
  private TextField lengthValue;
  
  @FXML
  private TextField delimiter;
  
  @FXML
  private TextField defaultValue;

  @FXML
  private ComboBox<String> type;
  
  @FXML
  private ComboBox<String> fieldGroup;
  
  @FXML
  private ComboBox<String> lengthFieldGroup;

  @FXML
  private ComboBox<String> nextFieldGroup;

  @FXML
  private ComboBox<String> lengthField;
  
  @FXML
  private ComboBox<String> nextField;
  
  @FXML
  private ComboBox<String> alignment;

  @FXML
  private TextField value;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    type.getItems().add(GroupField.Type.text.toString());
    type.getItems().add(GroupField.Type.number.toString());

    lengthFieldGroup.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

      }
    });
  }

  @Override
  public void fieldSelected(GroupField field) {
    ObservableList<String> fieldGroups = FXCollections
        .observableArrayList(field.getFieldGroup().getFormat().getFieldGroups().keySet());
    fieldGroup.getItems().addAll(fieldGroups);
    lengthFieldGroup.getItems().addAll(fieldGroups);
    nextFieldGroup.getItems().addAll(fieldGroups);

    lengthFieldGroup.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        lengthField.getItems().clear();
        lengthField.getItems().addAll(field.getFieldGroup().getFormat().getFieldGroups().get(newValue).getFields().keySet());
      }
    });
    
    nextFieldGroup.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        nextField.getItems().clear();
        nextField.getItems().addAll(field.getFieldGroup().getFormat().getFieldGroups().get(newValue).getFields().keySet());
      }
    });
    
    fieldGroup.getSelectionModel().select(field.getFieldGroup().getName());
    name.setText(field.getName());
    type.setValue(field.getType().toString());
    delimiter.setText(field.getDelimiter());
    alignment.setValue(field.getAlignment().toString());
    lengthValue.setText(field.getLength().getValue().toString());
    if (field.getNext() != null) {
      nextFieldGroup.setValue(field.getNext().getFieldGroup());
      nextField.setValue(field.getNext().getGroupField()); 
    }
    defaultValue.setText(field.getDefaultValue());
    
  }
}
