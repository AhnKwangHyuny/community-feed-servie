import React, { useState, useRef } from 'react';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { GetPostContentResponseDto } from '../../types/post';
import postService from '../../services/postService';
import '../../styles/PostForm.css';

interface PostFormProps {
  onPostCreated: (post: GetPostContentResponseDto) => void;
}

const PostForm: React.FC<PostFormProps> = ({ onPostCreated }) => {
  const [content, setContent] = useState('');
  const [images, setImages] = useState<File[]>([]);
  const [imageIds, setImageIds] = useState<number[]>([]);
  const [imageUrls, setImageUrls] = useState<string[]>([]); // 서버에서 반환한 실제 이미지 URL 저장
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isUploading, setIsUploading] = useState(false);
  const [uploadError, setUploadError] = useState<string | null>(null);
  const { user } = useAuth();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContent(e.target.value);
  };

  const handleImageChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setUploadError(null); // 에러 상태 초기화
      const selectedFiles = Array.from(e.target.files);
      
      // 임시로 상태 업데이트 (UI 반응성)
      const tempImages = [...images, ...selectedFiles];
      setImages(tempImages);
      
      // 이미지 미리보기 생성
      const newPreviews = [...imagePreviews];
      for (const file of selectedFiles) {
        const reader = new FileReader();
        reader.onloadend = () => {
          newPreviews.push(reader.result as string);
          setImagePreviews([...newPreviews]);
        };
        reader.readAsDataURL(file);
      }
      
      // 이미지 업로드 처리
      try {
        setIsUploading(true);
        console.log(`업로드할 이미지 개수: ${selectedFiles.length}`);
        
        // 수정된 uploadMultipleImages 함수는 내부적으로 이미지 개수에 따라
        // 단일 또는 다중 이미지 API를 선택합니다
        const uploadedImages = await postService.uploadMultipleImages(selectedFiles, 'POST');
        
        if (uploadedImages && uploadedImages.length > 0) {
          const newImageIds = uploadedImages.map(img => img.id);
          const newImageUrls = uploadedImages.map(img => img.url);
          
          setImageIds([...imageIds, ...newImageIds]);
          setImageUrls([...imageUrls, ...newImageUrls]);
          setIsUploading(false);
          console.log(`이미지 업로드 성공: ${uploadedImages.length}개`);
        } else {
          throw new Error('이미지 업로드 결과가 비어있습니다.');
        }
      } catch (error) {
        console.error('이미지 업로드 중 오류:', error);
        
        // 에러 처리 - 마지막으로 추가한 이미지 제거
        const originalImageCount = images.length - selectedFiles.length;
        setImages(images.slice(0, originalImageCount));
        setImagePreviews(imagePreviews.slice(0, originalImageCount));
        
        setUploadError('이미지 업로드에 실패했습니다. 다시 시도해주세요.');
        setIsUploading(false);
      }
    }
  };

  const removeImage = async (index: number) => {
    // 이미지 ID가 있으면 서버에서도 삭제 요청
    const imageId = imageIds[index];
    if (imageId) {
      try {
        await postService.deleteImage(imageId);
        console.log(`이미지 ID ${imageId} 삭제 성공`);
      } catch (error) {
        console.error(`이미지 ID ${imageId} 삭제 실패:`, error);
        // 실패해도 UI에서는 삭제 처리 진행
      }
    }
    
    // 로컬 상태 업데이트
    const newImages = [...images];
    const newPreviews = [...imagePreviews];
    const newImageIds = [...imageIds];
    const newImageUrls = [...imageUrls];
    
    newImages.splice(index, 1);
    newPreviews.splice(index, 1);
    newImageIds.splice(index, 1);
    newImageUrls.splice(index, 1);
    
    setImages(newImages);
    setImagePreviews(newPreviews);
    setImageIds(newImageIds);
    setImageUrls(newImageUrls);
    
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };
  
  const clearImages = async () => {
    // 서버에서 이미지 삭제
    for (const imageId of imageIds) {
      try {
        await postService.deleteImage(imageId);
      } catch (error) {
        console.error(`이미지 ID ${imageId} 삭제 실패:`, error);
      }
    }
    
    // 로컬 상태 초기화
    setImages([]);
    setImagePreviews([]);
    setImageIds([]);
    setImageUrls([]);
    
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
    
    // 이미지 업로드 중인 경우 대기
    if (isUploading) {
      alert('이미지 업로드가 완료될 때까지 기다려주세요.');
      return;
    }
    
    // 업로드 에러가 있는 경우 확인
    if (uploadError) {
      const proceed = window.confirm('이미지 업로드에 문제가 있습니다. 계속 진행하시겠습니까?');
      if (!proceed) return;
    }
    
    try {
      setIsSubmitting(true);
      
      // 포스트 데이터 생성 (이미지 ID 포함)
      const postData = {
        content: content.trim(),
        state: 'PUBLIC',  // PostPublicationState.PUBLIC
        imageIds: imageIds.length > 0 ? imageIds : undefined
      };
      
      // 포스트 생성 API 호출
      const postId = await postService.createPost(postData);
      
      // 임시 포스트 객체 생성 (실제 서버 URL 사용)
      const newPost: GetPostContentResponseDto = {
        id: postId,
        content: content.trim(),
        userId: user?.id || 0,
        userName: user?.name || '',
        userProfileImage: user?.profileImageUrl || '',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        likeCount: 0,
        viewCount: 0, // 필수 필드 추가
        isLikedByMe: false,
        commentCount: 0,
        thumbnailUrl: imageUrls.length > 0 ? imageUrls[0] : undefined
      };
      
      onPostCreated(newPost);
      
      // 폼 초기화
      setContent('');
      setImages([]);
      setImagePreviews([]);
      setImageIds([]);
      setImageUrls([]);
      setUploadError(null);
      
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      
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
      
      <div className="form-group image-upload">
        <label htmlFor="image" className="image-label">
          <div className="upload-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M19 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H19C20.1 21 21 20.1 21 19V5C21 3.9 20.1 3 19 3ZM19 19H5V5H19V19ZM17 12L14 16H10L8 13L5 17H19L17 12Z" fill="currentColor"/>
            </svg>
          </div>
          <span>{isUploading ? '업로드 중...' : '이미지 추가'}</span>
        </label>
        <input
          type="file"
          id="image"
          ref={fileInputRef}
          onChange={handleImageChange}
          accept="image/*"
          className="file-input"
          multiple
          disabled={isUploading}
        />
      </div>
      
      {uploadError && (
        <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>
          {uploadError}
        </div>
      )}
      
      {imagePreviews.length > 0 && (
        <div className="images-preview-container">
          {imagePreviews.map((preview, index) => (
            <div key={index} className="image-preview-wrapper">
              <img 
                src={preview} 
                alt={`이미지 ${index + 1}`} 
                className="image-preview" 
              />
              <button 
                type="button" 
                className="remove-image-btn"
                onClick={() => removeImage(index)}
                disabled={isUploading}
              >
                ✕
              </button>
              {index === 0 && <span className="main-image-badge">대표 이미지</span>}
            </div>
          ))}
        </div>
      )}
      
      <div className="form-actions">
        <button 
          type="submit" 
          className="submit-button"
          disabled={isSubmitting || !content.trim() || isUploading}
        >
          {isSubmitting ? '게시 중...' : '게시하기'}
        </button>
      </div>
    </form>
  );
};

export default PostForm;