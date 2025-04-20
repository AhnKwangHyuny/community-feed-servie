package org.faddy.community_feed.common.database.procedure.impl;

import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.common.database.procedure.ProcedureCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDataProcedureCreator implements ProcedureCreator {

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
                "CREATE PROCEDURE generate_post_data(IN total_records INT)\n" +
                    "BEGIN\n" +
                    "    DECLARE i INT DEFAULT 1;\n" +
                    "    DECLARE last_id INT;\n" +
                    "    DECLARE user_count INT;\n" +
                    "    DECLARE random_user_id INT;\n" +
                    "    DECLARE min_user_id INT;\n" +
                    "    DECLARE max_user_id INT;\n" +
                    "    \n" +
                    "    -- 마지막 게시물 ID 확인\n" +
                    "    SELECT IFNULL(MAX(id), 0) INTO last_id FROM community_post;\n" +
                    "    SET i = last_id + 1;\n" +
                    "    \n" +
                    "    -- 사용자 ID 범위 확인\n" +
                    "    SELECT MIN(id), MAX(id) INTO min_user_id, max_user_id FROM community_user;\n" +
                    "    SET user_count = max_user_id - min_user_id + 1;\n" +
                    "    \n" +
                    "    -- 트랜잭션 시작\n" +
                    "    START TRANSACTION;\n" +
                    "    \n" +
                    "    WHILE i <= (last_id + total_records) DO\n" +
                    "        -- 랜덤 사용자 ID 선택 (실제 존재하는 사용자 ID 범위 내에서)\n" +
                    "        SET random_user_id = min_user_id + FLOOR(RAND() * user_count);\n" +
                    "        \n" +
                    "        -- 랜덤 사용자 ID가 존재하는지 확인 (없으면 다시 선택)\n" +
                    "        WHILE NOT EXISTS (SELECT 1 FROM community_user WHERE id = random_user_id) DO\n" +
                    "            SET random_user_id = min_user_id + FLOOR(RAND() * user_count);\n" +
                    "        END WHILE;\n" +
                    "        \n" +
                    "        -- community_post 테이블에 데이터 삽입\n" +
                    "        INSERT INTO community_post (\n" +
                    "            id, reg_dt, upd_dt, comment_counter, content, like_count, state, author_id\n" +
                    "        ) VALUES (\n" +
                    "            i,\n" +
                    "            DATE_FORMAT(\n" +
                    "                DATE_ADD('2023-01-01 00:00:00', INTERVAL FLOOR(RAND() * 365 * 24 * 60 * 60) SECOND),\n" +
                    "                '%Y-%m-%d %H:%i:%s'\n" +
                    "            ),\n" +
                    "            DATE_FORMAT(\n" +
                    "                DATE_ADD('2023-01-01 00:00:00', INTERVAL FLOOR(RAND() * 365 * 24 * 60 * 60) SECOND),\n" +
                    "                '%Y-%m-%d %H:%i:%s'\n" +
                    "            ),\n" +
                    "            FLOOR(RAND() * 50), -- 0~49 사이의 댓글 수\n" +
                    "            CONCAT(\n" +
                    "                ELT(FLOOR(1 + RAND() * 10), \n" +
                    "                    '오늘 날씨가 정말 좋네요!', \n" +
                    "                    '새로운 프로젝트를 시작했어요.', \n" +
                    "                    '이번 주말에 가족들과 여행 다녀왔어요.', \n" +
                    "                    '맛있는 음식점을 발견했습니다.', \n" +
                    "                    '오랜만에 영화 보러 갔어요.', \n" +
                    "                    '공부하는 중입니다...', \n" +
                    "                    '커피 한 잔의 여유~', \n" +
                    "                    '오늘의 일기를 씁니다.', \n" +
                    "                    '새로운 취미를 시작했어요!', \n" +
                    "                    '열심히 살아가는 중입니다.'\n" +
                    "                ),\n" +
                    "                ' ',\n" +
                    "                ELT(FLOOR(1 + RAND() * 10), \n" +
                    "                    '모두들 행복한 하루 되세요!', \n" +
                    "                    '여러분은 어떻게 지내고 계신가요?', \n" +
                    "                    '오늘도 화이팅!', \n" +
                    "                    '좋은 하루 보내세요~', \n" +
                    "                    '감사합니다.', \n" +
                    "                    '열심히 해봅시다!', \n" +
                    "                    '곧 주말이네요!', \n" +
                    "                    '다들 건강 조심하세요.', \n" +
                    "                    '요즘 너무 바빠요...', \n" +
                    "                    '행복합니다!'\n" +
                    "                )\n" +
                    "            ),\n" +
                    "            FLOOR(RAND() * 200), -- 0~199 사이의 좋아요 수\n" +
                    "            -- 90%는 PUBLIC, 10%는 다른 상태\n" +
                    "            CASE\n" +
                    "                WHEN RAND() < 0.9 THEN 'PUBLIC'\n" +
                    "                WHEN RAND() >= 0.9 AND RAND() < 0.95 THEN 'PRIVATE'\n" +
                    "                ELSE 'DELETED'\n" +
                    "            END,\n" +
                    "            random_user_id\n" +
                    "        );\n" +
                    "        \n" +
                    "        -- 5000개 단위로 커밋\n" +
                    "        IF i % 5000 = 0 THEN\n" +
                    "            COMMIT;\n" +
                    "            START TRANSACTION;\n" +
                    "            SELECT CONCAT('생성 진행 중... ', i, '/', last_id + total_records) AS progress;\n" +
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
        return "generate_post_data";
    }
}