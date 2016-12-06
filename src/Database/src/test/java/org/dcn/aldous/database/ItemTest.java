package org.dcn.aldous.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

public class ItemTest {

  @Test
  public void testSerialize() throws IOException {
    Item item = new Item(0, "kek", "lol", "lur", "228", newArrayList("hah", "aha"), newArrayList("topkek", "ololo"));
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(item);
    Item actual = mapper.readValue(content, Item.class);
    assertThat(actual).isEqualToComparingFieldByField(item);
  }

}