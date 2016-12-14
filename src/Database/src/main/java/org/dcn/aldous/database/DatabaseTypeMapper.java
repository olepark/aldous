package org.dcn.aldous.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public interface DatabaseTypeMapper {

  String sqlColumnType(Field f);

  String autoincrement();

  Object extractColumn(ResultSet resultSet, Field field);

  Object toColumnValue(Object obj, Field field);
}
