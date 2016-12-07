package org.dcn.aldous.database.providers;

import com.github.davidmoten.rx.jdbc.Database;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;

public class DBProvider implements Provider<Database> {

  private final Config config;

  @Inject
  public DBProvider(Config config) {
    this.config = config;
  }

  @Override
  public Database get() {
    Config db = config.getConfig("db");
    String url = db.getString("url");
    String user = db.getString("user");
    String password = db.getString("password");
    return Database.builder()
        .url(url)
        .username(user)
        .password(password)
        .pool(2, 6)
        .build();
  }
}
