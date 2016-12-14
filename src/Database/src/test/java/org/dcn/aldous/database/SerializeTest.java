package org.dcn.aldous.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.dcn.aldous.database.items.Item;
import org.dcn.aldous.database.users.AldousUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class SerializeTest<T> {

  private final Supplier<T> supplier;

  @Test
  public void testSerialize() throws IOException {
    T obj = supplier.get();
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(obj);
  }

  @Parameterized.Parameters
  public static List<Object[]> params() {
    Supplier<Item> itemSupplier = () ->
        new Item(0, "kek", "lol", "lur", "228", newArrayList("hah", "aha"), newArrayList("topkek", "ololo"));
    Supplier<AldousUser> userSupplier = () ->
        new AldousUser(0, "kek", newArrayList(2, 3), "topkek", "ololo");
    List<Object[]> objects = newArrayList();
    objects.add(new Object[]{itemSupplier});
    objects.add(new Object[]{userSupplier});
    return objects;
  }

}