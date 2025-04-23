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
  viewCount?: number;
}

export interface CreatePostRequestDto {
  content: string;
  state: string;  // state가 필수값이 되었습니다
}

export interface UpdatePostRequestDto {
  content: string;
  state?: string;
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
}

export interface CreateCommentRequestDto {
  userId: number;
  postId: number;
  content: string;
}

export interface UpdateCommentRequestDto {
  userId: number;
  content: string;
}