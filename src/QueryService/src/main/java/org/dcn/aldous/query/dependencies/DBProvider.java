package org.dcn.aldous.query.dependencies;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public class DBProvider implements Provider<DB> {

  private final Config config;

  @Inject
  public DBProvider(Config config) {
    this.config = config;
  }

  @Override
  public DB get() {
    return DBMaker.fileDB(config.getString("pathToDB") + "/aldous.db")
        .closeOnJvmShutdown()
        .checksumStoreEnable()
        .make();
  }
}
