package org.dcn.aldous.app;

import com.google.common.io.Resources;

public class PurchaseManagerApplicationRunner {

  public static void main(String[] args) throws Exception {
    String pathToRest = Resources.getResource("rest.yml").getPath();
    PurchaseManagerServerApplication.main(new String[]{"server", pathToRest});
  }

}