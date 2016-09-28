package org.dcn.aldous.app.services;

import com.google.inject.Inject;
import org.dcn.aldous.app.components.ExampleComponent;

public class ExampleService {

  private final ExampleComponent component;

  @Inject
  public ExampleService(final ExampleComponent component) {
    this.component = component;
  }

  public String ping() {
    return component.ping();
  }
}
