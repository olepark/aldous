package org.dcn.aldous.database;

import org.assertj.core.util.Preconditions;
import org.jetbrains.annotations.NotNull;

public class DatabaseManagerRunner {

  public static void main(String[] args) throws Exception {
//    DatabaseManager.main(q("createTableItems"));
    DatabaseManager.main(q("viewTable", "items"));
  }

  @NotNull
  private static String[] q(String ... args) {
    Preconditions.checkArgument(args.length > 0, "You must specify action");
    return args;
  }

}
