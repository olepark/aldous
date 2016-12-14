package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.QueryUpdate;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import javax.persistence.Column;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Slf4j
public class DAO<T> {

  private final Database database;

  private final ORM<T> orm;
  
  private final String tableName;

  public DAO(Database database, ORM<T> orm) {
    this.database = database;
    this.orm = orm;
    tableName = orm.tableName();
  }

  public TableManager tableManager() {
    return new TableManager();
  }

  public Optional<T> getById(Integer id) {
    return selectFirstWhere("id=" + id);
  }

  public void deleteById(Integer id) {
    String sql = format("delete from %s where id=%d", tableName, id);
    log.debug("Executing {}", sql);
    int affected = database.update(sql).execute();
    if (affected > 0) {
      log.debug("Deleted by id {}. {} Rows affected", id, affected);
    } else {
      log.warn("Table {} was not affected by {}", tableName, sql);
    }
  }

  public Integer add(T obj) {
    String insert = insertStatement();
    log.debug("Executing {}", insert);
    QueryUpdate.Builder update = database.update(insert);
    List<Object> values = orm.columnValues(obj);
    values.forEach(update::parameter);
    Observable<Integer> id = update
        .returnGeneratedKeys()
        .get(rs -> rs.getInt(1));
    return firstOfEmpty(id).orElse(null);
  }

  protected String insertStatement() {
    String onConflict = conflict();
    String columns = orm.collectColumns(c -> true, Column::name, c -> true, joining(", "));
    String values = orm.collectColumns(c -> "?", joining(", "));
    return format("insert into %s(%s) values(%s) %s",
        tableName, columns, values, onConflict);
  }

  public String conflict() {
    String nonUnique = orm.collectColumns(c -> !c.unique(),
        c -> format("%s=EXCLUDED.%s", c.name(), c.name()), joining(", "));
    String unique = orm.collectColumns(Column::unique, Column::name, joining(", "));
    return format("on conflict (%s) do update set %s", unique, nonUnique);
  }

  public <R> void update(Integer id, String column, R newValue) {
    int executed = database.update(format("update %s set %s=? where id=%d", tableName, column, id))
        .parameter(orm.columnValue(column, newValue))
        .execute();
    Preconditions.checkState(executed > 0, format("Failed to update %s for id %d", column, id));
  }

  protected Optional<T> selectFirstWhere(String where) {
    return firstOfEmpty(selectWhere(where));
  }

  public Observable<T> selectWhere(String where) {
    return database
        .select(format("select * from %s where %s", tableName, where))
        .get(orm::parse);
  }
  
  private <R> Optional<R> firstOfEmpty(Observable<R> observable) {
    try {
      return Optional.of(observable.toBlocking().first());
    } catch (NoSuchElementException ex) {
      return Optional.empty();
    }
  }

  public class TableManager {

    @SneakyThrows(SQLException.class)
    public void createTable(String... additionalFields) {
      Connection connection = database.getConnectionProvider().get();
      String constraint = constraint();
      String sql = format("create table %s(%s%s%s);",
          tableName, orm.id(), orm.typedColumnNames(), add(constraint, additionalFields));
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.execute();
    }

    public boolean tableExists() {
      try {
        database.select(format("select * from %s limit 1", tableName))
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
      String sql = format("drop table %s;", tableName);
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

    private String constraint() {
      String unique = orm.collectColumns(Column::unique, Column::name, joining(", "));
      if (!unique.isEmpty()) {
        return format("constraint unique_%s UNIQUE(%s)", tableName, unique);
      } else {
        return "";
      }
    }
  }

}
