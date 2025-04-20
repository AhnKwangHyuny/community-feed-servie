package org.faddy.community_feed.common.database.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.database.procedure.ProcedureCreator;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.test-data.user.count:500000}")
    private int userDataCount;

    @Value("${app.test-data.post.enabled:false}")
    private boolean postDataEnabled;

    @Value("${app.test-data.post.count:500000}")
    private int postDataCount;

    /**
     * 모든 프로시저를 생성
     */
    public void createAllProcedures() {
        for (ProcedureCreator creator : procedureCreators) {
            creator.createProcedureIfNotExists();
        }
    }

    /**
     * 설정에 따라 필요한 테스트 데이터 생성
     */
    public void generateAllTestData() {
        // 모든 프로시저 생성
        createAllProcedures();

        // 사용자 데이터 생성이 활성화된 경우
        if (userDataEnabled) {
            generateUserData(userDataCount);
        }

        // 게시물 데이터 생성이 활성화된 경우
        if (postDataEnabled) {
            generatePostData(postDataCount);
        }
    }

    /**
     * 사용자 데이터 생성
     * @param count 생성할 사용자 수
     */
    public void generateUserData(int count) {
        log.info("사용자 테스트 데이터 {} 개 생성 시작...", count);
        jdbcTemplate.execute("CALL generate_user_data(" + count + ")");
        log.info("사용자 테스트 데이터 생성 완료!");
    }

    /**
     * 게시물 데이터 생성
     * @param count 생성할 게시물 수
     */
    public void generatePostData(int count) {
        log.info("게시물 테스트 데이터 {} 개 생성 시작...", count);
        jdbcTemplate.execute("CALL generate_post_data(" + count + ")");
        log.info("게시물 테스트 데이터 생성 완료!");
    }
}