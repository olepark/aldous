package org.dcn.aldous.database;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
@Accessors(fluent = true)
public class ORM<T> {

  private final Class<T> entityClass;

  private final DatabaseTypeMapper typeMapper;

  public String tableName() {
    return entityClass.getAnnotation(Entity.class).name() + "s";
  }

  @SneakyThrows({NoSuchMethodException.class,
      IllegalAccessException.class,
      InvocationTargetException.class,
      InstantiationException.class})
  public T parse(ResultSet resultSet) {
    List<Field> fields = newArrayList(entityClass.getDeclaredFields());
    List<Class<?>> fieldTypes = transform(fields, Field::getType);
    List<Object> fieldValues = transform(fields, f -> getColObj(resultSet, f));
    Class[] parameterTypes = fieldTypes.toArray(new Class[fieldTypes.size()]);
    Constructor<T> constructor = entityClass.getConstructor(parameterTypes);
    return constructor.newInstance(fieldValues.toArray());
  }

  public List<Object> columnValues(T t) {
    List<Field> fieldsList = colFields().collect(Collectors.toList());
    return transform(fieldsList, f -> columnValueFromT(t, f));
  }


  public Object columnValue(String column, Object value) {
    return columnValue(value, field(column));
  }

  private Field field(String column) {
    return colFields()
        .filter(f -> f.getAnnotation(Column.class).name().equals(column))
        .findAny()
        .orElseThrow(() -> new NoSuchElementException(format("Column %s not found", column)));
  }

  @SneakyThrows(IllegalAccessException.class)
  private Object columnValueFromT(T t, Field field) {
    field.setAccessible(true);
    Object value = field.get(t);
    field.setAccessible(false);
    return columnValue(value, field);
  }

  private Object getColObj(ResultSet resultSet, Field field) {
    return typeMapper.extractColumn(resultSet, field);
  }

  private Object columnValue(Object obj, Field field) {
    return typeMapper.toColumnValue(obj, field);
  }

  public <R, M> R collectColumns(
      Function<Column, M> mapper,
      Collector<M, ?, R> collector) {
    return columns()
        .map(mapper)
        .collect(collector);
  }

  public <R, M> R collectColumns(
      Predicate<Column> columnFilter,
      Function<Column, M> mapper,
      Collector<M, ?, R> collector) {
    return columns()
        .filter(columnFilter)
        .map(mapper)
        .collect(collector);
  }

  public <R, M> R collectColumns(
      Predicate<Column> columnFilter,
      Function<Column, M> mapper,
      Predicate<M> mFilter,
      Collector<M, ?, R> collector) {
    return columns()
        .filter(columnFilter)
        .map(mapper)
        .filter(mFilter)
        .collect(collector);
  }

  public String typedColumnNames() {
    return colFields()
        .map(f -> f.getAnnotation(Column.class).name() + " " + typeMapper.sqlColumnType(f))
        .collect(joining(", "));

  }

  public String id() {
    if (isIdPresent()) {
      return format("ID %s PRIMARY KEY, ", typeMapper.autoincrement());
    }
    return "";
  }

  private Stream<Column> columns() {
    return colFields().map(f -> f.getAnnotation(Column.class));
  }

  private Stream<Field> colFields() {
    return newArrayList(entityClass.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class));
  }

  private boolean isIdPresent() {
    Field[] fields = entityClass.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Id.class)) {
        return true;
      }
    }
    return false;
  }
}
