package org.faddy.community_feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableJpaAuditing
@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  // 프로시저 실행 여부를 결정하는 속성 (application.properties에 설정)
  @Value("${app.generate-test-data:false}")
  private boolean generateTestData;

  // 생성할 레코드 수
  @Value("${app.test-data-count:500000}")
  private int testDataCount;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (generateTestData) {
      System.out.println("테스트 데이터 생성 시작...");

      // 프로시저가 존재하는지 확인하고 없으면 생성
      createProcedureIfNotExists();

      // 프로시저 실행
      jdbcTemplate.execute("CALL generate_user_data(" + testDataCount + ")");

      System.out.println("테스트 데이터 생성 완료!");
    }
  }

  /**
   *  userAuth , User Enttiy 테스팅 데이터 50만개 생성 프로시져
   *
   * */
  private void createProcedureIfNotExists() {
    // 프로시저가 이미 존재하는지 확인
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM information_schema.routines WHERE routine_schema = DATABASE() AND routine_name = 'generate_user_data'",
        Integer.class
    );

    if (count != null && count == 0) {
      // 프로시저 생성
      jdbcTemplate.execute(
          "CREATE PROCEDURE generate_user_data(IN total_records INT)\n" +
              "BEGIN\n" +
              "    DECLARE i INT DEFAULT 1;\n" +
              "    DECLARE last_id INT;\n" +
              "    \n" +
              "    -- 마지막 ID 확인\n" +
              "    SELECT IFNULL(MAX(id), 0) INTO last_id FROM community_user;\n" +
              "    SET i = last_id + 1;\n" +
              "    \n" +
              "    -- 트랜잭션 시작\n" +
              "    START TRANSACTION;\n" +
              "    \n" +
              "    WHILE i <= (last_id + total_records) DO\n" +
              "        -- community_user 테이블에 데이터 삽입\n" +
              "        INSERT INTO community_user (\n" +
              "            id, follower_count, following_count, reg_date, reg_dt, upd_dt, name, profile_image\n" +
              "        ) VALUES (\n" +
              "            i,\n" +
              "            FLOOR(10 + RAND() * 3000),\n" +
              "            FLOOR(10 + RAND() * 1500),\n" +
              "            DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 365) DAY),\n" +
              "            DATE_FORMAT(\n" +
              "                DATE_ADD('2023-01-01 00:00:00', INTERVAL FLOOR(RAND() * 365 * 24 * 60 * 60) SECOND),\n" +
              "                '%Y-%m-%d %H:%i:%s'\n" +
              "            ),\n" +
              "            DATE_FORMAT(\n" +
              "                DATE_ADD('2023-01-01 00:00:00', INTERVAL FLOOR(RAND() * 365 * 24 * 60 * 60) SECOND),\n" +
              "                '%Y-%m-%d %H:%i:%s'\n" +
              "            ),\n" +
              "            CONCAT(\n" +
              "                ELT(FLOOR(1 + RAND() * 10), '김', '이', '박', '최', '정', '강', '윤', '임', '한', '송'),\n" +
              "                ELT(FLOOR(1 + RAND() * 10), '민준', '서연', '지호', '수아', '도윤', '지유', '서준', '하은', '민서', '지원')\n" +
              "            ),\n" +
              "            CONCAT('profile/', IF(RAND() < 0.3, CONCAT('default', FLOOR(RAND() * 10) + 1, '.jpg'), CONCAT('user', i, '.png')))\n" +
              "        );\n" +
              "        \n" +
              "        -- community_user_auth 테이블에 데이터 삽입\n" +
              "        INSERT INTO community_user_auth (\n" +
              "            email, password, role, user_id, last_login_at\n" +
              "        ) VALUES (\n" +
              "            CONCAT(\n" +
              "                LOWER(\n" +
              "                    ELT(FLOOR(1 + RAND() * 10), 'user', 'member', 'account', 'person', 'client', 'customer', 'individual', 'profile', 'identity', 'user')\n" +
              "                ),\n" +
              "                i, '@example.com'\n" +
              "            ),\n" +
              "            CONCAT('$2a$10$', SUBSTRING(MD5(RAND()), 1, 30)),\n" +
              "            ELT(FLOOR(1 + RAND() * 3), 'USER', 'MODERATOR', 'ADMIN'),\n" +
              "            i,\n" +
              "            DATE_FORMAT(\n" +
              "                DATE_ADD('2023-01-01 00:00:00', INTERVAL FLOOR(RAND() * 365 * 24 * 60 * 60) SECOND),\n" +
              "                '%Y-%m-%d %H:%i:%s'\n" +
              "            )\n" +
              "        );\n" +
              "        \n" +
              "        -- 5000개 단위로 커밋\n" +
              "        IF i % 5000 = 0 THEN\n" +
              "            COMMIT;\n" +
              "            START TRANSACTION;\n" +
              "        END IF;\n" +
              "        \n" +
              "        SET i = i + 1;\n" +
              "    END WHILE;\n" +
              "    \n" +
              "    -- 남은 트랜잭션 커밋\n" +
              "    COMMIT;\n" +
              "END"
      );
      System.out.println("'generate_user_data' 저장 프로시저가 성공적으로 생성되었습니다.");
    }
  }
}