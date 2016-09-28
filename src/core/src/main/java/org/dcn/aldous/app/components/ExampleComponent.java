package org.dcn.aldous.app.components;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExampleComponent {

  private final String pingString;

  public String ping() {
    return pingString;
  }
}
