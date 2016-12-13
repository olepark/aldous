package org.dcn.aldous.database;

import com.github.davidmoten.rx.jdbc.Database;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.lists.ItemList;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.providers.DBProvider;
import org.dcn.aldous.database.users.AldousUser;
import org.dcn.aldous.database.users.UsersDAO;
import org.dcn.aldous.providers.ConfigProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class DAOTest<T> {

  private final static Database db = getDB();

  private static final int id = 1;

  private final DAO<T> dao;

  private final Supplier<T> supplier;

  @Before
  public void setUp() {
    dao.tableManager(DAO.DB.POSTGESQL).createIfAbsent();
  }

  @After
  public void tearDown() {
    dao.tableManager(DAO.DB.POSTGESQL).dropTable();
  }

  @Test
  public void test() {
    T original = supplier.get();
    dao.add(original);
    Optional<T> byId = dao.getById(id);
    assertThat(byId).isPresent();
    T retrieved = byId.get();
    assertThat(retrieved).isEqualToComparingFieldByField(original);
    dao.deleteById(id);
    assertThat(dao.getById(id)).isNotPresent();
  }

  private static Database getDB() {
    return new DBProvider(new ConfigProvider().get()).get();
  }

  @Parameterized.Parameters
  public static List<Object[]> daos() {

    DAO<Item> itemDAO = ItemsDAO.create(db);
    Supplier<Item> samsung = () -> new Item(id, "Samsung", "Galaxy S5",
        "http://nope", "228", newArrayList("smartphone", "cellphone"),
        newArrayList("dope", "expensive", "iphone plagiarism"));

    ListsDAO listsDAO = ListsDAO.create(db);
    Supplier<ItemList> lists = () -> new ItemList(id, "list 1", newArrayList(1, 200), newArrayList(7));

    UsersDAO usersDAO = UsersDAO.create(db);
    Supplier<AldousUser> users = () -> new AldousUser(id, "OP", newArrayList(5));

    List<Object[]> list = newArrayList();
    list.add(new Object[]{itemDAO, samsung});
    list.add(new Object[]{listsDAO, lists});
    list.add(new Object[]{usersDAO, users});
    return list;
  }

}
