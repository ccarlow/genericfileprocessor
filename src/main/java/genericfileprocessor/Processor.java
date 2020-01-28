package genericfileprocessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import genericfileprocessor.SuperField.Next;

public class Processor {
  
  //TODO: method to find by index (for netcdf reading but also text) 
  
  public void read(String input, Format format) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(input));
      StringBuilder value = new StringBuilder();
      StringBuilder delimiter = new StringBuilder();
      int charCount = 0;
      
      List<FieldGroup> fieldGroups = new ArrayList<FieldGroup>();
      
      int glyph = -1;
      GroupField groupField = format.getNextGroupField();
      FieldGroup fieldGroup = new FieldGroup(groupField.getFieldGroup());
      groupField = fieldGroup.getFields().get(groupField.getName());
      fieldGroups.add(fieldGroup);
      while ((glyph = reader.read()) != -1) {
        delimiter.append((char)glyph);
        if (groupField.getDelimiter().indexOf(delimiter.toString()) >= 0) {
          if (groupField.getDelimiter().length() == delimiter.length()) {
            groupField.setDefaultValue(value.toString());
            System.out.println(value.toString());
            value.setLength(0);
            delimiter.setLength(0);
            if (groupField.getNextGroupField() == null) {
              fieldGroup = new FieldGroup(groupField.getFieldGroup()); 
              fieldGroups.add(fieldGroup);
              groupField = fieldGroup.getFields().get(groupField.getName());
            }
          }
        } else {
          value.append(delimiter);
          delimiter.setLength(0);
        }
        
        charCount++;
      }
      
      
      for (FieldGroup fieldGroup2 : fieldGroups) {
        for (GroupField groupField2 : fieldGroup2.getFields().values()) {
          System.out.println(groupField2.getName() + " = " + groupField2.getDefaultValue());
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
