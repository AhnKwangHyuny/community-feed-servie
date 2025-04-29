// src/types/post.ts

export interface ThumbnailDto {
  id: number;
  url: string;
  order: number;
}

export interface GetPostContentResponseDto {
  id: number;
  userId: number;
  userName: string;
  userProfileImage?: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  likeCount: number;
  viewCount: number;
  commentCount: number;
  isLikedByMe: boolean;
  
  // 이미지 관련 필드 (다양한 형태 지원)
  thumbnails?: ThumbnailDto[];
  thumbnailUrl?: string;
  images?: string[];
}

export interface PostCreateRequestDto {
  content: string;
  images?: File[];
}

export interface CommentDto {
  id: number;
  postId: number;
  userId: number;
  userName: string;
  userProfileImage?: string;
  content: string;
  createdAt: string;
  likeCount: number;
  isLikedByMe: boolean;
  parentId?: number;
  childComments?: CommentDto[];
  isBestComment?: boolean; // 베스트 댓글 표시를 위해 추가
}

export interface CommentCreateRequestDto {
  postId: number;
  content: string;
  parentId?: number;
}