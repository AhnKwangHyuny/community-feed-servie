import React, { useState } from 'react';
import { CommentDto } from '../../types/post';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import '../../styles/Comment.css';

interface CommentProps {
  comment: CommentDto;
  isAuthor?: boolean;
}

const Comment: React.FC<CommentProps> = ({ comment, isAuthor = false }) => {
  const { user } = useAuth();
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
    });
  };

  const handleLike = async () => {
    if (!user) {
      alert('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    try {
      const endpoint = comment.isLikedByMe ? '/comment/unlike' : '/comment/like';
      
      await axios.post(endpoint, {
        userId: user?.id || 0,
        targetId: comment.id
      });
      
      // 여기서는 상태 업데이트를 하지 않고 부모 컴포넌트에서 관리하도록 할 수도 있습니다.
      // 이 예시에서는 간단히 페이지 새로고침으로 처리
      window.location.reload();
    } catch (error) {
      console.error('댓글 좋아요 처리 중 오류:', error);
      alert('좋아요 처리 중 오류가 발생했습니다.');
    }
  };

  const handleReplySubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user) {
      alert('댓글을 작성하려면 로그인이 필요합니다.');
      return;
    }
    
    if (!replyContent.trim()) {
      return;
    }
    
    try {
      await axios.post('/comment', {
        userId: user?.id || 0,
        postId: comment.postId,
        parentId: comment.id, // 부모 댓글 ID
        content: replyContent.trim()
      });
      
      setReplyContent('');
      setReplyMode(false);
      // 새로고침을 통해 댓글 목록 업데이트
      window.location.reload();
    } catch (error) {
      console.error('댓글 작성 중 오류:', error);
      alert('댓글 작성 중 오류가 발생했습니다.');
    }
  };

  return (
    <div className={`comment ${isAuthor ? 'author-comment' : ''}`}>
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
          </div>
          <div className="comment-date">{formatDate(comment.createdAt)}</div>
        </div>
      </div>
      
      <div className="comment-content">
        {comment.content}
      </div>
      
      <div className="comment-actions">
        <button 
          className={`comment-action-button ${comment.isLikedByMe ? 'liked' : ''}`}
          onClick={handleLike}
        >
          <span className="like-icon">{comment.isLikedByMe ? '♥' : '♡'}</span>
          <span className="like-count">{comment.likeCount || 0}</span>
        </button>
        
        {user && (
          <button 
            className="comment-action-button reply-button"
            onClick={() => setReplyMode(!replyMode)}
          >
            {replyMode ? '취소' : '답글'}
          </button>
        )}
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