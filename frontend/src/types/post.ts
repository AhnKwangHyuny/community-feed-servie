export interface GetContentResponseDto {
  id: number;
  content: string;
  userId: number;
  userName: string;
  userProfileImage: string;
  createdAt: string;
  updatedAt: string;
  likeCount: number;
  isLikedByMe: boolean;
}

export interface GetPostContentResponseDto extends GetContentResponseDto {
  commentCount: number;
  thumbnailUrl?: string;
  images?: string[]; // 다중 이미지 지원을 위한 필드 추가
  viewCount?: number;
}

export interface CreatePostRequestDto {
  content: string;
  state: string;  // state가 필수값이 되었습니다
  imageIds?: number[];  // 게시물에 첨부할 이미지 ID 목록
}

export interface UpdatePostRequestDto {
  content: string;
  state?: string;
  imageIds?: number[]; // 이미지 업데이트 지원
}

export interface LikeRequestDto {
  targetId: number;
}

export interface CommentDto {
  id: number;
  content: string;
  postId: number;
  userId: number;
  userName: string;
  userProfileImage?: string;
  createdAt: string;
  updatedAt?: string;
  likeCount: number;
  isLikedByMe: boolean;
  parentId?: number;  // 대댓글 기능을 위한 부모 댓글 ID
  isBestComment?: boolean; // 베스트 댓글 표시를 위한 필드 추가
}

export interface CreateCommentRequestDto {
  userId: number;
  postId: number;
  content: string;
  parentId?: number; // 대댓글을 위한 부모 댓글 ID
}

export interface UpdateCommentRequestDto {
  userId: number;
  content: string;
}