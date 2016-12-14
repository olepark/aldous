package org.dcn.aldous.query.dependencies;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.dcn.aldous.database.items.ItemsDAO;
import org.dcn.aldous.database.items.ItemsSearcher;
import org.dcn.aldous.database.lists.ListsDAO;
import org.dcn.aldous.database.providers.ItemSearcherProvider;
import org.dcn.aldous.database.providers.ItemsDAOProvider;
import org.dcn.aldous.database.providers.ListsDAOProvider;
import org.dcn.aldous.database.providers.UsersDAOProvider;
import org.dcn.aldous.database.users.UsersDAO;
import org.dcn.aldous.providers.ConfigProvider;
import org.dcn.aldous.query.services.auth.UsersService;

import static com.google.common.collect.Maps.newHashMap;

public class QueryServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ConfigProvider.class);
    bind(ItemsDAO.class).toProvider(ItemsDAOProvider.class).in(Singleton.class);
    bind(UsersDAO.class).toProvider(UsersDAOProvider.class).in(Singleton.class);
    bind(ListsDAO.class).toProvider(ListsDAOProvider.class).in(Singleton.class);
    bind(ItemsSearcher.class).toProvider(ItemSearcherProvider.class).in(Singleton.class);
  }

  @Provides
  @Singleton
  private UsersService service(UsersDAO dao) {
    return new UsersService(dao, newHashMap());
  }
}
