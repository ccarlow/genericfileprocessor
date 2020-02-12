package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import genericfileprocessor.SuperField.Condition;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.SuperField.NextFieldCondition;
import genericfileprocessor.listener.NextFieldConditionSelectorListener;
import genericfileprocessor.listener.NextFieldListener;
import genericfileprocessor.listener.NextFieldSelectorListener;
import genericfileprocessor.ui.NextFieldUiHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class NextFieldConditionController implements Initializable, NextFieldSelectorListener, NextFieldConditionSelectorListener, NextFieldListener {
  @FXML
  private ComboBox<Condition.Operator> operator;
  
  @FXML
  private TreeView<String> parentCondition;
  
  private NextField nextField;
  private NextFieldCondition condition;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (Condition.Operator operator : Condition.Operator.values()) {
      this.operator.getItems().add(operator);
    }
  }

  public void applyChanges() {
    if (condition == null) {
      condition = new NextFieldCondition();
      nextField.setCondition(condition);
    }
    TreeItem<String> treeItem = parentCondition.getSelectionModel().getSelectedItem();
    if (treeItem.getGraphic() != null && treeItem.getGraphic().getUserData() != null) {
      ((NextFieldCondition)treeItem.getGraphic().getUserData()).addChildCondition(condition);
    }
    condition.setOperator(operator.getSelectionModel().getSelectedItem());
  }

  @Override
  public void nextFieldConditionSelected(NextFieldCondition condition) {
    this.nextField = condition.getNextField();
    nextField.addNextFieldListener(this);
    this.condition = condition;
    this.operator.getSelectionModel().select(condition.getOperator());
    
    NextFieldUiHelper.setNextFieldCondition(nextField.getCondition(), parentCondition.getRoot());
  }

  @Override
  public void nextFieldSelected(NextField nextField) {
    this.nextField = nextField;
    nextField.addNextFieldListener(this);
  }

  @Override
  public void nextFieldAdded(NextField nextField) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void nextFieldConditionAdded(NextFieldCondition condition) {
    NextFieldUiHelper.setNextFieldCondition(condition, (TreeItem<String>)parentCondition.getRoot());
  }

  @Override
  public void nextFieldConditionRemoved(NextFieldCondition condition) {
    // TODO Auto-generated method stub
    
  }
}
