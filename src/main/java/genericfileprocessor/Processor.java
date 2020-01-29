package genericfileprocessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Processor {
  
  //TODO: method to find by index (for netcdf reading but also text) 
  
  public void read(String input, Format format) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(input));
      StringBuilder value = new StringBuilder();
      StringBuilder delimiter = new StringBuilder();
      
      List<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
      
      int glyph = -1;
      GroupField groupField = format.getNextGroupField();
      FieldGroup fieldGroup = new FieldGroup(groupField.getFieldGroup());
      groupField = fieldGroup.getFields().get(groupField.getName());
      fieldGroups.add(fieldGroup);
      while ((glyph = reader.read()) != -1) {
        if (glyph == '\n') {
          continue;
        }
        delimiter.append((char)glyph);
        if (groupField.getDelimiter().indexOf(delimiter.toString()) >= 0) {
          if (groupField.getDelimiter().length() == delimiter.length()) {
            groupField.setDefaultValue(value.toString());
            value.setLength(0);
            delimiter.setLength(0);
            if (groupField.getNextGroupField() != null) {
              GroupField nextField = groupField.getNextGroupField();
              if (!nextField.getFieldGroup().getName().equals(groupField.getFieldGroup().getName())) {
                fieldGroup = new FieldGroup(nextField.getFieldGroup()); 
                fieldGroups.add(fieldGroup);
              }
              groupField = fieldGroup.getFields().get(nextField.getName());
            } else {
              break;
            }
          }
        } else {
          value.append(delimiter);
          delimiter.setLength(0);
        }
      }
      groupField.setDefaultValue(value.toString());
      
      for (FieldGroup fieldGroup2 : fieldGroups) {
        for (GroupField groupField2 : fieldGroup2.getFields().values()) {
          System.out.println(fieldGroup2.getName() + "." + groupField2.getName() + " = " + groupField2.getDefaultValue());
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
