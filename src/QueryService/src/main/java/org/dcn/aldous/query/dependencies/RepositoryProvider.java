package org.dcn.aldous.query.dependencies;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.dcn.aldous.query.services.query.Item;
import org.dcn.aldous.query.services.query.ItemsRepository;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DB;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RepositoryProvider implements Provider<ItemsRepository> {

  private final DB db;

  @Inject
  public RepositoryProvider(DB db) {
    this.db = db;
  }

  @Override
  public ItemsRepository get() {
    return new ItemsRepository(db.hashSet("items", new ItemSerializer()).createOrOpen());
  }

  private class ItemSerializer implements org.mapdb.Serializer<Item> {

    @Override
    public void serialize(@NotNull DataOutput2 dataOutput2, @NotNull Item item) throws IOException {
      dataOutput2.writeChars(item.vendorCode());
      dataOutput2.writeChars(item.vendor());
      dataOutput2.writeChars(item.name());
      writeList(dataOutput2, item.urls());
      writeList(dataOutput2, item.tags());
      writeList(dataOutput2, item.properties());
    }

    private void writeList(@NotNull DataOutput2 dataOutput2, List<String> list) throws IOException {
      dataOutput2.writeInt(list.size());
      for (String s : list) {
        dataOutput2.writeChars(s);
      }
    }

    @Override
    public Item deserialize(@NotNull DataInput2 dataInput2, int i) throws IOException {
      String code = dataInput2.readUTF();
      String vendor = dataInput2.readUTF();
      String name = dataInput2.readUTF();
      List<String> urls = readList(dataInput2);
      List<String> tags = readList(dataInput2);
      List<String> properties = readList(dataInput2);
      return new Item(code, vendor, name, urls, tags, properties);
    }

    private List<String> readList(@NotNull DataInput2 dataInput2) throws IOException {
      int len = dataInput2.readInt();
      List<String> list = newArrayList();
      for (int j = 0; j < len; j++) {
        list.add(dataInput2.readUTF());
      }
      return list;
    }
  }
}
