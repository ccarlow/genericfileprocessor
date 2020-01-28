package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import genericfileprocessor.GroupField;
import genericfileprocessor.FieldGroup;
import genericfileprocessor.Format;
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

    formatReader.addFormatsFromConfig("resources/config/formats/csv.xml");

    for (Entry<String, Format> entry : formatReader.getFormats().entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue().getType());
      for (Entry<String, FieldGroup> entry2 : entry.getValue().getFieldGroups().entrySet()) {
        System.out.println(entry2.getKey());

        for (Entry<String, GroupField> entry3 : entry2.getValue().getFields().entrySet()) {
          System.out.println(entry3.getKey());
          System.out.println(entry3.getValue().getType());
        }
      }
    }
  }

}
