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

  @Column(name = "VENDOR_CODE")
  private final String vendorCode;

  @Column(name = "VENDOR")
  private final String vendor;

  @Column(name = "NAME")
  private final String name;

  @Column(name = "URLS")
  private final List<String> urls;

  @Column(name = "TAGS")
  private final List<String> tags;

  @Column(name = "PROPERTIES")
  private final List<String> properties;

  public String fullName() {
    return vendor + name;
  }

}
