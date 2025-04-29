import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../../services/api';
import { GetPostContentResponseDto } from '../../types/post';
import { useAuth } from '../../context/AuthContext';
import '../../styles/Post.css';

interface PostProps {
  post: GetPostContentResponseDto;
  onLike: (postId: number, liked: boolean) => void;
}

const Post: React.FC<PostProps> = ({ post, onLike }) => {
  const [isLiking, setIsLiking] = useState(false);
  const { user } = useAuth();
  
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);

    // 1일 이내면 시간 표시
    if (diffDay < 1) {
      if (diffHour < 1) {
        if (diffMin < 1) {
          return '방금 전';
        }
        return `${diffMin}분 전`;
      }
      return `${diffHour}시간 전`;
    }
    
    // 오늘이 아니면 날짜 표시
    return date.toLocaleDateString('ko-KR', { 
      year: 'numeric', 
      month: '2-digit', 
      day: '2-digit' 
    });
  };

  const handleLike = async () => {
    if (!user) {
      alert('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    if (isLiking) return; // 중복 요청 방지

    try {
      setIsLiking(true);
      const endpoint = post.isLikedByMe ? '/api/posts/unlike' : '/api/posts/like';
      
      await api.post(endpoint, {
        targetId: post.id
      });
      
      onLike(post.id, !post.isLikedByMe);
    } catch (error) {
      console.error('좋아요 처리 중 오류:', error);
      alert('좋아요 처리 중 오류가 발생했습니다.');
    } finally {
      setIsLiking(false);
    }
  };

  // 다중 이미지 변환 (여러 형식의 이미지 필드 지원)
  const getPostImages = (): string[] => {
    // thumbnails 배열이 있으면 URL 추출
    if (post.thumbnails && post.thumbnails.length > 0) {
      return post.thumbnails.map(thumbnail => thumbnail.url);
    }
    
    // 이전 버전 호환성을 위한 코드
    if (post.images && post.images.length > 0) {
      return post.images;
    }
    
    if (post.thumbnailUrl) {
      return [post.thumbnailUrl];
    }
    
    return [];
  };

  // 프로필 이미지가 없는 경우 기본 이미지 사용
  const profileImageUrl = post.userProfileImage || 'https://via.placeholder.com/40';

  // 이미지 목록 가져오기
  const postImages = getPostImages();
  const hasImages = postImages.length > 0;

  // 내용 길이에 따라 더보기 표시
  const isLongContent = post.content.length > 100;
  const displayContent = isLongContent 
    ? post.content.substring(0, 100) + '...' 
    : post.content;

  return (
    <article className="post-card">
      {hasImages && (
        <div className="post-thumbnail-container">
          <Link to={`/post/detail/${post.id}`} className="post-thumbnail-link">
            <img 
              src={postImages[0]} 
              alt="게시물 썸네일" 
              className="post-thumbnail" 
              onError={(e) => {
                (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=이미지+로드+실패';
              }}
            />
            {postImages.length > 1 && (
              <div className="multiple-images-badge">+{postImages.length - 1}</div>
            )}
          </Link>
          <button 
            className={`post-like-button ${post.isLikedByMe ? 'liked' : ''}`}
            onClick={handleLike}
            disabled={isLiking}
          >
            <span className="heart-icon">{post.isLikedByMe ? '❤️' : '🤍'}</span>
            <span className="like-count">{post.likeCount || 0}</span>
          </button>
        </div>
      )}
      
      <div className="post-content-container">
        <div className="post-header">
          <Link to={`/profile/${post.userId}`} className="post-author-link">
            <img 
              src={profileImageUrl} 
              alt={`${post.userName}의 프로필`} 
              className="post-author-avatar" 
              onError={(e) => {
                (e.target as HTMLImageElement).src = 'https://via.placeholder.com/40?text=오류';
              }}
            />
            <span className="post-author-name">{post.userName}</span>
          </Link>
          <span className="post-date">{formatDate(post.createdAt)}</span>
        </div>
        
        <div className="post-body">
          <Link to={`/post/detail/${post.id}`} className="post-content-link">
            <p className="post-content">{displayContent}</p>
            {isLongContent && <span className="read-more">더 보기</span>}
          </Link>
          
          {!hasImages && (
            <div className="post-actions">
              <button 
                className={`post-like-button-text ${post.isLikedByMe ? 'liked' : ''}`}
                onClick={handleLike}
                disabled={isLiking}
              >
                {post.isLikedByMe ? '❤️' : '🤍'} {post.likeCount || 0}
              </button>
              {post.commentCount > 0 && (
                <Link to={`/post/detail/${post.id}`} className="post-comment-link">
                  💬 {post.commentCount}
                </Link>
              )}
            </div>
          )}
        </div>
      </div>
    </article>
  );
};

export default Post;