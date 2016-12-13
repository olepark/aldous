package org.dcn.aldous.database;

import org.dcn.aldous.database.items.Item;

import java.lang.reflect.Field;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class SerializingUtil {

  private static final String delimiter = "/,/";

  public static String serialize(Field f, Object obj) {
    String val = "";
    f.setAccessible(true);
    try {
      Object o = f.get(obj);
      f.setAccessible(false);
      if (o instanceof String) {
        val = (String) o;
      } else if (o instanceof List) {
        val = ((List<Object>) o).stream()
            .map(Object::toString)
            .collect(joining(delimiter));
      } else {
        val = o.toString();
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return val;
  }

}
