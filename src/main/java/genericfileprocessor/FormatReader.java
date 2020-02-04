package genericfileprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import genericfileprocessor.Field.Type;
import genericfileprocessor.SuperField.Alignment;
import genericfileprocessor.SuperField.NextField;
import genericfileprocessor.listener.FormatsListener;

@XmlRootElement(name = "formats")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormatReader {
  @XmlElement(name="format")
  private List<Format> formats = new ArrayList<Format>();

//  @XmlTransient
//  private Map<String, Format> formatMap = new HashMap<String, Format>();
  
  @XmlTransient
  private List<FormatsListener> formatsListeners = new ArrayList<FormatsListener>();

//  public Map<String, Format> getFormatMap() {
//    return formatMap;
//  }
  
  public List<Format> getFormats() {
    return formats;
  }
  
  public void setFormats(List<Format> formats) {
    this.formats = formats;
  }

  public void addFormatsListener(FormatsListener formatsListener) {
    formatsListeners.add(formatsListener);
  }

  public void removeFormatsListener(FormatsListener formatsListener) {
    formatsListeners.remove(formatsListener);
  }

  public void addFormatsFromConfig(String configFile) {
    try {
      File file = new File(configFile);
      JAXBContext jaxbContext = JAXBContext.newInstance(FormatReader.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      FormatReader formatReader = (FormatReader) unmarshaller.unmarshal(file);
      if (formatReader.getFormats() != null) {
        for (Format format : formatReader.getFormats()) {
          setFormatDefaults(format);
          formats.add(format);
          for (FormatsListener formatsListener : formatsListeners) {
            formatsListener.formatAdded(format);
          } 
        }
      }
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }
  
  public void setFormatDefaults(Format format) {
    Field previous = null;
    for (Field field : format.getFields()) {
      field.setFormat(format);

      if (field.getAlignment() == null) {
        if (format.getAlignment() != null) {
          field.setAlignment(format.getAlignment());
        } else {
          field.setAlignment(Alignment.left);
        }
      }

      if (field.getDelimiter() == null && format.getDelimiter() != null) {
        field.setDelimiter(format.getDelimiter());
      }

      if (field.getDefaultValue() == null && format.getDefaultValue() != null) {
        field.setDefaultValue(format.getDefaultValue());
      }

      if (field.getLength() == null && format.getLength() != null) {
        field.setLength(format.getLength());
      }

      if (field.getType() == null) {
        field.setType(Type.text);
      }

      if (previous != null) {
        if (previous.getNextFields() == null || previous.getNextFields().isEmpty()) {
          List<NextField> nexts = new ArrayList<NextField>();
          NextField next = new NextField();
          nexts.add(next);
          next.setField(field.getName());
          previous.setNextFields(nexts);
        }
      }
      previous = field;
    }
  }

  public void marshal(Map<String, Format> formats) {
    try {
      File file = new File(
          "C:\\Users\\carlowc\\Documents\\Projects\\dev\\dev\\formats\\formats\\resources\\config\\formats_new4.xml");
      JAXBContext jaxbContext = JAXBContext.newInstance(FormatReader.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      OutputStream out = new FileOutputStream(file);
      DOMResult domResult = new DOMResult();
      jaxbMarshaller.marshal(formats, domResult);

      // Transformer used to beautify xml output that is otherwise uglified by faulty indentations
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
      transformer.transform(new DOMSource(domResult.getNode()), new StreamResult(out));
    } catch (FileNotFoundException | TransformerFactoryConfigurationError
        | TransformerException e) {
      e.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    }

  }
}
