package org.faddy.community_feed.image.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadRequestDto {
    private String type;  // 이미지 타입 (POST, PROFILE 등)
    // 필요시 추가 메타데이터
}
