package org.faddy.community_feed;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.common.database.service.TestDataGenerationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling  
public class Application implements CommandLineRunner {

  private final TestDataGenerationService testDataGenerationService;

  // 전체 테스트 데이터 생성 여부
  @Value("${app.test-data.enabled:false}")
  private boolean testDataEnabled;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (testDataEnabled) {
      testDataGenerationService.generateAllTestData();
    }
  }
}