import api, { ApiResponse } from './api';
import { CommentDto } from '../types/post';

export interface CreateCommentRequestDto {
  postId: number;
  content: string;
}

/**
 * 댓글 관련 API 호출 함수들을 제공하는 서비스
 */
const commentService = {
  /**
   * 새 댓글을 생성하는 함수
   * @param data 댓글 생성 데이터
   * @returns 생성된 댓글 정보
   */
  async createComment(data: CreateCommentRequestDto): Promise<CommentDto> {
    try {
      console.log('댓글 생성 요청:', data);
      const response = await api.post<ApiResponse<CommentDto>>('/comment', data);
      console.log('댓글 생성 응답:', response.data);
      
      return response.data.data;
    } catch (error) {
      console.error('댓글 생성 에러:', error);
      throw error;
    }
  },

  /**
   * 댓글을 삭제하는 함수
   * @param commentId 삭제할 댓글 ID
   */
  async deleteComment(commentId: number): Promise<void> {
    try {
      await api.delete<ApiResponse<void>>(`/comment/${commentId}`);
    } catch (error) {
      console.error('댓글 삭제 에러:', error);
      throw error;
    }
  }
};

export default commentService; 