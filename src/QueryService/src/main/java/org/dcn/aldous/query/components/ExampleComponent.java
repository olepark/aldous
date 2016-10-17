package org.dcn.aldous.query.components;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExampleComponent {

  private final String pingString;

  public String ping() {
    return pingString;
  }
}
