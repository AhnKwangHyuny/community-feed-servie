package org.fastcampus.community_feed.common.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

  @GetMapping
  public String healthcheck() {
    return "OK";
  }

}
