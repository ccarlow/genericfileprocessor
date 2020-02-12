package genericfileprocessor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processor {
  
  //TODO: method to find by index (for netcdf reading but also text) 
  
  public void read(String input, Format format) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(input));
      StringBuilder value = new StringBuilder();
      StringBuilder delimiter = new StringBuilder();
      
      Map<String, List<Field>> fieldMap = new HashMap<String, List<Field>>();
      
      
      Field field = new Field(format.getFields().get(0));
      int glyph = -1;
//      Field field = new Field(format.getNextField());
      addToFieldMap(fieldMap, field);
//      field = format.getFields().get(field.getName());
      Field firstField  = field;//new Field(field);
      while ((glyph = reader.read()) != -1) {
        if (glyph == '\n') {
          continue;
        }
        delimiter.append((char)glyph);
        if (field.getDelimiter().indexOf(delimiter.toString()) >= 0) {
          if (field.getDelimiter().length() == delimiter.length()) {
            field.setDefaultValue(value.toString());
            value.setLength(0);
            delimiter.setLength(0);
            Field nextField = field.getNextField(fieldMap);
            if (nextField != null) {
              Field actualNextField = new Field(nextField);
              field.setActualNextField(actualNextField);
              field = actualNextField;
              addToFieldMap(fieldMap, field);
            } else {
              break;
            }
          }
        } else {
          value.append(delimiter);
          delimiter.setLength(0);
        }
      }
      field.setDefaultValue(value.toString());
      
      field = firstField;
      while (field != null) {
        System.out.println(field.getName() + " = " + field.getDefaultValue());
        field = field.getActualNextField();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void addToFieldMap(Map<String, List<Field>> fieldMap, Field field) {
    List<Field> fieldList = fieldMap.get(field.getName());
    if (fieldList == null) {
      fieldList = new ArrayList<Field>();
      fieldMap.put(field.getName(), fieldList);
    }
    fieldList.add(field);
  }
  
}
