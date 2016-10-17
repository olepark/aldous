package org.dcn.aldous.query.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.dcn.aldous.database.ItemsDAO;
import org.dcn.aldous.providers.ConfigProvider;
import org.dcn.aldous.providers.ItemsDAOProvider;
import org.dcn.aldous.query.services.query.DescriptionParser;
import org.dcn.aldous.query.services.query.ItemsSearcher;

public class QueryServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ConfigProvider.class);
    bind(DescriptionParser.class).toInstance(new DescriptionParser());
    bind(ItemsDAO.class).toProvider(ItemsDAOProvider.class).in(Singleton.class);
    bind(ItemsSearcher.class).toProvider(ItemSearcherProvider.class).in(Singleton.class);
  }
}
