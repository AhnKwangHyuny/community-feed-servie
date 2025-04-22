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
  userId: number;
  content: string;
}

export interface UpdatePostRequestDto {
  userId: number;
  content: string;
  state?: string;
}

export interface LikeRequestDto {
  userId: number;
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