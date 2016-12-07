package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import com.github.davidmoten.util.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.providers.ConfigProvider;
import rx.Observable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class DatabaseManager {

  private final Config config;

  @SneakyThrows
  public void createTable(Class<?> entity, String ... additionalFields) {
    Connection connection = connect();
    String constraint = constraint(entity);
    String sql = format("create table %s(%s%s%s);",
        tableName(entity), id(entity), columnNames(entity), add(constraint, additionalFields));
    System.out.println(sql);
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.execute();
  }

  private String id(Class<?> entity) {
    Field[] fields = entity.getDeclaredFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Id.class)) {
        return "ID  SERIAL PRIMARY KEY, ";
      }
    }
    return "";
  }

  private String add(String constraint, String[] additionalFields) {
    StringBuilder builder = new StringBuilder();
    for (String field : additionalFields) {
      builder.append(", ");
      builder.append(field);
    }
    if (!constraint.isEmpty()) {
      builder.append(", ");
      builder.append(constraint);
    }
    return builder.toString();
  }

  private String constraint(Class<?> entity) {
    String uniqueFields = uniqueFields(entity);
    if (!uniqueFields.isEmpty()) {
      return format("constraint unique_%s UNIQUE(%s)", entityName(entity), uniqueFields);
    } else {
      return "";
    }
  }

  private String uniqueFields(Class<?> entity) {
    return Arrays.asList(entity.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class))
        .map(f -> f.getAnnotation(Column.class))
        .filter(Column::unique).map(Column::name)
        .collect(joining(", "));
  }

  @SneakyThrows
  public void dropTable(Class<?> entity) {
    Connection connection = connect();
    String sql = format("drop table %s;", tableName(entity));
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.execute();
  }

  private String tableName(Class<?> aClass) {
    return entityName(aClass) + "s";
  }

  private String entityName(Class<?> entity) {
    return entity.getAnnotation(Entity.class).name();
  }

  private String columnNames(Class<?> aClass) {
    return Arrays.asList(aClass.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class))
        .map(f -> f.getAnnotation(Column.class).name() + " " + sqlFieldType(f))
        .collect(joining(", "));
  }

  private String sqlFieldType(Field f) {
    String typeName = f.getType().getName();
    if (typeName.equals(String.class.getName())) {
      return "TEXT";
    } else {
      return "TEXT";
    }
  }

  public void printTables() {
    listTables().forEach(System.out::println);
  }

  @SneakyThrows
  public List<String> listTables() {
    Connection connection = connect();
    DatabaseMetaData md = connection.getMetaData();
    ResultSet rs = md.getTables(null, null, "%", null);
    List<String> tables = newArrayList();
    while (rs.next()) {
      tables.add(rs.getString(3));
    }
    return tables;
  }

  @SneakyThrows
  public void viewTable(String tableName) {
    Connection connection = connect();
    DatabaseMetaData md = connection.getMetaData();
    ResultSet tables = md.getTables(null, null, tableName, null);
    Preconditions.checkArgument(tables.next(), "Table " + tableName + " not found");
    ResultSet columns = md.getColumns(null, null, tableName, null);
    ArrayListMultimap.create();
    while (columns.next()) {
      System.out.println(columns.getString(4) + " " + columns.getString(6));
    }
    Database.from(connection)
        .select("select * from " + tableName)
        .getTupleN()
        .map(row -> row.values().stream().map(Object::toString).collect(joining(", ")))
        .forEach(System.out::println);
  }

  private Connection connect() throws SQLException {
    Config db = config.getConfig("db");
    String url = db.getString("url");
    String user = db.getString("user");
    String password = db.getString("password");
    return DriverManager.getConnection(url, user, password);
  }

  static DatabaseManager of(Config config) {
    return new DatabaseManager(config);
  }
}
