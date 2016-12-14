package org.dcn.aldous.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * Created by Олег on 14.12.2016.
 */
public interface DatabaseTypeMapper {

  String sqlColumnType(Field f);

  String autoincrement();

  Object extractColumn(ResultSet resultSet, Field field);

  Object toColumnValue(Object obj, Field field);
}
