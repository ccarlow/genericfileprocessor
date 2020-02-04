package genericfileprocessor.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockManager;
import dnddockfx.DockPane;
import genericfileprocessor.Field;
import genericfileprocessor.Format;
import genericfileprocessor.SuperField.Condition;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.listener.FieldSelectorListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FieldController implements Initializable, FieldSelectorListener {
  @FXML
  private DockPane fieldDockPane;
  
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
  private ComboBox<String> lengthField;
  
  @FXML
  private ComboBox<String> nextField;
  
  @FXML
  private ComboBox<String> alignment;

  @FXML
  private TextField value;
  
  @FXML
  private TreeView nextFields;
  
  @FXML
  private VBox nextFieldContainer;
  
  private Field field;

  public static final String CONDITIONS_TREE_ITEM = "Conditions";
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (Field.Type type : Field.Type.values()) {
      this.type.getItems().add(type.toString());
    }
  }
  
  @Override
  public void fieldSelected(Field field) {
    this.field = field;
    name.setText(field.getName());
    type.setValue(field.getType().toString());
    delimiter.setText(field.getDelimiter());
    alignment.setValue(field.getAlignment().toString());
    if (field.getLength() != null) {
      lengthValue.setText(field.getLength().getValue().toString()); 
    }
    if (field.getNextFields() != null) { 
      for (NextField nextField : field.getNextFields()) {
        TreeItem treeItem = new TreeItem(nextField.getField());
        treeItem.setExpanded(true);
        nextFields.getRoot().getChildren().add(treeItem);
        
        TreeItem conditionsItem = new TreeItem(CONDITIONS_TREE_ITEM);
        conditionsItem.setExpanded(true);
        treeItem.getChildren().add(conditionsItem);
        
        if (nextField.getCondition() != null) {
          setNextFieldCondition(nextField.getCondition(), conditionsItem);
        }
      }
    }
    defaultValue.setText(field.getDefaultValue());
  }
  
  public void openNextItem() {
    TreeItem treeItem = (TreeItem) nextFields.getSelectionModel().getSelectedItem();
    if (treeItem.getGraphic() != null) {
      Object userData = treeItem.getGraphic().getUserData();
      if (userData instanceof Condition) {
        Condition condition = (Condition)userData;
        
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("genericfileprocessor/fxml/NextFieldCondition.fxml")); 
        try {
          Parent root = fxmlLoader.load();
          ((NextFieldConditionController)fxmlLoader.getController()).fieldSelected(field);
          DockPane dockPane = new DockPane((String)treeItem.getValue(), root);
          fieldDockPane.getDockPanes().add(dockPane);
          dockPane.show();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  public void setNextFieldCondition(Condition condition, TreeItem parentItem) {
    String conditionValue = "";
    if (condition.getField() != null) {
      conditionValue += condition.getField();
    }
    
    if (condition.getOperator() != null) {
      conditionValue += condition.getOperator();
    }
    
    if (condition.getValue() != null) {
      conditionValue += condition.getValue();
    }
    
    Region region = new Region();
    region.setUserData(condition);
    TreeItem treeItem = new TreeItem(conditionValue, region);
    treeItem.setExpanded(true);
    parentItem.getChildren().add(treeItem);
    
    if (condition.getConditions() != null) {
      for (Condition childCondition : condition.getConditions()) {
        setNextFieldCondition(childCondition, treeItem);
      }
    }
  }
  
}
