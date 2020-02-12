package genericfileprocessor.ui;

import java.io.IOException;
import genericfileprocessor.Field;
import genericfileprocessor.SuperField.Condition;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.SuperField.NextFieldCondition;
import genericfileprocessor.ui.controller.NextFieldConditionController;
import genericfileprocessor.ui.controller.NextFieldController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;

public class NextFieldUiHelper {
  
  public static final String CONDITION_TREE_ITEM = "Condition";

  public static void setNextFieldCondition(Condition condition, TreeItem<String> parentItem) {
    TreeItem<String> conditionTreeItem = getConditionTreeItem(condition, parentItem);
    if (conditionTreeItem == null) {
      Region region = new Region();
      region.setUserData(condition);
      conditionTreeItem = new TreeItem<String>("", region);
      conditionTreeItem.setExpanded(true);
      parentItem.getChildren().add(conditionTreeItem); 
    }
    String conditionValue = getConditionValue(condition);
    conditionTreeItem.setValue(conditionValue);
    
    if (condition.getConditionsCopy() != null) {
      for (Condition childCondition : condition.getConditionsCopy()) {
        setNextFieldCondition(childCondition, conditionTreeItem);
      }
    }
  }
  
  public static String getConditionValue(Condition condition) {
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
    return conditionValue;
  }
  
  public static TreeItem<String> getNextFieldTreeItem(NextField nextField, TreeItem<String> parentItem) {
    for (Object child : parentItem.getChildren()) {
      TreeItem<String> treeItem = (TreeItem<String>)child;
      if (treeItem.getGraphic() != null) {
        Object userData = treeItem.getGraphic().getUserData();
        if (userData.equals(nextField)) {
          return treeItem;
        }
      }
    }
    return null;
  }
  
  public static TreeItem<String> getConditionTreeItem(Condition condition, TreeItem<String> parentTreeItem) {
    for (TreeItem<String> treeItem : parentTreeItem.getChildren()) {
      if (treeItem.getGraphic().getUserData().equals(condition)) {
        return treeItem;
      }
    }
    
    for (TreeItem<String> treeItem : parentTreeItem.getChildren()) {
      if (treeItem.getGraphic().getUserData().equals(condition)) {
        return getConditionTreeItem(condition, treeItem);
      }
    }
    
    return null;
  }
  
  public static void setNextField(NextField nextField, TreeItem<String> parentTreeItem) {
//    nextField.addFieldRefListener(this);
    Region region = new Region();
    region.setUserData(nextField);
    TreeItem<String> treeItem = new TreeItem<String>(nextField.getFieldRef(), region);
    treeItem.setExpanded(true);
    parentTreeItem.getChildren().add(treeItem);
    
    region = new Region();
    region.setUserData(nextField);
    TreeItem<String> conditionItem = new TreeItem<String>(CONDITION_TREE_ITEM, region);
    conditionItem.setExpanded(true);
    treeItem.getChildren().add(conditionItem);
    
//    nextField.addNextFieldListener(this);
  }
  
  public static Parent getNextFieldControl(TreeItem<String> parentTreeItem, Field field) {
    NextField nextField = null;
    NextFieldCondition condition = null;
    if (parentTreeItem.getGraphic() != null) {
      Object userData = parentTreeItem.getGraphic().getUserData();
      if (userData instanceof NextField) {
        nextField = (NextField)userData;
      } else if (userData instanceof NextFieldCondition) {
        condition = (NextFieldCondition)userData;
        nextField = condition.getNextField();
      }
    }
    
    FXMLLoader fxmlLoader = null; 
    if (condition != null || parentTreeItem.getValue().equals(NextFieldUiHelper.CONDITION_TREE_ITEM)) {
      fxmlLoader = new FXMLLoader(NextFieldUiHelper.class.getClassLoader().getResource("genericfileprocessor/fxml/NextFieldCondition.fxml"));
    } else {
      fxmlLoader = new FXMLLoader(NextFieldUiHelper.class.getClassLoader().getResource("genericfileprocessor/fxml/NextField.fxml"));
    }
    Parent root = null;;
    try {
      root = fxmlLoader.load();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    
    if (root != null) {
      if (fxmlLoader.getController() instanceof NextFieldController) {
        if (nextField == null) {
          ((NextFieldController)fxmlLoader.getController()).fieldSelected(field); 
        } else {
          ((NextFieldController)fxmlLoader.getController()).nextFieldSelected(nextField);
        }
      } else if (fxmlLoader.getController() instanceof NextFieldConditionController) {
        if (condition == null) {
          ((NextFieldConditionController)fxmlLoader.getController()).nextFieldSelected(nextField); 
        } else {
          ((NextFieldConditionController)fxmlLoader.getController()).nextFieldConditionSelected(condition);
        }
      }
    }
    return root;
  }
  
//  public void set(NextField nextField, TreeItem<String> parentItem) {
//    Region region = new Region();
//    region.setUserData(nextField);
//    TreeItem conditionsItem = new TreeItem(CONDITION_TREE_ITEM, region);
//    conditionsItem.setExpanded(true);
//    parentItem.getChildren().add(conditionsItem);
//    
//    if (nextField.getCondition() != null) {
//      setNextFieldCondition(nextField.getCondition(), conditionsItem);
//    }
//  }
}
