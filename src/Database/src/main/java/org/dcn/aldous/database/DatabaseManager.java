package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import com.github.davidmoten.util.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class DatabaseManager {

  private final Config config;

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
    System.out.println(tableName);
    while (columns.next()) {
      System.out.println("\t" + columns.getString(4) + " " + columns.getString(6));
    }
    Database.from(connection)
        .select("select * from " + tableName)
        .getTupleN()
        .map(this::rowToString)
        .forEach(System.out::println);
  }

  protected String rowToString(TupleN<Object> row) {
    return row.values().stream()
        .map(Object::toString)
        .collect(joining(", "));
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
