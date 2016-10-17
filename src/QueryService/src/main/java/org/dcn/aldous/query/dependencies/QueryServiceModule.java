package org.dcn.aldous.query.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.dcn.aldous.query.services.query.DescriptionParser;
import org.dcn.aldous.query.services.query.ItemsRepository;
import org.mapdb.DB;

public class QueryServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ConfigProvider.class);
    bind(DescriptionParser.class).toInstance(new DescriptionParser());
    bind(DB.class).toProvider(DBProvider.class).in(Singleton.class);
    bind(ItemsRepository.class).toProvider(RepositoryProvider.class).in(Singleton.class);
  }
}
