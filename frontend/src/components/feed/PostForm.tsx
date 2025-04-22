import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../../context/AuthContext';
import { GetPostContentResponseDto } from '../../types/post';

interface PostFormProps {
  onPostCreated: (post: GetPostContentResponseDto) => void;
}

const PostForm: React.FC<PostFormProps> = ({ onPostCreated }) => {
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { user } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!content.trim()) {
      return;
    }
    
    if (!user) {
      alert('포스트를 작성하려면 로그인이 필요합니다.');
      return;
    }
    
    try {
      setIsSubmitting(true);
      
      const response = await axios.post('/post', {
        userId: user.id,
        content: content.trim()
      });
      
      // API가 생성된 전체 포스트 객체를 반환하지 않는 경우를 고려해 임시 객체를 생성합니다.
      // 실제 API 응답에 따라 이 부분을 조정해야 할 수 있습니다.
      const newPostId = response.data.data;
      
      // 임시 포스트 객체 생성
      const newPost: GetPostContentResponseDto = {
        id: newPostId || 0,
        content: content.trim(),
        userId: user?.id || 0,
        userName: user?.name || '',
        userProfileImage: user?.profileImageUrl || '',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        likeCount: 0,
        isLikedByMe: false,
        commentCount: 0
      };
      
      setContent('');
      onPostCreated(newPost);
    } catch (error) {
      console.error('포스트 작성 중 오류:', error);
      alert('포스트 작성 중 오류가 발생했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="post-form-container">
      <h3>새 포스트 작성</h3>
      <form className="post-form" onSubmit={handleSubmit}>
        <textarea
          className="post-input"
          placeholder="무슨 생각을 하고 계신가요?"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
        <button 
          type="submit" 
          className="post-submit-button"
          disabled={isSubmitting || !content.trim()}
        >
          {isSubmitting ? '게시 중...' : '게시하기'}
        </button>
      </form>
    </div>
  );
};

export default PostForm;