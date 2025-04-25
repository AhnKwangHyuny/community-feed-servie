import api, { ApiResponse } from './api';
import { GetPostContentResponseDto, CommentDto } from '../types/post';

export interface CreatePostRequestDto {
  content: string;
  state: string;
  imageIds?: number[]; // 이미지 ID 목록 추가
}

export interface UpdatePostRequestDto {
  content: string;
  state?: string;
  imageIds?: number[]; // 이미지 ID 목록 추가
}

export interface LikeRequestDto {
  targetId: number;
}

export interface ImageUploadResponseDto {
  id: number;
  url: string;
  filename: string;
  contentType: string;
  size: number;
  type: string;
  bucketPath: string;
}

export interface ImageUploadRequestDto {
  type: string;
}

/**
 * 포스트 관련 API 호출 함수들을 제공하는 서비스
 */
const postService = {
  /**
   * 새 포스트를 생성하는 함수
   * @param data 포스트 생성 데이터
   * @returns 생성된 포스트 ID
   */
  async createPost(data: CreatePostRequestDto): Promise<number> {
    try {
      console.log('포스트 생성 요청:', data);
      const response = await api.post<ApiResponse<number>>('/api/posts', data);
      console.log('포스트 생성 응답:', response.data);
      
      // response.data는 {code: 0, message: "ok", data: 123} 형식 
      if (!response.data.data && !response.data.value) {
        throw new Error('포스트 생성 실패: 서버에서 ID를 반환하지 않았습니다');
      }
      return response.data.data ?? (response.data.value as number);
    } catch (error) {
      console.error('포스트 생성 에러:', error);
      throw error;
    }
  },

  /**
   * 이미지를 업로드하는 함수
   * @param file 업로드할 이미지 파일
   * @param type 이미지 타입 (POST, PROFILE 등)
   * @returns 업로드된 이미지 정보
   */
  async uploadImage(file: File, type: string = 'POST'): Promise<ImageUploadResponseDto> {
    try {
      console.log('단일 이미지 업로드 호출:', file.name, type);
      
      // FormData와 JSON을 함께 보내기 위한 처리
      const formData = new FormData();
      formData.append('file', file);
      
      // type 정보를 JSON으로 전송
      const typeData: ImageUploadRequestDto = { type };
      
      console.log('단일 이미지 업로드 요청 URL:', '/api/images/upload');
      
      // 두 부분의 요청을 합쳐서 보내기 (멀티파트는 파일만, 타입은 JSON으로)
      const response = await api.post<ApiResponse<ImageUploadResponseDto>>(
        '/api/images/upload',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          params: typeData // URL 쿼리 파라미터로 타입 정보 전달
        }
      );
      
      console.log('단일 이미지 업로드 응답:', response.data);
      
      if (!response.data.data && !response.data.value) {
        throw new Error('이미지 업로드 실패: 서버에서 응답이 없습니다');
      }
      
      // data 또는 value 필드에서 응답 데이터 추출
      return response.data.data || response.data.value;
    } catch (error) {
      console.error('이미지 업로드 에러:', error);
      throw error;
    }
  },
  
  /**
   * 여러 이미지를 업로드하는 함수
   * @param files 업로드할 이미지 파일 배열
   * @param type 이미지 타입 (POST, PROFILE 등)
   * @returns 업로드된 이미지 정보 배열
   */
  async uploadMultipleImages(files: File[], type: string = 'POST'): Promise<ImageUploadResponseDto[]> {
    try {
      // 파일이 1개인 경우 단일 이미지 업로드 API 사용
      if (files.length === 1) {
        const imageResponse = await this.uploadImage(files[0], type);
        return [imageResponse];
      }
      
      // 여러 파일인 경우 다중 이미지 업로드 API 사용
      const formData = new FormData();
      files.forEach(file => {
        formData.append('files', file);
      });
      
      // type 정보를 JSON으로 전송
      const typeData: ImageUploadRequestDto = { type };
      
      // 두 부분의 요청을 합쳐서 보내기 (멀티파트는 파일만, 타입은 JSON으로)
      const response = await api.post<ApiResponse<ImageUploadResponseDto[]>>(
        '/api/images/upload/multiple',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          params: typeData // URL 쿼리 파라미터로 타입 정보 전달
        }
      );
      
      if (!response.data.data || response.data.data.length === 0) {
        throw new Error('이미지 업로드 실패: 서버에서 응답이 없습니다');
      }
      
      return response.data.data;
    } catch (error) {
      console.error('다중 이미지 업로드 에러:', error);
      throw error;
    }
  },

  /**
   * 임시 이미지를 삭제하는 함수
   * @param imageId 삭제할 이미지 ID
   */
  async deleteImage(imageId: number): Promise<void> {
    try {
      await api.delete<ApiResponse<null>>(`/api/images/${imageId}`);
    } catch (error) {
      console.error('이미지 삭제 에러:', error);
      throw error;
    }
  },

  /**
   * 포스트를 수정하는 함수
   * @param postId 수정할 포스트 ID
   * @param data 포스트 수정 데이터
   * @returns 수정된 포스트 ID
   */
  async updatePost(postId: number, data: UpdatePostRequestDto): Promise<number> {
    try {
      const response = await api.patch<ApiResponse<number>>(`/api/posts/${postId}`, data);
      if (!response.data.data && !response.data.value) {
        throw new Error('포스트 수정 실패: 서버에서 ID를 반환하지 않았습니다');
      }
      return response.data.data ?? (response.data.value as number);
    } catch (error) {
      console.error('포스트 수정 에러:', error);
      throw error;
    }
  },

  /**
   * 포스트 좋아요 함수
   * @param postId 좋아요할 포스트 ID
   */
  async likePost(postId: number): Promise<void> {
    try {
      const dto: LikeRequestDto = { targetId: postId };
      await api.post<ApiResponse<null>>('/api/posts/like', dto);
    } catch (error) {
      console.error('좋아요 에러:', error);
      throw error;
    }
  },

  /**
   * 포스트 좋아요 취소 함수
   * @param postId 좋아요 취소할 포스트 ID
   */
  async unlikePost(postId: number): Promise<void> {
    try {
      const dto: LikeRequestDto = { targetId: postId };
      await api.post<ApiResponse<null>>('/api/posts/unlike', dto);
    } catch (error) {
      console.error('좋아요 취소 에러:', error);
      throw error;
    }
  },

  /**
   * 포스트의 댓글 목록을 가져오는 함수
   * @param postId 포스트 ID
   * @returns 댓글 목록
   */
  async getComments(postId: number): Promise<CommentDto[]> {
    try {
      const response = await api.get<ApiResponse<CommentDto[]>>(`/api/posts/${postId}/comments`);
      return response.data.data || [];
    } catch (error) {
      console.error('댓글 목록 조회 에러:', error);
      throw error;
    }
  },
  
  /**
   * 포스트 상세 정보를 가져오는 함수
   * @param postId 포스트 ID
   * @returns 포스트 상세 정보
   */
  async getPostDetail(postId: number): Promise<any> {
    try {
      console.log(`getPostDetail 호출됨, postId: ${postId}, URL: /api/posts/detail/${postId}`);
      const response = await api.get<ApiResponse<any>>(`/api/posts/detail/${postId}`);
      console.log('API 응답:', response);
      return response.data.data || response.data.value || null;
    } catch (error) {
      console.error('포스트 상세 조회 에러:', error);
      throw error;
    }
  }
};

export default postService;