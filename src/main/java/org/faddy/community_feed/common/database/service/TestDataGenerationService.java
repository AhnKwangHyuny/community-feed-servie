package org.faddy.community_feed.common.database.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.database.procedure.ProcedureCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDataGenerationService {

    private final JdbcTemplate jdbcTemplate;
    private final List<ProcedureCreator> procedureCreators;

    @Value("${app.test-data.user.enabled:false}")
    private boolean userDataEnabled;

    @Value("${app.test-data.user.count:1000}")
    private int userDataCount;

    @Value("${app.test-data.post.enabled:false}")
    private boolean postDataEnabled;

    @Value("${app.test-data.post.count:1000}")
    private int postDataCount;

    /**
     * 모든 프로시저를 생성
     */
    public void createAllProcedures() {
        try {
            for (ProcedureCreator creator : procedureCreators) {
                creator.createProcedureIfNotExists();
            }
            log.info("모든 저장 프로시저가 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            log.error("저장 프로시저 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("저장 프로시저 생성 실패", e);
        }
    }

    /**
     * 설정에 따라 필요한 테스트 데이터 생성
     */
    public void generateAllTestData() {
        try {
            // 모든 프로시저 생성
            createAllProcedures();

            // 사용자 데이터 생성이 활성화된 경우
            if (userDataEnabled) {
                generateUserData(userDataCount);

                // 사용자 데이터가 실제로 생성되었는지 확인
                Integer userCount = verifyUserData();
                log.info("현재 사용자 데이터 수: {}", userCount);

                if (userCount == null || userCount == 0) {
                    log.error("사용자 데이터 생성이 실패했거나 데이터가 없습니다. 게시물 데이터 생성을 건너뜁니다.");
                    return;
                }
            } else {
                // 사용자 데이터 생성이 비활성화된 경우에도 기존 사용자 데이터가 있는지 확인
                Integer userCount = verifyUserData();
                log.info("현재 사용자 데이터 수: {}", userCount);

                if (userCount == null || userCount == 0) {
                    log.error("사용자 데이터가 없습니다. 게시물 데이터 생성을 건너뜁니다.");
                    return;
                }
            }

            // 게시물 데이터 생성이 활성화된 경우
            if (postDataEnabled) {
                generatePostData(postDataCount);
            }
        } catch (Exception e) {
            log.error("테스트 데이터 생성 중 오류 발생: {}", e.getMessage(), e);
            // 오류를 로그로 기록하지만 애플리케이션이 중단되지 않도록 예외를 다시 던지지 않음
        }
    }

    /**
     * 사용자 데이터 검증
     * @return 현재 데이터베이스의 사용자 수
     */
    private Integer verifyUserData() {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM community_user", Integer.class);
        } catch (DataAccessException e) {
            log.error("사용자 데이터 확인 중 오류 발생: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 사용자 데이터 생성
     * @param count 생성할 사용자 수
     */
    public void generateUserData(int count) {
        try {
            log.info("사용자 테스트 데이터 {} 개 생성 시작...", count);
            jdbcTemplate.execute("CALL generate_user_data(" + count + ")");
            log.info("사용자 테스트 데이터 생성 완료!");
        } catch (DataAccessException e) {
            log.error("사용자 데이터 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("사용자 데이터 생성 실패", e);
        }
    }

    /**
     * 게시물 데이터 생성
     * @param count 생성할 게시물 수
     */
    public void generatePostData(int count) {
        try {
            log.info("게시물 테스트 데이터 {} 개 생성 시작...", count);
            jdbcTemplate.execute("CALL generate_post_data(" + count + ")");
            log.info("게시물 테스트 데이터 생성 완료!");
        } catch (DataAccessException e) {
            log.error("게시물 데이터 생성 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("게시물 데이터 생성 실패", e);
        }
    }
}