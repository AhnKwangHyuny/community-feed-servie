package org.faddy.community_feed.common.database.procedure.impl;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.common.database.procedure.ProcedureCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataProcedureCreator implements ProcedureCreator {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createProcedureIfNotExists() {
        // 프로시저가 이미 존재하는지 확인
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM information_schema.routines WHERE routine_schema = DATABASE() AND routine_name = ?",
            Integer.class,
            getProcedureName()
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
            System.out.println("'" + getProcedureName() + "' 저장 프로시저가 성공적으로 생성되었습니다.");
        }
    }

    @Override
    public String getProcedureName() {
        return "generate_user_data";
    }
}