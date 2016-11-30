package org.dcn.aldous.database;

import lombok.Data;
import lombok.experimental.Accessors;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Data
@Accessors(fluent = true)
@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
    creatorVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Item {

  public static String TABLE = "items";

  @Column(name = "vendor_code")
  private final String vendorCode;

  @Column(name = "vendor")
  private final String vendor;

  @Column(name = "name")
  private final String name;

  @Column(name = "url")
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
