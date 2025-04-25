import React, { useState } from 'react';
import api from '../../services/api';
import { Link } from 'react-router-dom';
import { GetPostContentResponseDto, CommentDto } from '../../types/post';
import { useAuth } from '../../context/AuthContext';
import CommentForm from './CommentForm';
import CommentList from './CommentList';
import ImageGrid from './ImageGrid'; // 이미지 그리드 컴포넌트 추가
import '../../styles/Post.css';

interface PostProps {
  post: GetPostContentResponseDto;
  onLike: (postId: number, liked: boolean) => void;
}

const Post: React.FC<PostProps> = ({ post, onLike }) => {
  const [showComments, setShowComments] = useState(false);
  const [comments, setComments] = useState<CommentDto[]>([]);
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();
  
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' });
  };

  const handleLike = async () => {
    if (!user) {
      alert('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    try {
      const endpoint = post.isLikedByMe ? '/api/posts/unlike' : '/api/posts/like';
      
      await api.post(endpoint, {
        targetId: post.id
      });
      
      onLike(post.id, !post.isLikedByMe);
    } catch (error) {
      console.error('좋아요 처리 중 오류:', error);
      alert('좋아요 처리 중 오류가 발생했습니다.');
    }
  };

  const handleCommentToggle = async () => {
    // 댓글이 이미 보이는 상태면 숨기기만 하고 API 호출 하지 않음
    if (showComments) {
      setShowComments(false);
      return;
    }
    
    try {
      setLoading(true);
      // 댓글 목록 API 호출 (현재는 비활성화)
      // const response = await api.get(`/api/posts/${post.id}/comments`);
      // setComments(response.data.data || []);
      
      // 댓글 기능이 완성되기 전까지는 빈 배열 사용
      setComments([]);
      setShowComments(true);
    } catch (error) {
      console.error('댓글을 불러오는 중 오류:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCommentSubmit = (newComment: any) => {
    setComments((prevComments) => [newComment, ...prevComments]);
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
    
    // 이미지가 없는 경우 기본 이미지 사용
    return ['https://via.placeholder.com/400x300'];
  };

  // 프로필 이미지가 없는 경우 기본 이미지 사용
  const profileImageUrl = post.userProfileImage || 'https://via.placeholder.com/100';

  // 피드에서는 첫 번째 이미지만 썸네일로 표시
  const thumbnailUrl = getPostImages()[0];

  return (
    <article className="post-card">
      <div className="thumbnail-container">
        <div className="post-link-container">
          <Link to={`/post/detail/${post.id}`} className="post-link">
            <img src={thumbnailUrl} alt="게시물 이미지" className="post-thumbnail" />
          </Link>
          <div 
            className={`post-like-count ${post.isLikedByMe ? 'liked' : ''}`}
            onClick={handleLike}
          >
            <span className="heart-icon-overlay">{post.isLikedByMe ? '❤️' : '🤍'}</span>
            <span className="like-count-overlay">{post.likeCount || 0}</span>
          </div>
        </div>
      </div>
      
      <div className="post-content-area">
        <div className="post-profile-section">
          <Link to={`/profile/${post.userId}`} className="post-profile-link">
            <img 
              src={profileImageUrl} 
              alt={`${post.userName}의 프로필`} 
              className="post-author-avatar" 
            />
          </Link>
          
          <div className="post-title-wrapper">
            <h3 className="post-title">
              <Link to={`/post/detail/${post.id}`}>
                {post.content.length > 50 
                  ? post.content.substring(0, 50) + '...' 
                  : post.content}
                {post.commentCount > 0 && (
                  <span className="post-comment-count"> [{post.commentCount}]</span>
                )}
              </Link>
            </h3>
            
            <div className="post-meta-info">
              <div className="post-author-name">{post.userName}</div>
              <div className="post-meta-stats">
                <span className="post-date">{formatDate(post.createdAt)}</span>
                <span className="post-like-info">♥ {post.likeCount || 0}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      {showComments && (
        <div className="post-comments">
          {user && (
            <CommentForm 
              postId={post.id} 
              onCommentSubmit={handleCommentSubmit} 
            />
          )}
          
          {loading ? (
            <div className="loading">댓글 로딩 중...</div>
          ) : (
            <CommentList comments={comments} />
          )}
        </div>
      )}
    </article>
  );
};

export default Post;