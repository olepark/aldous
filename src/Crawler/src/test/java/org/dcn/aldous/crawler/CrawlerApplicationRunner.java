package org.dcn.aldous.crawler;

import com.google.common.io.Resources;

public class CrawlerApplicationRunner {

  public static void main(String[] args) throws Exception {
    String pathToRest = Resources.getResource("rest.yml").getPath();
    CrawlerApplication.main(new String[]{"server", pathToRest});
  }
}