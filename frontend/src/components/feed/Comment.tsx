import React from 'react';
import { CommentDto } from '../../types/post';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';

interface CommentProps {
  comment: CommentDto;
}

const Comment: React.FC<CommentProps> = ({ comment }) => {
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
      const endpoint = comment.isLikedByMe ? '/comment/unlike' : '/comment/like';
      
      await axios.post(endpoint, {
        userId: user.id,
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

  return (
    <div className="comment">
      <div className="comment-header">
        <div className="comment-author">
          {comment.userProfileImage && (
            <img 
              src={comment.userProfileImage} 
              alt={comment.userName} 
              className="profile-image-small" 
            />
          )}
          <span className="author-name">{comment.userName}</span>
        </div>
        <div className="comment-date">
          {formatDate(comment.createdAt)}
        </div>
      </div>
      
      <div className="comment-content">
        {comment.content}
      </div>
      
      <div className="comment-actions">
        <button 
          className={`like-button-small ${comment.isLikedByMe ? 'liked' : ''}`}
          onClick={handleLike}
        >
          {comment.isLikedByMe ? '♥' : '♡'} {comment.likeCount || 0}
        </button>
      </div>
    </div>
  );
};

export default Comment;