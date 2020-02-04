package genericfileprocessor.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import genericfileprocessor.Format;
import genericfileprocessor.Field;
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
          formatsDockPane.getDockPanes().add(dockPane);
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

  public TreeItem<String> getFormatTreeItem(String format) {
    for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
      if (treeItem.getValue().equals(format)) {
        return treeItem;
      }
    }
    return null;
  }


  @Override
  public void formatAdded(Format format) {
    Region region = new Region();
    region.setUserData(format);
    TreeItem<String> formatTreeItem = new TreeItem<String>(format.getName(), region);
    formatTreeItem.setExpanded(true);
    treeView.getRoot().getChildren().add(formatTreeItem);
    
    for (Field field : format.getFields()) {
      region = new Region();
      region.setUserData(field);
      TreeItem<String> fieldTreeItem = new TreeItem<String>(field.getName(), region);
      formatTreeItem.getChildren().add(fieldTreeItem);
    }
  }

//  @Override
//  public void fieldAdded(String format, Field field) {
//    TreeItem<String> formatTreeItem = getFormatTreeItem(format);
//    
//    Region region = new Region();
//    region.setUserData(field);
//    TreeItem<String> treeItem = new TreeItem<String>(field.getName(), region);
//    formatTreeItem.getChildren().add(treeItem);
//  }
}
