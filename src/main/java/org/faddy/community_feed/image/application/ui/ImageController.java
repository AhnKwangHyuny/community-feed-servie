package org.faddy.community_feed.image.application.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.common.principal.AuthPrincipal;
import org.faddy.community_feed.common.principal.UserPrincipal;
import org.faddy.community_feed.common.ui.Response;

import org.faddy.community_feed.image.application.dto.request.ImageUploadRequestDto;
import org.faddy.community_feed.image.application.dto.response.ImageUploadResponseDto;
import org.faddy.community_feed.image.application.interfaces.ImageUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    /**
     * 이미지 업로드 API
     */
    @PostMapping("/upload")
    public Response<ImageUploadResponseDto> uploadImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("type") String type,
        @AuthPrincipal UserPrincipal principal) {

        log.info("Image upload request received. User: {}, File: {}, Type: {}",
            principal.getUserId(), file.getOriginalFilename(), type);

        ImageUploadResponseDto responseDto = imageUploadService.uploadTemporaryImage(
            file, type, principal.getUserId());

        return Response.ok(responseDto);
    }

    /**
     * 다중 이미지 업로드 API
     */
    @PostMapping("/upload/multiple")
    public Response<List<ImageUploadResponseDto>> uploadMultipleImages(
        @RequestParam("files") List<MultipartFile> files,
        @RequestParam("type") String type,
        @AuthPrincipal UserPrincipal principal) {

        List<ImageUploadResponseDto> responseDtos = imageUploadService.uploadMultipleTemporaryImages(
            files, type, principal.getUserId());

        return Response.ok(responseDtos);
    }

    /**
     * 임시 이미지 삭제 API
     */
    @DeleteMapping("/{imageId}")
    public Response<Void> deleteImage(
        @PathVariable Long imageId,
        @AuthPrincipal UserPrincipal principal) {

        imageUploadService.deleteTemporaryImage(imageId, principal.getUserId());

        return Response.ok(null);
    }
}