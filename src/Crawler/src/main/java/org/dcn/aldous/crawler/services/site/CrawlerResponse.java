package org.dcn.aldous.crawler.services.site;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect
public class CrawlerResponse {

  private final String response;

  private final Integer jobId;

}
