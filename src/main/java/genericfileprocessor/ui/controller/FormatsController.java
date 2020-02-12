package genericfileprocessor.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import dnddockfx.DockPane;
import genericfileprocessor.Format;
import genericfileprocessor.Field;
import genericfileprocessor.listener.FormatFieldIndexListener;
import genericfileprocessor.listener.FormatListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class FormatsController implements Initializable, FormatListener, FormatFieldIndexListener {
  @FXML
  private TreeView<String> treeView;

  @FXML
  private DockPane formatsDockPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void loadProcessor() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    if (treeItem != null) {
      Object selectedItem = null;
      if (treeItem.getGraphic() != null && treeItem.getGraphic().getUserData() != null) {
        selectedItem = treeItem.getGraphic().getUserData();
      }
      if (selectedItem instanceof Format) {
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(
              getClass().getClassLoader().getResource("genericfileprocessor/fxml/Processor.fxml")); 
          Parent root = fxmlLoader.load();
          ((ProcessorController)fxmlLoader.getController()).setFormat((Format)selectedItem);
          DockPane dockPane = new DockPane(treeItem.getValue(), root);
          formatsDockPane.getDockPanes().add(dockPane);
          dockPane.show();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  public void openTreeItem() {
    TreeItem<String> treeItem = treeView.getSelectionModel().getSelectedItem();
    if (treeItem != null) {
      FXMLLoader fxmlLoader = null;
      Object selectedItem = null;
      if (treeItem.getGraphic() != null && treeItem.getGraphic().getUserData() != null) {
        selectedItem = treeItem.getGraphic().getUserData();
      }
      if (selectedItem instanceof Format) {
        fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("genericfileprocessor/fxml/Format.fxml")); 
      } else if (selectedItem instanceof Field) {
        fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("genericfileprocessor/fxml/Field.fxml"));
      }

      if (fxmlLoader != null) {
        try {
          DockPane dockPane = fxmlLoader.load();
          dockPane.setId(dockPane.getId() + UUID.randomUUID());
          formatsDockPane.getGroupDockPane().getDockPanes().add(dockPane);
          formatsDockPane.getGroupDockPane().getDockManager().addDockPane(dockPane);
          dockPane.show();
          if (selectedItem instanceof Format) {
            FormatController controller = fxmlLoader.getController();
            controller.formatSelected((Format)selectedItem); 
          } else if (selectedItem instanceof Field) {
            FieldController controller = fxmlLoader.getController();
            controller.fieldSelected((Field) selectedItem);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public TreeItem<String> getFormatTreeItem(Format format) {
    for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
      if (treeItem.getGraphic().getUserData().equals(format)) {
        return treeItem;
      }
    }
    return null;
  }

  public TreeItem<String> getFieldTreeItem(TreeItem<String> formatTreeItem, Field field) {
    for (TreeItem<String> treeItem : formatTreeItem.getChildren()) {
      if (treeItem.getGraphic().getUserData().equals(field)) {
        return treeItem;
      }
    }
    return null;
  }

  @Override
  public void formatAdded(Format format) {
    format.addFormatFieldIndexListener(this);
    Region region = new Region();
    region.setUserData(format);
    TreeItem<String> formatTreeItem = new TreeItem<String>(format.getName(), region);
    formatTreeItem.setExpanded(true);   
    treeView.getRoot().getChildren().add(formatTreeItem);
    
    int index = 0;
    for (Field field : format.getFieldsCopy()) {
      fieldIndexChanged(field, index++);
    }
    
    format.addFormatListener(this);
  }

  @Override
  public void fieldIndexChanged(Field field, int index) {
    TreeItem<String> formatTreeItem = getFormatTreeItem(field.getFormat());
    TreeItem<String> fieldTreeItem = getFieldTreeItem(formatTreeItem, field);

    if (fieldTreeItem == null) {
      Region region = new Region();
      region.setUserData(field);
      fieldTreeItem = new TreeItem<String>(field.getName(), region);
    } else {
      formatTreeItem.getChildren().remove(fieldTreeItem);
    }
    
    formatTreeItem.getChildren().add(index, fieldTreeItem);
    treeView.getSelectionModel().select(fieldTreeItem);
  }
  
//  @Override
//  public void fieldAdded(Field field) {
//    TreeItem treeItem = getFormatTreeItem(field.getFormat());
//    Region region = new Region();
//    region.setUserData(field);
//    TreeItem<String> fieldTreeItem = new TreeItem<String>(field.getName(), region);
//    treeItem.getChildren().add(fieldTreeItem);
//  }

}
