package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.util.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.dcn.aldous.providers.ConfigProvider;
import rx.Observable;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@AllArgsConstructor
public class DatabaseManager {

  private final Config config;

  @SneakyThrows
  public void createTableItems() {
    Connection connection = connect();
    String sql = format("create table %s(ID  SERIAL PRIMARY KEY, %s);", Item.TABLE, extractColumnNames());
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.execute();
  }

  @SneakyThrows
  public void dropTableItems() {
    Connection connection = connect();
    String sql = format("drop table %s;", Item.TABLE);
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.execute();
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
    ItemsDAO itemsDAO = new ItemsDAO(Database.from(connection));
    Observable<Item> matchingItems = itemsDAO.getMatchingItems("");
    matchingItems.forEach(System.out::println);
  }

  private String extractColumnNames() {
    return Arrays.asList(Item.class.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class))
        .map(f -> f.getAnnotation(Column.class).name() + " " + sqlFieldType(f))
        .collect(Collectors.joining(", "));
  }

  private String sqlFieldType(Field f) {
    String typeName = f.getType().getName();
    if (typeName.equals(String.class.getName())) {
      return "TEXT";
    } else {
      return "TEXT";
    }
  }

  private Connection connect() throws SQLException {
    Config db = config.getConfig("db");
    String url = db.getString("url");
    String user = db.getString("user");
    String password = db.getString("password");
    return DriverManager.getConnection(url, user, password);
  }

  public static void main(String[] args) throws Exception {
    DatabaseManager manager = DatabaseManager.of(new ConfigProvider().get());
    Arrays.asList(DatabaseManager.class.getMethods()).stream()
        .filter(m -> m.getName().equals(args[0]))
        .findFirst().orElseThrow(() -> new IllegalArgumentException("No such method: " + args[0]))
        .invoke(manager, Arrays.copyOfRange(args, 1, args.length));
  }

  private static DatabaseManager of(Config config) {
    return new DatabaseManager(config);
  }
}
