import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import '../../styles/CommentForm.css';

interface CommentFormProps {
  postId: number;
  onCommentSubmit?: (newComment: any) => void;
  parentId?: number;
}

const CommentForm: React.FC<CommentFormProps> = ({ postId, onCommentSubmit, parentId }) => {
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { user } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user) {
      alert('댓글을 작성하려면 로그인이 필요합니다.');
      return;
    }
    
    if (!content.trim()) {
      return;
    }
    
    try {
      setIsSubmitting(true);
      
      const commentData = {
        userId: user?.id || 0,
        postId: postId,
        content: content.trim(),
        ...(parentId && { parentId })
      };
      
      const response = await axios.post('/comment', commentData);
      
      // 임시 댓글 객체 생성 (실제 API 응답에 따라 조정 필요)
      const newCommentId = response.data.data;
      const newComment = {
        id: newCommentId,
        content: content.trim(),
        postId,
        userId: user?.id || 0,
        userName: user?.name || '',
        userProfileImage: user?.profileImageUrl,
        createdAt: new Date().toISOString(),
        likeCount: 0,
        isLikedByMe: false,
        ...(parentId && { parentId })
      };
      
      setContent('');
      
      if (onCommentSubmit) {
        onCommentSubmit(newComment);
      }
    } catch (error) {
      console.error('댓글 작성 중 오류:', error);
      alert('댓글 작성 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <div className="form-input-area">
        {user?.profileImageUrl ? (
          <img 
            src={user.profileImageUrl} 
            alt="" 
            className="comment-user-avatar" 
          />
        ) : (
          <div className="comment-user-avatar-placeholder">
            {user?.name ? user.name.charAt(0) : '?'}
          </div>
        )}
        
        <textarea
          className="comment-input"
          placeholder="댓글을 입력하세요..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
          rows={2}
          required
        />
      </div>
      
      <div className="form-actions">
        <button 
          type="submit" 
          className="comment-submit-button"
          disabled={isSubmitting || !content.trim()}
        >
          {isSubmitting ? '등록 중...' : '등록'}
        </button>
      </div>
    </form>
  );
};

export default CommentForm;