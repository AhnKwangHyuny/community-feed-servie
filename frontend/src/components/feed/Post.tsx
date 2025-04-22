import React, { useState } from 'react';
import axios from 'axios';
import { GetPostContentResponseDto, CommentDto } from '../../types/post';
import { useAuth } from '../../context/AuthContext';
import CommentForm from './CommentForm';
import CommentList from './CommentList';

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
    return date.toLocaleString();
  };

  const handleLike = async () => {
    if (!user) {
      alert('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    try {
      const endpoint = post.isLikedByMe ? '/post/unlike' : '/post/like';
      
      await axios.post(endpoint, {
        userId: user.id,
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
      // 댓글 목록 API 호출 (백엔드 API를 확인하고 실제 경로로 수정 필요)
      const response = await axios.get(`/post/${post.id}/comments`);
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

  return (
    <div className="post">
      <div className="post-header">
        <div className="post-author">
          {post.userProfileImage && (
            <img 
              src={post.userProfileImage} 
              alt={post.userName} 
              className="profile-image" 
            />
          )}
          <span className="author-name">{post.userName}</span>
        </div>
        <div className="post-date">
          {formatDate(post.createdAt)}
        </div>
      </div>
      
      <div className="post-content">
        {post.content}
      </div>
      
      <div className="post-actions">
        <button 
          className={`like-button ${post.isLikedByMe ? 'liked' : ''}`}
          onClick={handleLike}
        >
          {post.isLikedByMe ? '♥' : '♡'} {post.likeCount || 0}
        </button>
        
        <button 
          className="comment-button"
          onClick={handleCommentToggle}
        >
          💬 {post.commentCount || 0}
        </button>
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
    </div>
  );
};

export default Post;