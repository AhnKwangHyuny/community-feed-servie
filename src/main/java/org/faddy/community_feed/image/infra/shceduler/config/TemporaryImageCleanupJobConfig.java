package org.faddy.community_feed.image.infra.shceduler.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.image.domain.ImageStatus;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
import org.faddy.community_feed.image.repository.jpa.JpaImageRepository;
import org.faddy.community_feed.infra.s3.application.S3Service;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TemporaryImageCleanupJobConfig {

    /**
     *  요구 사항
     *  1. TEMPORARY 이미지를 30개 단위로 청크 처리
     *  2. 2시간 이상 경과된 TEMPORARY 상태 이미지 검색
     *  3. S3에서 파일 삭제 후 DB에서도 제거
     * */

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final S3Service s3Service;
    private final JpaImageRepository jpaImageRepository;

    // 한 번에 처리할 이미지 수
    private static final int CHUNK_SIZE = 30;

    @Bean
    public Job temporaryImageCleanupJob() {
        return new JobBuilder("temporaryImageCleanupJob", jobRepository)
            .start(temporaryImageCleanupStep())
            .build();
    }

    @Bean
    public Step temporaryImageCleanupStep() {
        return new StepBuilder("temporaryImageCleanupStep", jobRepository)
            .<ImageEntity, ImageEntity>chunk(CHUNK_SIZE, transactionManager)
            .reader(temporaryImageReader())
            .processor(temporaryImageProcessor())
            .writer(temporaryImageWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<ImageEntity> temporaryImageReader() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(2); // 2시간 이전 이미지

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", ImageStatus.TEMPORARY);
        parameters.put("cutoffTime", cutoffTime);

        return new JpaPagingItemReaderBuilder<ImageEntity>()
            .name("temporaryImageReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT i FROM ImageEntity i WHERE i.status = :status AND i.regDt < :cutoffTime")
            .parameterValues(parameters)
            .pageSize(CHUNK_SIZE)
            .build();
    }

    @Bean
    public ItemProcessor<ImageEntity, ImageEntity> temporaryImageProcessor() {
        return image -> {
            try {
                // S3에서 파일 삭제
                String bucketPath = image.getBucketPath();
                if (bucketPath != null && !bucketPath.isEmpty()) {
                    log.info("Deleting S3 file: {}", bucketPath);
                    s3Service.deleteFile(bucketPath);
                }
                return image;
            } catch (Exception e) {
                log.error("Failed to delete S3 file for image ID {}: {}", image.getId(), e.getMessage());
                return null; // null 반환 시 writer로 전달하지 않음
            }
        };
    }

    @Bean
    public ItemWriter<ImageEntity> temporaryImageWriter() {
        return items -> {
            if (items.isEmpty()) {
                log.info("No temporary images to delete");
                return;
            }

            log.info("Deleting {} temporary images from database", items.size());
            for (ImageEntity image : items) {
                log.debug("Deleting image: ID={}, URL={}", image.getId(), image.getUrl());
                jpaImageRepository.delete(image);
            }
        };
    }
}