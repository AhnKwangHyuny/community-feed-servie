import React, { useState, useRef } from 'react';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { GetPostContentResponseDto } from '../../types/post';
import '../../styles/PostForm.css';

interface PostFormProps {
  onPostCreated: (post: GetPostContentResponseDto) => void;
}

const PostForm: React.FC<PostFormProps> = ({ onPostCreated }) => {
  const [content, setContent] = useState('');
  const [thumbnail, setThumbnail] = useState<File | null>(null);
  const [thumbnailPreview, setThumbnailPreview] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { user } = useAuth();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContent(e.target.value);
  };

  const handleThumbnailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setThumbnail(file);
      
      // 이미지 미리보기 생성
      const reader = new FileReader();
      reader.onloadend = () => {
        setThumbnailPreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const clearThumbnail = () => {
    setThumbnail(null);
    setThumbnailPreview(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user) {
      alert('로그인이 필요합니다.');
      return;
    }
    
    if (!content.trim()) {
      alert('내용을 입력해주세요.');
      return;
    }
    
    try {
      setIsSubmitting(true);
      
      // 포스트 데이터 생성
      const postData = {
        content: content.trim(),
        state: 'PUBLIC'  // PostPublicationState.PUBLIC
      };
      
      // 이미지가 있는 경우 FormData로 변환
      if (thumbnail) {
        const formData = new FormData();
        formData.append('content', content.trim());
        formData.append('state', 'PUBLIC');
        formData.append('thumbnail', thumbnail);
        
        // 이미지 업로드 API 호출 (백엔드 구현 필요)
        // 백엔드 API 구현 전까지는 기존 API를 사용
        const response = await api.post<{data: number}>('/post', postData);
        
        // 임시 포스트 객체 생성
        const newPost: GetPostContentResponseDto = {
          id: response.data.data || 0,
          content: content.trim(),
          userId: user?.id || 0,
          userName: user?.name || '',
          userProfileImage: user?.profileImageUrl || '',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
          likeCount: 0,
          isLikedByMe: false,
          commentCount: 0,
          thumbnailUrl: thumbnailPreview || undefined
        };
        
        onPostCreated(newPost);
      } else {
        // 기존 텍스트만 있는 포스트 생성
        const response = await api.post<{data: number}>('/post', postData);
        
        // 임시 포스트 객체 생성
        const newPost: GetPostContentResponseDto = {
          id: response.data.data || 0,
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
        
        onPostCreated(newPost);
      }
      
      // 폼 초기화
      setContent('');
      clearThumbnail();
      setIsSubmitting(false);
      
    } catch (error) {
      console.error('포스트 생성 중 오류:', error);
      alert('포스트 생성 중 오류가 발생했습니다.');
      setIsSubmitting(false);
    }
  };

  return (
    <form className="post-form" onSubmit={handleSubmit}>
      <div className="form-group">
        <textarea
          className="post-textarea"
          placeholder="무슨 생각을 하고 계신가요?"
          value={content}
          onChange={handleContentChange}
          rows={4}
          required
        />
      </div>
      
      <div className="form-group thumbnail-upload">
        <label htmlFor="thumbnail" className="thumbnail-label">
          <div className="upload-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M19 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H19C20.1 21 21 20.1 21 19V5C21 3.9 20.1 3 19 3ZM19 19H5V5H19V19ZM17 12L14 16H10L8 13L5 17H19L17 12Z" fill="currentColor"/>
            </svg>
          </div>
          <span>이미지 추가</span>
        </label>
        <input
          type="file"
          id="thumbnail"
          ref={fileInputRef}
          onChange={handleThumbnailChange}
          accept="image/*"
          className="file-input"
        />
      </div>
      
      {thumbnailPreview && (
        <div className="thumbnail-preview-container">
          <img 
            src={thumbnailPreview} 
            alt="썸네일 미리보기" 
            className="thumbnail-preview" 
          />
          <button 
            type="button" 
            className="remove-thumbnail-btn"
            onClick={clearThumbnail}
          >
            ✕
          </button>
        </div>
      )}
      
      <div className="form-actions">
        <button 
          type="submit" 
          className="submit-button"
          disabled={isSubmitting || !content.trim()}
        >
          {isSubmitting ? '게시 중...' : '게시하기'}
        </button>
      </div>
    </form>
  );
};

export default PostForm;