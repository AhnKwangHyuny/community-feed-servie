import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import Header from '../components/layout/Header';
import CommentForm from '../components/feed/CommentForm';
import CommentList from '../components/feed/CommentList';
import { GetPostContentResponseDto, CommentDto } from '../types/post';
import { useNotification } from '../context/NotificationContext';
import '../styles/PostDetail.css';

const PostDetail: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();
  const [post, setPost] = useState<GetPostContentResponseDto | null>(null);
  const [comments, setComments] = useState<CommentDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [commentLoading, setCommentLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();
  const { addNotification } = useNotification();
  const navigate = useNavigate();

  // 포스트 상세 정보 로드
  useEffect(() => {
    const fetchPostDetail = async () => {
      try {
        setLoading(true);
        const response = await axios.get<{ data: GetPostContentResponseDto }>(`/post/${postId}`);
        setPost(response.data.data);
        setLoading(false);
        
        // 포스트 로드 후 댓글도 로드
        fetchComments();
      } catch (err) {
        setError('포스트를 불러오는데 실패했습니다.');
        setLoading(false);
        console.error('Failed to fetch post details:', err);
      }
    };

    if (postId) {
      fetchPostDetail();
    }
  }, [postId]);

  // 댓글 로드
  const fetchComments = async () => {
    if (!postId) return;
    
    try {
      setCommentLoading(true);
      const response = await axios.get<{ data: CommentDto[] }>(`/post/${postId}/comments`);
      setComments(response.data.data || []);
      setCommentLoading(false);
    } catch (err) {
      console.error('댓글을 불러오는 중 오류:', err);
      setCommentLoading(false);
    }
  };

  // 댓글 추가 처리
  const handleCommentSubmit = (newComment: CommentDto) => {
    setComments(prevComments => [newComment, ...prevComments]);
    
    addNotification({
      message: '댓글이 등록되었습니다.',
      type: 'success',
      duration: 3000
    });
  };

  // 좋아요 처리
  const handleLike = async () => {
    if (!user || !post) {
      alert('좋아요를 누르려면 로그인이 필요합니다.');
      return;
    }

    try {
      const endpoint = post.isLikedByMe ? '/post/unlike' : '/post/like';
      
      await axios.post(endpoint, {
        userId: user?.id || 0,
        targetId: post.id
      });
      
      // 좋아요 상태 업데이트
      setPost(prevPost => {
        if (!prevPost) return null;
        
        return {
          ...prevPost,
          isLikedByMe: !prevPost.isLikedByMe,
          likeCount: prevPost.isLikedByMe 
            ? Math.max((prevPost.likeCount || 1) - 1, 0) 
            : (prevPost.likeCount || 0) + 1
        };
      });
    } catch (error) {
      console.error('좋아요 처리 중 오류:', error);
      alert('좋아요 처리 중 오류가 발생했습니다.');
    }
  };

  // 포스트 삭제 처리
  const handleDelete = async () => {
    if (!user || !post) return;
    
    if (user.id !== post.userId) {
      alert('자신의 게시물만 삭제할 수 있습니다.');
      return;
    }
    
    const confirmDelete = window.confirm('정말로 이 게시물을 삭제하시겠습니까?');
    if (!confirmDelete) return;
    
    try {
      await axios.delete(`/post/${post.id}`, {
        data: { userId: user.id }
      });
      
      addNotification({
        message: '게시물이 삭제되었습니다.',
        type: 'success',
        duration: 3000
      });
      
      // 메인 페이지로 리디렉션
      navigate('/');
    } catch (error) {
      console.error('게시물 삭제 중 오류:', error);
      alert('게시물 삭제 중 오류가 발생했습니다.');
    }
  };

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

  return (
    <div className="post-detail-container">
      <Header />
      
      <main className="post-detail-content">
        {loading ? (
          <div className="loading-container">
            <div className="spinner"></div>
            <p>게시물을 불러오는 중...</p>
          </div>
        ) : error ? (
          <div className="error-container">
            <div className="error-message">{error}</div>
            <Link to="/" className="back-link">메인으로 돌아가기</Link>
          </div>
        ) : post ? (
          <>
            <div className="post-detail-card">
              <div className="post-header">
                <div className="post-info">
                  <div className="author-info">
                    <div className="author-avatar">
                      {post.userProfileImage ? (
                        <img 
                          src={post.userProfileImage} 
                          alt={post.userName} 
                          className="avatar-img" 
                        />
                      ) : (
                        <div className="avatar-placeholder">{post.userName.charAt(0)}</div>
                      )}
                    </div>
                    <div className="author-details">
                      <div className="author-name">{post.userName}</div>
                      <div className="post-date">{formatDate(post.createdAt)}</div>
                    </div>
                  </div>
                  
                  {user && user.id === post.userId && (
                    <div className="post-actions">
                      <Link to={`/post/edit/${post.id}`} className="edit-link">수정</Link>
                      <button className="delete-button" onClick={handleDelete}>삭제</button>
                    </div>
                  )}
                </div>
              </div>
              
              <div className="post-thumbnail-container">
                {post.thumbnailUrl && (
                  <img 
                    src={post.thumbnailUrl} 
                    alt="게시물 이미지" 
                    className="post-detail-thumbnail" 
                  />
                )}
              </div>
              
              <div className="post-body">
                <div className="post-content">
                  {post.content}
                </div>
              </div>
              
              <div className="post-footer">
                <div className="post-stats">
                  <div className="stat-item">
                    <span className="stat-label">조회</span>
                    <span className="stat-value">{post.viewCount || 0}</span>
                  </div>
                  <div className="stat-item">
                    <span className="stat-label">댓글</span>
                    <span className="stat-value">{post.commentCount || 0}</span>
                  </div>
                  <div className="stat-item">
                    <span className="stat-label">좋아요</span>
                    <span className="stat-value">{post.likeCount || 0}</span>
                  </div>
                </div>
                
                <div className="post-interactions">
                  <button 
                    className={`interaction-button like-button ${post.isLikedByMe ? 'liked' : ''}`}
                    onClick={handleLike}
                  >
                    <span className="button-icon">{post.isLikedByMe ? '♥' : '♡'}</span>
                    <span className="button-text">좋아요</span>
                  </button>
                  
                  <button 
                    className="interaction-button share-button"
                    onClick={() => {
                      navigator.clipboard.writeText(window.location.href);
                      addNotification({
                        message: '링크가 클립보드에 복사되었습니다.',
                        type: 'info',
                        duration: 3000
                      });
                    }}
                  >
                    <span className="button-icon">🔗</span>
                    <span className="button-text">공유</span>
                  </button>
                </div>
              </div>
            </div>
            
            <div className="comments-section">
              {user && (
                <div className="comment-form-container">
                  <h3 className="section-title">댓글 작성</h3>
                  <CommentForm 
                    postId={post.id} 
                    onCommentSubmit={handleCommentSubmit} 
                  />
                </div>
              )}
              
              <div className="comment-list-container">
                {commentLoading ? (
                  <div className="loading-container">
                    <div className="spinner-small"></div>
                    <p>댓글을 불러오는 중...</p>
                  </div>
                ) : (
                  <CommentList 
                    comments={comments}
                    postAuthorId={post.userId} 
                  />
                )}
              </div>
            </div>
          </>
        ) : (
          <div className="not-found-container">
            <h2>게시물을 찾을 수 없습니다.</h2>
            <Link to="/" className="back-link">메인으로 돌아가기</Link>
          </div>
        )}
      </main>
    </div>
  );
};

export default PostDetail;