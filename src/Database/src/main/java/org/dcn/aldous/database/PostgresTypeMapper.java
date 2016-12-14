package org.dcn.aldous.database;

import lombok.SneakyThrows;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.collect.Lists.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class PostgresTypeMapper implements DatabaseTypeMapper {

  private static final String delimiter = "/,/";

  @Override
  public String sqlColumnType(Field f) {
    Class<?> type = f.getType();
    if (type.equals(String.class)) {
      return "TEXT";
    } else if (type.equals(Integer.class)) {
      return "INTEGER";
    } else {
      return "TEXT";
    }
  }

  @Override
  public String autoincrement() {
    return "SERIAL";
  }

  @Override
  @SneakyThrows(SQLException.class)
  public Object extractColumn(ResultSet resultSet, Field field) {
    Class<?> type = field.getType();
    if (field.isAnnotationPresent(Id.class)) {
      return resultSet.getInt(1);
    }
    String label = field.getAnnotation(Column.class).name();
    if (type.equals(String.class)) {
      return resultSet.getString(label);
    } else if (type.equals(Integer.class)) {
      return resultSet.getInt(label);
    } else {
      String list = resultSet.getString(label);
      List<String> strings = newArrayList(list.split(delimiter));
      if (label.toLowerCase().contains("ids")) { //TODO generalize
        return strings.stream()
            .filter(s -> !s.isEmpty())
            .map(Integer::parseInt)
            .collect(toList());
      }
      return strings;
    }
  }

  @Override
  public Object toColumnValue(Object obj, Field field) {
    Class<?> type = field.getType();
    if (type.equals(String.class)) {
      return removeAllNullChars((String) obj);
    } else if (type.equals(Integer.class)) {
      return (Integer) obj;
    } else {
      return removeAllNullChars(((List<Object>) obj).stream()
          .map(Object::toString)
          .collect(joining(delimiter)));
    }
  }

  private String removeAllNullChars(String s) {
    return s == null ? s : s.replaceAll("\\x00", "");
  }
}
