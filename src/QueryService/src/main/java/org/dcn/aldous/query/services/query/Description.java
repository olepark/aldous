package org.dcn.aldous.query.services.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class Description {

  private final String rawDescription;

  private final String vendor;

  private final String name;

  private final List<String> tags;

  private final List<String> properties;

}
