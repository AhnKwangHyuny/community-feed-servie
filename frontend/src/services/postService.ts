import api, { ApiResponse } from './api';
import { GetPostContentResponseDto, CommentDto } from '../types/post';

export interface CreatePostRequestDto {
  content: string;
  state: string;
}

export interface UpdatePostRequestDto {
  content: string;
  state?: string;
}

export interface LikeRequestDto {
  targetId: number;
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
      const response = await api.post<ApiResponse<number>>('/post', data);
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
   * 포스트를 수정하는 함수
   * @param postId 수정할 포스트 ID
   * @param data 포스트 수정 데이터
   * @returns 수정된 포스트 ID
   */
  async updatePost(postId: number, data: UpdatePostRequestDto): Promise<number> {
    try {
      const response = await api.patch<ApiResponse<number>>(`/post/${postId}`, data);
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
      await api.post<ApiResponse<null>>('/post/like', dto);
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
      await api.post<ApiResponse<null>>('/post/unlike', dto);
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
      const response = await api.get<ApiResponse<CommentDto[]>>(`/post/${postId}/comments`);
      return response.data.data || [];
    } catch (error) {
      console.error('댓글 목록 조회 에러:', error);
      throw error;
    }
  }
};

export default postService; 