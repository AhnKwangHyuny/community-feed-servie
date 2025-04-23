import React, { useState } from 'react';
import api from '../../services/api';
import { Link } from 'react-router-dom';
import { GetPostContentResponseDto, CommentDto } from '../../types/post';
import { useAuth } from '../../context/AuthContext';
import CommentForm from './CommentForm';
import CommentList from './CommentList';
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
      const endpoint = post.isLikedByMe ? '/post/unlike' : '/post/like';
      
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
      // 댓글 목록 API 호출
      const response = await api.get(`/post/${post.id}/comments`);
      setComments(response.data.data || []);
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

  // 여기서 post.thumbnailUrl은 백엔드에서 아직 구현되지 않은 필드입니다.
  // 백엔드가 업데이트되면 이 필드를 사용합니다.
  const thumbnailUrl = post.thumbnailUrl || 'https://via.placeholder.com/400x500';

  return (
    <article className="post-card">
      <div className="thumbnail-container">
        <Link to={`/post/${post.id}`} className="post-link">
          <img src={thumbnailUrl} alt="게시물 이미지" className="post-thumbnail" />
          <div className="post-hover-overlay">
            <div className="post-stats-overlay">
              <div className="stat-item-overlay">
                <span className="heart-icon">♥</span>
                <span className="stat-count">{post.likeCount || 0}</span>
              </div>
              <div className="stat-item-overlay">
                <span className="comment-icon">💬</span>
                <span className="stat-count">{post.commentCount || 0}</span>
              </div>
            </div>
          </div>
        </Link>
        
        <div className="post-meta">
          <div className="post-date">{formatDate(post.createdAt)}</div>
          <div className="post-author">by {post.userName}</div>
        </div>
      </div>
      
      <div className="post-info">
        <h3 className="post-title">
          <Link to={`/post/${post.id}`}>
            {post.content.length > 50 
              ? post.content.substring(0, 50) + '...' 
              : post.content}
          </Link>
        </h3>
        
        <div className="post-actions">
          <button 
            className={`action-button like-button ${post.isLikedByMe ? 'liked' : ''}`}
            onClick={handleLike}
          >
            {post.isLikedByMe ? '♥' : '♡'} {post.likeCount || 0}
          </button>
          
          <button 
            className="action-button comment-button"
            onClick={handleCommentToggle}
          >
            💬 {post.commentCount || 0}
          </button>
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