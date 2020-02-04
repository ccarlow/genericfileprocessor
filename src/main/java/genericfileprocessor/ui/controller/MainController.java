package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import genericfileprocessor.FormatReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable {

  @FXML
  private FormatsController formatsController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    FormatReader formatReader = new FormatReader();

    formatReader.addFormatsListener(formatsController);

//    formatReader.addFormatsFromConfig("resources/config/formats/moods.xml");
    formatReader.addFormatsFromConfig("resources/config/formats/csv.xml");
  }

}
