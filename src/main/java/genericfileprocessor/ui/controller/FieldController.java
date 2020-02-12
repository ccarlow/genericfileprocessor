package genericfileprocessor.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import genericfileprocessor.Field;
import genericfileprocessor.Format;
import genericfileprocessor.SuperField;
import genericfileprocessor.SuperField.Condition;
import genericfileprocessor.SuperField.FieldRef;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.SuperField.NextFieldCondition;
import genericfileprocessor.listener.FieldRefListener;
import genericfileprocessor.listener.FieldSelectorListener;
import genericfileprocessor.listener.FormatFieldIndexListener;
import genericfileprocessor.listener.NextFieldListener;
import genericfileprocessor.ui.NextFieldUiHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FieldController implements Initializable, FieldSelectorListener, FormatFieldIndexListener, NextFieldListener, FieldRefListener {
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
  private ComboBox<Field.Type> type;

  @FXML
  private ComboBox<String> lengthField;
  
  @FXML
  private ComboBox<String> indexField;
  
  @FXML
  private ComboBox<SuperField.Alignment> alignment;

  @FXML
  private TextField value;
  
  @FXML
  private TreeView nextFields;
  
  @FXML
  private VBox nextFieldContainer;
  
  private Field field;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (Field.Type type : Field.Type.values()) {
      this.type.getItems().add(type);
    }
    
    for (SuperField.Alignment alignment : SuperField.Alignment.values()) {
      this.alignment.getItems().add(alignment);
    }
  }
  
  @Override
  public void fieldSelected(Field field) {
    this.field = field;
    field.getFormat().addFormatFieldIndexListener(this);
    name.setText(field.getName());
    type.setValue(field.getType());
    delimiter.setText(field.getDelimiter());
    alignment.setValue(field.getAlignment());
    if (field.getLength() != null) {
      lengthValue.setText(field.getLength().getValue().toString()); 
    }
    if (field.getNextFieldsCopy() != null) { 
      for (NextField nextField : field.getNextFieldsCopy()) {
        nextFieldAdded(nextField);
      }
    }
    defaultValue.setText(field.getDefaultValue());
    setFieldComboBoxes();
    field.addNextFieldListener(this);
  }
  
  public void setFieldComboBoxes() {
    indexField.getItems().clear();
    lengthField.getItems().clear();
    Field previousField = null;
    for (Field formatField : this.field.getFormat().getFieldsCopy()) {
      if (!formatField.equals(this.field)) {
        indexField.getItems().add(formatField.getName());
        lengthField.getItems().add(formatField.getName());
      } else if (previousField != null) {
        indexField.getSelectionModel().select(previousField.getName()); 
      }
      previousField = formatField;
    }
  }
  
  public void openNextItem() {
    TreeItem treeItem = (TreeItem) nextFields.getSelectionModel().getSelectedItem();
    Parent root = NextFieldUiHelper.getNextFieldControl(treeItem, field);
    
//    NextField nextField = null;
//    NextFieldCondition condition = null;
//    if (treeItem.getGraphic() != null) {
//      Object userData = treeItem.getGraphic().getUserData();
//      if (userData instanceof NextField) {
//        nextField = (NextField)userData;
//      } else if (userData instanceof NextFieldCondition) {
//        condition = (NextFieldCondition)userData;
//        nextField = condition.getNextField();
//      }
//    }
//    
//    FXMLLoader fxmlLoader = null; 
//    if (condition != null || treeItem.getValue().equals(NextFieldUiHelper.CONDITION_TREE_ITEM)) {
//      fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("genericfileprocessor/fxml/NextFieldCondition.fxml"));
//    } else {
//      fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("genericfileprocessor/fxml/NextField.fxml"));
//    }
//    Parent root = null;;
//    try {
//      root = fxmlLoader.load();
//    } catch (IOException e1) {
//      e1.printStackTrace();
//    }
    
    if (root != null) {
//      if (fxmlLoader.getController() instanceof NextFieldController) {
//        if (nextField == null) {
//          ((NextFieldController)fxmlLoader.getController()).fieldSelected(field); 
//        } else {
//          ((NextFieldController)fxmlLoader.getController()).nextFieldSelected(nextField);
//        }
//      } else if (fxmlLoader.getController() instanceof NextFieldConditionController) {
//        if (condition == null) {
//          ((NextFieldConditionController)fxmlLoader.getController()).nextFieldSelected(nextField); 
//        } else {
//          ((NextFieldConditionController)fxmlLoader.getController()).nextFieldConditionSelected(condition);
//        }
//      }
      
      DockPane dockPane = new DockPane((String)treeItem.getValue(), root);
      fieldDockPane.getDockPanes().add(dockPane);
      dockPane.show();
    }
  }
  
  public void setAsFirstField() {
    
  }
  
  public void saveAsNew() {
    applyChanges(new Field(), field.getFormat());
  }
  
  public void applyChanges() {
    applyChanges(field, field.getFormat());
  }
  
  public void applyChanges(Field field, Format format) {
    field.setName(name.getText());
    field.setType(type.getSelectionModel().getSelectedItem());
//    field.setFormat(format);
    format.setFieldIndexAfter(field, indexField.getValue());
  }

  @Override
  public void fieldIndexChanged(Field field, int index) {
    setFieldComboBoxes();
  }

  @Override
  public void nextFieldAdded(NextField nextField) {
    nextField.addFieldRefListener(this);
    nextField.addNextFieldListener(this);
    NextFieldUiHelper.setNextField(nextField, nextFields.getRoot());
//    Region region = new Region();
//    region.setUserData(nextField);
//    TreeItem treeItem = new TreeItem(nextField.getFieldRef(), region);
//    treeItem.setExpanded(true);
//    nextFields.getRoot().getChildren().add(treeItem);
    
    
  }
  
  @Override
  public void fieldRefChanged(FieldRef fieldRef) {
    if (fieldRef instanceof NextField) {
      TreeItem<String> treeItem = NextFieldUiHelper.getNextFieldTreeItem((NextField)fieldRef, nextFields.getRoot());
      treeItem.setValue(fieldRef.getFieldRef());
    }
  }

  @Override
  public void nextFieldConditionAdded(NextFieldCondition condition) {
//    TreeItem<String> nextFieldTreeItem = getNextFieldTreeItem(condition.getNextField());
//    TreeItem<String> conditionTreeRoot = nextFieldTreeItem.getChildren().get(0);
//    if (condition.getParentCondition() != null) {
//      conditionTreeRoot = getConditionTreeItem(conditionTreeRoot, condition.getParentCondition());
//    }
    NextFieldUiHelper.setNextFieldCondition(condition, (TreeItem<String>)nextFields.getRoot());
  }

  @Override
  public void nextFieldConditionRemoved(NextFieldCondition condition) {
    // TODO Auto-generated method stub
    
  }

//  @Override
//  public void nextFieldConditionOperatorAdded(NextFieldCondition condition) {
////    TreeItem<String> nextFieldTreeItem = getNextFieldTreeItem(condition.getNextField());
////    TreeItem<String> conditionTreeRoot = nextFieldTreeItem.getChildren().get(0);
////    TreeItem<String> conditionTreeItem = getConditionTreeItem(conditionTreeRoot, condition);
////    conditionTreeItem.setValue(getConditionValue(condition));
//  }
}
