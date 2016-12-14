package org.dcn.aldous.database.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dcn.aldous.database.lists.ItemList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.security.Principal;
import java.util.List;

@Data
@Entity(name = "user")
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect
public class AldousUser implements Principal {

  @Id
  private final Integer id;

  @Column(name = "name", unique = true)
  private final String name;

  @Column(name = "listIds")
  private final List<Integer> listIds;

  @Column(name = "username", unique = true)
  private final String username;

  @Column(name = "password")
  private final String password;

}
