package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import genericfileprocessor.GroupField;
import genericfileprocessor.FieldGroup;
import genericfileprocessor.Format;
import genericfileprocessor.listener.FormatsListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Region;

public class FormatController implements Initializable {
  @FXML
  private ComboBox<String> type;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    type.getItems().add("text");
    type.getItems().add("netcdf");
  }
}
