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
                    "    DECLARE profile_image_url VARCHAR(255);\n" +
                    "    \n" +
                    "    -- 마지막 ID 확인\n" +
                    "    SELECT IFNULL(MAX(id), 0) INTO last_id FROM community_user;\n" +
                    "    SET i = last_id + 1;\n" +
                    "    \n" +
                    "    -- 트랜잭션 시작\n" +
                    "    START TRANSACTION;\n" +
                    "    \n" +
                    "    WHILE i <= (last_id + total_records) DO\n" +
                    "        -- 프로필 이미지를 5개 중 하나로 랜덤 선택\n" +
                    "        SET profile_image_url = CASE FLOOR(1 + RAND() * 5)\n" +
                    "            WHEN 1 THEN '/static/images/test/default_profile.jpg'\n" +
                    "            WHEN 2 THEN '/static/images/test/default_profile.jpg'\n" +
                    "            WHEN 3 THEN '/static/images/test/default_profile.jpg'\n" +
                    "            WHEN 4 THEN '/static/images/test/default_profile.jpg'\n" +
                    "            ELSE '/static/images/test/default_profile.jpg'\n" +
                    "        END;\n" +
                    "        \n" +
                    "        -- community_user 테이블에 데이터 삽입\n" +
                    "        INSERT INTO community_user (\n" +
                    "            id, reg_dt, upd_dt, follower_count, following_count, name, profile_image\n" +
                    "        ) VALUES (\n" +
                    "            i,\n" +
                    "            DATE_FORMAT(\n" +
                    "                DATE_ADD('2024-01-01 00:00:00', INTERVAL FLOOR(RAND() * 120 * 24 * 60 * 60) SECOND),\n" +
                    "                '%Y-%m-%d %H:%i:%s'\n" +
                    "            ),\n" +
                    "            DATE_FORMAT(\n" +
                    "                DATE_ADD('2024-01-01 00:00:00', INTERVAL FLOOR(RAND() * 120 * 24 * 60 * 60) SECOND),\n" +
                    "                '%Y-%m-%d %H:%i:%s'\n" +
                    "            ),\n" +
                    "            FLOOR(RAND() * 300),\n" +
                    "            FLOOR(RAND() * 150),\n" +
                    "            CONCAT(\n" +
                    "                ELT(FLOOR(1 + RAND() * 10), '김', '이', '박', '최', '정', '강', '윤', '임', '한', '송'),\n" +
                    "                ELT(FLOOR(1 + RAND() * 10), '민준', '서연', '지호', '수아', '도윤', '지유', '서준', '하은', '민서', '지원')\n" +
                    "            ),\n" +
                    "            profile_image_url\n" +
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
                    "                i, '@naver.com'\n" +
                    "            ),\n" +
                    "            CONCAT('$2a$10$', SUBSTRING(MD5(RAND()), 1, 30)),\n" +
                    "            CASE\n" +
                    "                WHEN RAND() < 0.9 THEN 'USER'\n" +
                    "                ELSE 'ADMIN'\n" +
                    "            END,\n" +
                    "            i,\n" +
                    "            DATE_FORMAT(\n" +
                    "                DATE_ADD('2024-01-01 00:00:00', INTERVAL FLOOR(RAND() * 120 * 24 * 60 * 60) SECOND),\n" +
                    "                '%Y-%m-%d %H:%i:%s'\n" +
                    "            )\n" +
                    "        );\n" +
                    "        \n" +
                    "        -- 1000개 단위로 커밋\n" +
                    "        IF i % 1000 = 0 THEN\n" +
                    "            COMMIT;\n" +
                    "            START TRANSACTION;\n" +
                    "            SELECT CONCAT('사용자 생성 진행 중... ', i, '/', last_id + total_records) AS progress;\n" +
                    "        END IF;\n" +
                    "        \n" +
                    "        SET i = i + 1;\n" +
                    "    END WHILE;\n" +
                    "    \n" +
                    "    -- 남은 트랜잭션 커밋\n" +
                    "    COMMIT;\n" +
                    "    \n" +
                    "    SELECT CONCAT('총 ', total_records, '명의 사용자가 생성되었습니다.') AS completion_message;\n" +
                    "END"
            );
            System.out.println("'" + getProcedureName() + "' 저장 프로시저가 성공적으로 생성되었습니다.");
        } else {
            System.out.println("'" + getProcedureName() + "' 저장 프로시저가 이미 존재합니다.");
        }
    }

    @Override
    public String getProcedureName() {
        return "generate_user_data";
    }
}