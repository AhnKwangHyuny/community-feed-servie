import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';

interface CommentFormProps {
  postId: number;
  onCommentSubmit: (comment: any) => void;
}

const CommentForm: React.FC<CommentFormProps> = ({ postId, onCommentSubmit }) => {
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { user } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!content.trim()) {
      return;
    }
    
    if (!user) {
      alert('댓글을 작성하려면 로그인이 필요합니다.');
      return;
    }
    
    try {
      setIsSubmitting(true);
      
      const response = await axios.post('/comment', {
        userId: user.id,
        postId: postId,
        content: content.trim()
      });
      
      setContent('');
      onCommentSubmit(response.data.data);
    } catch (error) {
      console.error('댓글 작성 중 오류:', error);
      alert('댓글 작성 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <textarea
        className="comment-input"
        placeholder="댓글을 작성하세요..."
        value={content}
        onChange={(e) => setContent(e.target.value)}
        required
      />
      <button 
        type="submit" 
        className="comment-submit-button"
        disabled={isSubmitting || !content.trim()}
      >
        {isSubmitting ? '게시 중...' : '댓글 작성'}
      </button>
    </form>
  );
};

export default CommentForm;