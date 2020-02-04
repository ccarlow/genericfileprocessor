package genericfileprocessor.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import genericfileprocessor.Format;
import genericfileprocessor.FormatReader;
import genericfileprocessor.Processor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ProcessorController implements Initializable {
  @FXML
  private ComboBox<String> formats;
  
  @FXML
  private TextField input;
  
  private FormatReader formatReader;
  
  private Processor processor = new Processor();
  
  private Format format;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
  }
  
  public void setFormatReader(FormatReader formatReader) {
    this.formatReader = formatReader;
    for (Format format : formatReader.getFormats()) {
      formats.getItems().add(format.getName());
    }
  }
  
  public void setFormat(Format format) {
    this.format = format;
  }

  public void process() {
    processor.read(input.getText(), format);
  }
}
