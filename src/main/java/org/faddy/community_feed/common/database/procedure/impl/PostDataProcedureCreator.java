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

        // 존재하면 삭제 (재생성을 위해)
        if (count != null && count > 0) {
            jdbcTemplate.execute("DROP PROCEDURE IF EXISTS " + getProcedureName());
            System.out.println("'" + getProcedureName() + "' 저장 프로시저를 삭제했습니다.");
        }

        // 프로시저 생성
        jdbcTemplate.execute(
            "CREATE PROCEDURE generate_post_data(IN total_records INT)\n" +
                "BEGIN\n" +
                "    DECLARE i INT DEFAULT 1;\n" +
                "    DECLARE j INT;\n" +
                "    DECLARE last_post_id INT;\n" +
                "    DECLARE last_image_id INT;\n" +
                "    DECLARE last_thumbnail_id INT;\n" +
                "    DECLARE user_count INT;\n" +
                "    DECLARE random_user_id INT;\n" +
                "    DECLARE min_user_id INT;\n" +
                "    DECLARE max_user_id INT;\n" +
                "    DECLARE img_count INT;\n" +
                "    DECLARE post_created_at DATETIME;\n" +
                "    DECLARE image_url VARCHAR(255);\n" +
                "    DECLARE original_filename VARCHAR(255);\n" +
                "    \n" +
                "    -- 오류 처리 핸들러 설정\n" +
                "    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION\n" +
                "    BEGIN\n" +
                "        ROLLBACK;\n" +
                "        GET DIAGNOSTICS CONDITION 1 @sqlstate = RETURNED_SQLSTATE, @errno = MYSQL_ERRNO, @text = MESSAGE_TEXT;\n" +
                "        SELECT CONCAT('SQL 오류 발생. 상태: ', @sqlstate, ', 오류코드: ', @errno, ', 메시지: ', @text) AS error_message;\n" +
                "    END;\n" +
                "    \n" +
                "    -- 레이블 정의 (이 부분이 없어서 오류 발생)\n" +
                "    data_generation_block: BEGIN\n" +
                "    \n" +
                "    -- 사용자 ID 범위 확인\n" +
                "    SELECT MIN(id), MAX(id), COUNT(*) INTO min_user_id, max_user_id, user_count FROM community_user;\n" +
                "    \n" +
                "    -- 사용자가 존재하지 않으면 중단\n" +
                "    IF user_count = 0 THEN\n" +
                "        SELECT 'ERROR: 사용자 데이터가 없습니다. 먼저 사용자 데이터를 생성하세요.' AS error_message;\n" +
                "        LEAVE data_generation_block;\n" +
                "    END IF;\n" +
                "    \n" +
                "    -- 마지막 게시물 ID 확인\n" +
                "    SELECT IFNULL(MAX(id), 0) INTO last_post_id FROM community_post;\n" +
                "    \n" +
                "    -- 마지막 이미지 ID 확인\n" +
                "    SELECT IFNULL(MAX(id), 0) INTO last_image_id FROM community_image;\n" +
                "    \n" +
                "    -- 마지막 썸네일 ID 확인\n" +
                "    SELECT IFNULL(MAX(id), 0) INTO last_thumbnail_id FROM community_post_thumbnail;\n" +
                "    \n" +
                "    -- 시작 인덱스 설정\n" +
                "    SET i = last_post_id + 1;\n" +
                "    \n" +
                "    -- 트랜잭션 시작\n" +
                "    START TRANSACTION;\n" +
                "    \n" +
                "    WHILE i <= (last_post_id + total_records) DO\n" +
                "        -- 랜덤 사용자 ID 선택\n" +
                "        SET random_user_id = min_user_id + FLOOR(RAND() * (max_user_id - min_user_id + 1));\n" +
                "        \n" +
                "        -- 사용자 ID가 존재하는지 확인\n" +
                "        WHILE NOT EXISTS (SELECT 1 FROM community_user WHERE id = random_user_id) DO\n" +
                "            SET random_user_id = min_user_id + FLOOR(RAND() * (max_user_id - min_user_id + 1));\n" +
                "        END WHILE;\n" +
                "        \n" +
                "        -- 게시물 생성 시간 설정\n" +
                "        SET post_created_at = DATE_FORMAT(\n" +
                "            DATE_ADD('2024-01-01 00:00:00', INTERVAL FLOOR(RAND() * 120 * 24 * 60 * 60) SECOND),\n" +
                "            '%Y-%m-%d %H:%i:%s'\n" +
                "        );\n" +
                "        \n" +
                "        -- community_post 테이블에 데이터 삽입\n" +
                "        INSERT INTO community_post (\n" +
                "            id, reg_dt, upd_dt, comment_counter, content, like_count, view_counter, state, author_id\n" +
                "        ) VALUES (\n" +
                "            i,\n" +
                "            post_created_at,\n" +
                "            post_created_at,\n" +
                "            FLOOR(RAND() * 50), -- 0~49 사이의 댓글 수\n" +
                "            CONCAT(\n" +
                "                ELT(FLOOR(1 + RAND() * 10), \n" +
                "                    '오늘 날씨가 정말 좋네요! 산책하기 딱 좋은 날이에요.', \n" +
                "                    '새로운 프로젝트를 시작했어요. 열심히 개발 중입니다!', \n" +
                "                    '이번 주말에 가족들과 여행 다녀왔어요. 정말 즐거웠습니다.', \n" +
                "                    '맛있는 음식점을 발견했습니다. 다들 한번 가보세요!', \n" +
                "                    '오랜만에 영화 보러 갔어요. 정말 재미있었습니다.', \n" +
                "                    '공부하는 중입니다... 모두 화이팅하세요!', \n" +
                "                    '커피 한 잔의 여유~ 오늘 하루도 행복하게!', \n" +
                "                    '오늘의 일기를 씁니다. 행복한 하루였어요.', \n" +
                "                    '새로운 취미를 시작했어요! 정말 재미있네요.', \n" +
                "                    '열심히 살아가는 중입니다. 모두들 건강하세요.'\n" +
                "                ),\n" +
                "                ' ',\n" +
                "                ELT(FLOOR(1 + RAND() * 10), \n" +
                "                    '모두들 행복한 하루 되세요! 오늘도 화이팅입니다.', \n" +
                "                    '여러분은 어떻게 지내고 계신가요? 좋은 일만 가득하길 바랍니다.', \n" +
                "                    '오늘도 화이팅! 모두 건강하시고 행복한 하루 되세요.', \n" +
                "                    '좋은 하루 보내세요~ 항상 응원합니다!', \n" +
                "                    '감사합니다. 여러분의 소중한 시간에 감사드립니다.', \n" +
                "                    '열심히 해봅시다! 노력은 배신하지 않습니다.', \n" +
                "                    '곧 주말이네요! 모두 즐거운 주말 계획 세우세요.', \n" +
                "                    '다들 건강 조심하세요. 건강이 최고의 자산입니다.', \n" +
                "                    '요즘 너무 바빠요... 그래도 열심히 살아갑니다.', \n" +
                "                    '행복합니다! 여러분도 행복한 하루 되세요!'\n" +
                "                )\n" +
                "            ),\n" +
                "            FLOOR(RAND() * 200), -- 0~199 사이의 좋아요 수\n" +
                "            FLOOR(RAND() * 500), -- 0~499 사이의 조회수\n" +
                "            CASE\n" +
                "                WHEN RAND() < 0.8 THEN 'PUBLIC'\n" +
                "                WHEN RAND() < 0.9 THEN 'ONLY_FOLLOWER'\n" +
                "                ELSE 'PRIVATE'\n" +
                "            END,\n" +
                "            random_user_id\n" +
                "        );\n" +
                "        \n" +
                "        -- 각 게시물에 1~3장의 이미지 추가\n" +
                "        SET img_count = 1 + FLOOR(RAND() * 3);\n" +
                "        SET j = 1;\n" +
                "        \n" +
                "        WHILE j <= img_count DO\n" +
                "            -- 이미지 ID 증가\n" +
                "            SET last_image_id = last_image_id + 1;\n" +
                "            \n" +
                "            -- 이미지 URL 및 파일명 설정\n" +
                "            SET image_url = CASE FLOOR(1 + RAND() * 5)\n" +
                "                WHEN 1 THEN '/static/images/test/image1.jpeg'\n" +
                "                WHEN 2 THEN '/static/images/test/image2.jpeg'\n" +
                "                WHEN 3 THEN '/static/images/test/image3.jpeg'\n" +
                "                WHEN 4 THEN '/static/images/test/image4.jpeg'\n" +
                "                ELSE '/static/images/test/test_default_image.png'\n" +
                "            END;\n" +
                "            \n" +
                "            SET original_filename = CASE FLOOR(1 + RAND() * 5)\n" +
                "                WHEN 1 THEN 'image1.jpeg'\n" +
                "                WHEN 2 THEN 'image2.jpeg'\n" +
                "                WHEN 3 THEN 'image3.jpeg'\n" +
                "                WHEN 4 THEN 'image4.jpeg'\n" +
                "                ELSE 'default.png'\n" +
                "            END;\n" +
                "            \n" +
                "            -- community_image 테이블에 이미지 데이터 삽입 (status와 bucket_path 필드 포함)\n" +
                "            INSERT INTO community_image (\n" +
                "                id, reg_dt, upd_dt, url, original_filename, content_type, size, type, status, bucket_path\n" +
                "            ) VALUES (\n" +
                "                last_image_id,\n" +
                "                post_created_at,\n" +
                "                post_created_at,\n" +
                "                image_url,\n" +
                "                original_filename,\n" +
                "                CASE\n" +
                "                    WHEN original_filename LIKE '%.png' THEN 'image/png'\n" +
                "                    ELSE 'image/jpeg'\n" +
                "                END,\n" +
                "                FLOOR(100000 + RAND() * 900000), -- 100KB~1MB 크기\n" +
                "                'POST', -- ImageType enum 값\n" +
                "                'PERMANENT', -- ImageStatus enum 값\n" +
                "                '/images/test/'\n" +
                "            );\n" +
                "            \n" +
                "            -- image_id가 고유(UNI) 제약조건을 가지므로, 이미지 사용 전 검증\n" +
                "            IF NOT EXISTS (SELECT 1 FROM community_post_thumbnail WHERE image_id = last_image_id) THEN\n" +
                "                -- 썸네일 ID 증가\n" +
                "                SET last_thumbnail_id = last_thumbnail_id + 1;\n" +
                "                \n" +
                "                -- community_post_thumbnail 테이블에 연결 정보 추가\n" +
                "                INSERT INTO community_post_thumbnail (\n" +
                "                    id, reg_dt, upd_dt, post_id, image_id, display_order, is_main\n" +
                "                ) VALUES (\n" +
                "                    last_thumbnail_id,\n" +
                "                    post_created_at,\n" +
                "                    post_created_at,\n" +
                "                    i,\n" +
                "                    last_image_id,\n" +
                "                    j,\n" +
                "                    IF(j = 1, true, false) -- 첫 번째 이미지만 메인 썸네일로 설정\n" +
                "                );\n" +
                "            ELSE\n" +
                "                -- 이미 사용된 이미지 ID라면 건너뛰기 (메시지 기록)\n" +
                "                SELECT CONCAT('이미지 ID ', last_image_id, '는 이미 사용 중입니다. 건너뜁니다.') AS warning;\n" +
                "                -- j를 줄여서 필요한 이미지 수를 맞추기 위해 반복\n" +
                "                SET j = j - 1;\n" +
                "            END IF;\n" +
                "            \n" +
                "            SET j = j + 1;\n" +
                "        END WHILE;\n" +
                "        \n" +
                "        -- 10개 단위로 커밋 (더 작은 단위로 조정)\n" +
                "        IF i % 10 = 0 THEN\n" +
                "            COMMIT;\n" +
                "            START TRANSACTION;\n" +
                "            SELECT CONCAT('게시물 생성 진행 중... ', i, '/', last_post_id + total_records) AS progress;\n" +
                "        END IF;\n" +
                "        \n" +
                "        SET i = i + 1;\n" +
                "    END WHILE;\n" +
                "    \n" +
                "    -- 남은 트랜잭션 커밋\n" +
                "    COMMIT;\n" +
                "    \n" +
                "    SELECT CONCAT('총 ', total_records, '개의 게시물과 약 ', (last_image_id - last_post_id), '개의 이미지가 생성되었습니다.') AS completion_message;\n" +
                "    END; -- data_generation_block의 끝\n" +
                "END"
        );
        System.out.println("'" + getProcedureName() + "' 저장 프로시저가 성공적으로 생성되었습니다.");
    }

    @Override
    public String getProcedureName() {
        return "generate_post_data";
    }
}