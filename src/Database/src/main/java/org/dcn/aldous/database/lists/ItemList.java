package org.dcn.aldous.database.lists;

import lombok.Data;
import org.dcn.aldous.database.items.Item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity(name = "list")
@Data
public class ItemList {

  @Id
  private final Integer id;

  @Column(name = "name", unique = true)
  private final String name;

  @Column(name = "itemIds")
  private final List<Integer> itemIds;

  @Column(name = "ownerId", unique = true)
  private final Integer ownerId;

}
