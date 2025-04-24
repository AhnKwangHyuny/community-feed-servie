package org.faddy.community_feed.image.infra.shceduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCleanupScheduler {

    private final JobLauncher jobLauncher;
    private final Job temporaryImageCleanupJob;

    /**
     * 30분마다 실행되는 임시 이미지 정리 스케줄러
     */
    @Scheduled(fixedRate = 1800000) // 30분(1800초)마다 실행
    public void scheduleTemporaryImageCleanup() {
        try {
            log.info("Starting temporary image cleanup job at {}", LocalDateTime.now());

            // 각 실행마다 고유한 JobParameters 생성
            JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

            // 배치 작업 실행
            jobLauncher.run(temporaryImageCleanupJob, jobParameters);

        } catch (Exception e) {
            log.error("Error during temporary image cleanup job", e);
        }
    }
}