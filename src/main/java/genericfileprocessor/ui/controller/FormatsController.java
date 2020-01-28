package genericfileprocessor.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import genericfileprocessor.GroupField;
import genericfileprocessor.Processor;
import genericfileprocessor.FieldGroup;
import genericfileprocessor.Format;
import genericfileprocessor.listener.FormatsListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class FormatsController implements Initializable, FormatsListener {
  @FXML
  private TreeView<String> treeView;

  @FXML
  private DockPane formatsDockPane;

  private String FIELD_GROUPS_TREE_ITEM = "Field Groups2";
  private String FIELDS_TREE_ITEM = "Fields";
  private String PROCESSOR_TREE_ITEM = "Processor";

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void openTreeItem() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    if (treeItem != null) {
      FXMLLoader fxmlLoader = null;
      Object selectedItem = null;
      if (treeItem.getGraphic() != null && treeItem.getGraphic().getUserData() != null) {
        selectedItem = treeItem.getGraphic().getUserData();
      }
      if (selectedItem instanceof Format) {
        if (treeItem.getValue().contentEquals(PROCESSOR_TREE_ITEM)) {
          fxmlLoader = new FXMLLoader(
              getClass().getClassLoader().getResource("genericfileprocessor/fxml/Processor.fxml"));
        } else {
          fxmlLoader = new FXMLLoader(
              getClass().getClassLoader().getResource("genericfileprocessor/fxml/Format.fxml")); 
        }
      } else if (selectedItem instanceof FieldGroup) {
        fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("genericfileprocessor/fxml/FieldGroup.fxml"));
      } else if (selectedItem instanceof GroupField) {
        fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("genericfileprocessor/fxml/Field.fxml"));
      }

      if (fxmlLoader != null) {
        try {
          Parent root = fxmlLoader.load();
          DockPane dockPane = new DockPane(treeItem.getValue(), root);
          formatsDockPane.getDockPanes().add(dockPane);
          dockPane.show();
          if (selectedItem instanceof Format) {
            if (treeItem.getValue().contentEquals(PROCESSOR_TREE_ITEM)) {
              ProcessorController controller = fxmlLoader.getController();
              controller.setFormat((Format)selectedItem); 
            }
          } else if (selectedItem instanceof FieldGroup) {
          } else if (selectedItem instanceof GroupField) {
            FieldController controller = fxmlLoader.getController();
            controller.fieldSelected((GroupField) selectedItem);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public TreeItem<String> getFormatTreeItem(String format) {
    for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
      if (treeItem.getValue().equals(format)) {
        return treeItem;
      }
    }
    return null;
  }

  public TreeItem<String> getFieldGroupsTreeItem(String format) {
    TreeItem<String> treeItem = getFormatTreeItem(format);
    if (treeItem != null) {
      for (TreeItem<String> treeItem2 : treeItem.getChildren()) {
        if (treeItem2.getValue().equals(FIELD_GROUPS_TREE_ITEM)) {
          return treeItem2;
        }
      }
    }
    return null;
  }

  public TreeItem<String> getFieldGroupTreeItem(String format, String fieldGroup) {
    TreeItem<String> treeItem = getFieldGroupsTreeItem(format);
    if (treeItem != null) {
      if (treeItem != null) {
        for (TreeItem<String> treeItem2 : treeItem.getChildren()) {
          if (treeItem2.getValue().equals(fieldGroup)) {
            return treeItem2;
          }
        }
      }
    }
    return null;
  }

  public TreeItem<String> getFieldsTreeItem(String format, String fieldGroup) {
    TreeItem<String> treeItem = getFieldGroupTreeItem(format, fieldGroup);
    if (treeItem != null) {
      for (TreeItem<String> treeItem2 : treeItem.getChildren()) {
        if (treeItem2.getValue().equals(FIELDS_TREE_ITEM)) {
          return treeItem2;
        }
      }
    }
    return null;
  }

  public TreeItem<String> getFieldTreeItem(String format, String fieldGroup, String field) {
    TreeItem<String> treeItem = getFieldsTreeItem(format, fieldGroup);
    if (treeItem != null) {
      for (TreeItem<String> treeItem2 : treeItem.getChildren()) {
        if (treeItem2.getValue().equals(fieldGroup)) {
          return treeItem2;
        }
      }
    }
    return null;
  }

  @Override
  public void formatAdded(String key, Format format) {
    Region region = new Region();
    region.setUserData(format);
    TreeItem<String> treeItem = new TreeItem<String>(key, region);
    treeItem.setExpanded(true);
    treeView.getRoot().getChildren().add(treeItem);

    TreeItem<String> treeItem2 = new TreeItem<String>(FIELD_GROUPS_TREE_ITEM);
    treeItem2.setExpanded(true);
    treeItem.getChildren().add(treeItem2);
    
    treeItem2 = new TreeItem<String>(PROCESSOR_TREE_ITEM, region);
    treeItem2.setExpanded(true);
    treeItem.getChildren().add(treeItem2);
  }

  @Override
  public void fieldGroupAdded(String format, String key, FieldGroup fieldGroup) {
    if (getFieldGroupTreeItem(format, key) == null) {
      TreeItem<String> parentTreeItem = getFieldGroupsTreeItem(format);
      if (parentTreeItem != null) {
        Region region = new Region();
        region.setUserData(fieldGroup);
        TreeItem<String> treeItem = new TreeItem<String>(key, region);
        treeItem.setExpanded(true);
        parentTreeItem.getChildren().add(treeItem);

        TreeItem<String> treeItem2 = new TreeItem<String>(FIELDS_TREE_ITEM);
        treeItem2.setExpanded(true);
        treeItem.getChildren().add(treeItem2);
      }
    }
  }

  @Override
  public void fieldAdded(String format, String fieldGroup, String key, GroupField field) {
    if (getFieldTreeItem(format, fieldGroup, key) == null) {
      TreeItem<String> parentTreeItem = getFieldsTreeItem(format, fieldGroup);
      if (parentTreeItem != null) {
        Region region = new Region();
        region.setUserData(field);
        TreeItem<String> treeItem = new TreeItem<String>(key, region);
        parentTreeItem.getChildren().add(treeItem);
      }
    }
  }
}
