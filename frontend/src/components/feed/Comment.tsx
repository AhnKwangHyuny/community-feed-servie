import React, { useState } from 'react';
import { CommentDto } from '../../types/post';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import '../../styles/Comment.css';

interface CommentProps {
  comment: CommentDto;
  isAuthor?: boolean;
  onDelete?: (commentId: number) => void;
}

const Comment: React.FC<CommentProps> = ({ comment, isAuthor = false, onDelete }) => {
  const { user } = useAuth();
  const { addNotification } = useNotification();
  const [replyMode, setReplyMode] = useState(false);
  const [replyContent, setReplyContent] = useState('');
  
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR', { 
      year: 'numeric', 
      month: '2-digit', 
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }).replace(/\./g, '').replace(/\s\s+/g, ' ');
  };

  const handleLike = async () => {
    if (!user) {
      addNotification({
        message: '좋아요를 누르려면 로그인이 필요합니다',
        type: 'warning',
        duration: 3000
      });
      return;
    }

    try {
      const endpoint = comment.isLikedByMe ? '/comment/unlike' : '/comment/like';
      
      await axios.post(endpoint, {
        targetId: comment.id
      });
      
      // 좋아요 상태 업데이트는 부모 컴포넌트에서 처리 (페이지 새로고침 대신)
      window.location.reload();
    } catch (error) {
      console.error('댓글 좋아요 처리 중 오류:', error);
      addNotification({
        message: '좋아요 처리 중 오류가 발생했습니다',
        type: 'error',
        duration: 3000
      });
    }
  };

  const handleReplySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user) {
      addNotification({
        message: '댓글을 작성하려면 로그인이 필요합니다',
        type: 'warning',
        duration: 3000
      });
      return;
    }
    
    if (!replyContent.trim()) {
      return;
    }
    
    try {
      await axios.post('/comment', {
        postId: comment.postId,
        parentId: comment.id,
        content: replyContent.trim()
      });
      
      setReplyContent('');
      setReplyMode(false);
      
      addNotification({
        message: '답글이 등록되었습니다',
        type: 'success',
        duration: 3000
      });
      
      // 댓글 목록 업데이트를 위한 새로고침
      window.location.reload();
    } catch (error) {
      console.error('댓글 작성 중 오류:', error);
      addNotification({
        message: '댓글 작성 중 오류가 발생했습니다',
        type: 'error',
        duration: 3000
      });
    }
  };

  const handleDelete = async () => {
    if (!user || user.id !== comment.userId) {
      return;
    }

    const confirmed = window.confirm('댓글을 삭제하시겠습니까?');
    if (!confirmed) {
      return;
    }

    try {
      await axios.delete(`/comment/${comment.id}`);
      
      addNotification({
        message: '댓글이 삭제되었습니다',
        type: 'success',
        duration: 3000
      });
      
      // 부모 컴포넌트의 onDelete 콜백 호출
      if (onDelete) {
        onDelete(comment.id);
      }
    } catch (error) {
      console.error('댓글 삭제 중 오류:', error);
      addNotification({
        message: '댓글 삭제 중 오류가 발생했습니다',
        type: 'error',
        duration: 3000
      });
    }
  };

  return (
    <div className={`comment ${isAuthor ? 'author-comment' : ''} ${comment.isBestComment ? 'best-comment' : ''}`}>
      <div className="comment-user-info">
        <div className="comment-avatar">
          {comment.userProfileImage ? (
            <img 
              src={comment.userProfileImage} 
              alt="" 
              className="avatar-img" 
            />
          ) : (
            <div className="avatar-placeholder">{comment.userName.charAt(0)}</div>
          )}
        </div>
        <div className="comment-user-meta">
          <div className="comment-username">
            {comment.userName}
            {isAuthor && <span className="author-tag">작성자</span>}
            {comment.isBestComment && <span className="best-comment-tag">베스트 댓글</span>}
          </div>
          <div className="comment-date">{formatDate(comment.createdAt)}</div>
        </div>
      </div>
      
      <div className="comment-content">
        {comment.content}
      </div>
      
      <div className="comment-actions">
        <div className="comment-action-buttons">
          {user && (
            <button 
              className="comment-action-button reply-button"
              onClick={() => setReplyMode(!replyMode)}
            >
              {replyMode ? '취소' : '답글'}
            </button>
          )}
        </div>
        
        <div className="comment-stats">
          <button 
            className={`like-button ${comment.isLikedByMe ? 'liked' : ''}`}
            onClick={handleLike}
          >
            <span className="like-icon">{comment.isLikedByMe ? '♥' : '♡'}</span>
            <span className="like-count">{comment.likeCount || 0}</span>
          </button>
          
          {user && user.id === comment.userId && (
            <button 
              className="delete-button"
              onClick={handleDelete}
              aria-label="댓글 삭제"
            >
              ✕
            </button>
          )}
        </div>
      </div>
      
      {replyMode && (
        <div className="reply-form-container">
          <form className="reply-form" onSubmit={handleReplySubmit}>
            <textarea
              className="reply-input"
              placeholder="답글을 입력하세요..."
              value={replyContent}
              onChange={(e) => setReplyContent(e.target.value)}
              rows={2}
            />
            <button 
              type="submit" 
              className="reply-submit-button"
              disabled={!replyContent.trim()}
            >
              등록
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default Comment;