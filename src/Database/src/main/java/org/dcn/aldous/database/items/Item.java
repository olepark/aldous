package org.dcn.aldous.database.items;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Accessors(fluent = true)
@Entity(name = "item")
@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Item {

  @Id
  private final Integer id;

  @Column(name = "vendor", unique = true)
  private final String vendor;

  @Column(name = "name", unique = true)
  private final String name;

  @Column(name = "url", unique = true)
  private final String url;

  @Column(name = "price")
  private final String price;

  @Column(name = "tags")
  private final List<String> tags;

  @Column(name = "properties")
  private final List<String> properties;

  public String fullName() {
    return vendor + " " + name;
  }

}
