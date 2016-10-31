package org.dcn.aldous.query;

import com.google.common.io.Resources;

public class QueryServiceApplicationRunner {

  public static void main(String[] args) throws Exception {
    String pathToRest = Resources.getResource("rest.yml").getPath();
    QueryServiceApplication.main(new String[]{"server", pathToRest});
  }

}