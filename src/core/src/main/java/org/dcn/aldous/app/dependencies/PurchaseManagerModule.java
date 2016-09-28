package org.dcn.aldous.app.dependencies;

import com.google.inject.AbstractModule;
import org.dcn.aldous.app.components.ExampleComponent;

public class PurchaseManagerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ExampleComponent.class).toInstance(new ExampleComponent("pong"));
  }
}
