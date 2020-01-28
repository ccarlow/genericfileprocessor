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
import genericfileprocessor.SuperField.Next;
import genericfileprocessor.listener.FormatsListener;

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormatReader {
  private Map<String, Format> formats = new HashMap<String, Format>();

  @XmlTransient
  private List<FormatsListener> formatsListeners = new ArrayList<FormatsListener>();

  public Map<String, Format> getFormats() {
    return formats;
  }

  public void setFormats(Map<String, Format> formats) {
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
      unmarshaller.setListener(new Unmarshaller.Listener() {
        public void afterUnmarshal(Object target, Object parent) {
          if (target instanceof Format) {
            Format format = (Format) target;
            GroupField firstGroupField = null;
            String key = (String) ((Object[]) parent)[0];
            for (FormatsListener formatsListener : formatsListeners) {
              formatsListener.formatAdded(key, format);
              GroupField previous = null;
              for (Entry<String, FieldGroup> entry : format.getFieldGroups().entrySet()) {
                FieldGroup fieldGroup = entry.getValue();
                formatsListener.fieldGroupAdded(key, entry.getKey(), fieldGroup);
                fieldGroup.setFormat(format);
                fieldGroup.setName(entry.getKey());
                for (Entry<String, GroupField> entry2 : fieldGroup.getFields().entrySet()) {
                  GroupField groupField = entry2.getValue();
                  formatsListener.fieldAdded(key, entry.getKey(), entry2.getKey(), groupField);
                  groupField.setFieldGroup(fieldGroup);
                  groupField.setName(entry2.getKey());

                  if (groupField.getAlignment() == null) {
                    if (fieldGroup.getAlignment() != null) {
                      groupField.setAlignment(fieldGroup.getAlignment());
                    } else {
                      groupField.setAlignment(Alignment.left);
                    }
                  }

                  if (groupField.getDelimiter() == null && fieldGroup.getDelimiter() != null) {
                    groupField.setDelimiter(fieldGroup.getDelimiter());
                  }

                  if (groupField.getDefaultValue() == null
                      && fieldGroup.getDefaultValue() != null) {
                    groupField.setDefaultValue(fieldGroup.getDefaultValue());
                  }

                  if (groupField.getLength() == null && fieldGroup.getLength() != null) {
                    groupField.setLength(fieldGroup.getLength());
                  }

                  if (groupField.getType() == null) {
                    if (fieldGroup.getType() != null) {
                      groupField.setType(fieldGroup.getType());
                    } else {
                      groupField.setType(Type.text);
                    }
                  }

                  if (previous != null) {
                    Next next = previous.getNext();
                    if (next == null) {
                      next = new Next();
                      next.setFieldGroup(groupField.getFieldGroup().getName());
                      next.setGroupField(groupField.getName());
                      previous.setNext(next);
                    }

                    if (format.getNext() == null) {
                      format.setNext(next);
                    }
                  }
                  previous = groupField;
                }
              }
            }

            if (format.getNext() == null) {
              Next next = new Next();
              FieldGroup fieldGroup = format.getFieldGroups().values().iterator().next();
              GroupField groupField = fieldGroup.getFields().values().iterator().next();
              next.setFieldGroup(fieldGroup.getName());
              next.setGroupField(groupField.getName());
              format.setNext(next);
            }
          }
        }
      });
      FormatReader formatReader = (FormatReader) unmarshaller.unmarshal(file);
      formats.putAll(formatReader.getFormats());
    } catch (JAXBException e) {
      e.printStackTrace();
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
