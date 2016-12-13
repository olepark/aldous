package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Slf4j
@RequiredArgsConstructor
public class DAO<T> {

  private final Database database;

  private final Class<T> entityClass;

  private final Function<ResultSet, T> rsParser;

  private final BiFunction<Field, T, String> fieldSerializer;

  public TableManager tableManager(DB db) {
    return new TableManager(db);
  }

  public Optional<T> getById(Integer id) {
    return selectFirstWhere("id=" + id);
  }

  public void deleteById(Integer id) {
    String sql = format("delete from %s where id=%d", tableName(), id);
    log.debug("Executing {}", sql);
    int affected = database.update(sql).execute();
    if (affected > 0) {
      log.debug("Deleted by id {}. {} Rows affected", id, affected);
    } else {
      log.warn("Table {} was not affected by {}", tableName(), sql);
    }
  }

  public void add(T obj) {
    String onConflict = conflict();
    String columns = columns()
        .map(Column::name)
        .collect(joining(", "));
    String values = colFields()
        .map(f -> fieldSerializer.apply(f, obj))
        .map(this::removeAllNullChars)
        .map(this::quoted)
        .collect(joining(", "));
    String insert = format("insert into %s(%s) values(%s) %s",
        tableName(), columns, values, onConflict);
    log.debug("Executing {}", insert);
    int inserted = database.update(insert).execute();
    Preconditions.checkState(inserted == 1, "Failed to insert " + obj);
  }

  protected Optional<T> selectFirstWhere(String where) {
    return firstOfEmpty(selectWhere(where));
  }

  private String quoted(String s) {
    return "'" + s + "'";
  }

  private String conflict() {
    String nonUnique = columns()
        .map(Column::name)
        .map(c -> format("%s=EXCLUDED.%s", c, c))
        .collect(joining(", "));
    return format("on conflict (%s) do update set %s", uniqueColumns(), nonUnique);
  }

  public Observable<T> selectWhere(String where) {
    return database
        .select("select * from " + tableName() + " where " + where)
        .get(rsParser::apply);
  }

  private String tableName() {
    return entityName() + "s";
  }

  private String entityName() {
    return entityClass.getAnnotation(Entity.class).name();
  }

  private Optional<T> firstOfEmpty(Observable<T> observable) {
    try {
      return Optional.of(observable.toBlocking().first());
    } catch (NoSuchElementException ex) {
      return Optional.empty();
    }
  }

  private String removeAllNullChars(String s) {
    return s.replaceAll("\\x00", "");
  }

  private String uniqueColumns() {
    return columns()
        .filter(Column::unique)
        .map(Column::name)
        .collect(joining(", "));
  }

  private Stream<Column> columns() {
    return colFields().map(f -> f.getAnnotation(Column.class));
  }

  private Stream<Field> colFields() {
    return Arrays.asList(entityClass.getDeclaredFields()).stream()
        .filter(f -> f.isAnnotationPresent(Column.class));
  }

  @RequiredArgsConstructor
  public class TableManager {

    private final DB db;

    @SneakyThrows(SQLException.class)
    public void createTable(String... additionalFields) {
      Connection connection = database.getConnectionProvider().get();
      String constraint = constraint();
      String sql = format("create table %s(%s%s%s);",
          tableName(), id(), typedColumnNames(), add(constraint, additionalFields));
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.execute();
    }

    public boolean tableExists() {
      try {
        database.select(format("select * from %s limit 1", tableName()))
            .get(rs -> rs).first().toBlocking().first();
        return true;
      } catch (NoSuchElementException ex) {
        return true;
      } catch (Throwable throwable){
        return false;
      }
    }

    public void createIfAbsent(String... additionalFields) {
      if (!tableExists()) {
        createTable(additionalFields);
      }
    }

    @SneakyThrows(SQLException.class)
    public void dropTable() {
      Connection connection = database.getConnectionProvider().get();
      String sql = format("drop table %s;", tableName());
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.execute();
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

    private String typedColumnNames() {
      return colFields()
          .map(f -> f.getAnnotation(Column.class).name() + " " + sqlFieldType(f))
          .collect(joining(", "));
    }

    private String sqlFieldType(Field f) {
      String typeName = f.getType().getName();
      if (typeName.equals(String.class.getName())) {
        return db.text;
      } else {
        return db.text;
      }
    }

    private String id() {
      Field[] fields = entityClass.getDeclaredFields();
      for (Field field : fields) {
        if (field.isAnnotationPresent(Id.class)) {
          return format("ID %s PRIMARY KEY, ", db.autoincrement);
        }
      }
      return "";
    }

    private String constraint() {
      String uniqueFields = uniqueColumns();
      if (!uniqueFields.isEmpty()) {
        return format("constraint unique_%s UNIQUE(%s)", entityName(), uniqueFields);
      } else {
        return "";
      }
    }
  }

  @RequiredArgsConstructor
  public enum DB {

    POSTGESQL("SERIAL", "TEXT"),
    HSQLDB("INTEGER IDENTITY", "VARCHAR(5000)"),
    OTHER("INTEGER AUTOINCREMENT", "VARCHAR");

    private final String autoincrement;

    private final String text;
  }
}
