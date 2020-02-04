package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import dnddockfx.DockPane;
import genericfileprocessor.Format;
import genericfileprocessor.listener.FormatSelectorListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class FormatController implements Initializable, FormatSelectorListener {
  @FXML
  private ComboBox<Format.Type2> type;
  
  @FXML
  private DockPane formatDockPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    for (Format.Type2 type : Format.Type2.values()) {
      this.type.getItems().add(type);
    }
  }

  @Override
  public void formatSelected(Format format) {
    if (format.getType() != null) {
      this.type.getSelectionModel().select(format.getType()); 
    }
  }
}
