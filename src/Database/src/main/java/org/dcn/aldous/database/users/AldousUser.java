package org.dcn.aldous.database.users;

import lombok.Data;
import org.dcn.aldous.database.lists.ItemList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.Principal;
import java.util.List;

@Data
@Entity(name = "user")
public class AldousUser implements Principal {

  @Id
  private final Integer id;

  @Column(name = "name", unique = true)
  private final String name;

  @Column(name = "listIds")
  private final List<Integer> listIds;

}
