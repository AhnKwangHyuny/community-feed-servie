import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import Header from '../components/layout/Header';
import { GetPostContentResponseDto } from '../types/post';

const Profile: React.FC = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  
  const [userPosts, setUserPosts] = useState<GetPostContentResponseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  
  useEffect(() => {
    // 로그인하지 않은 사용자는 로그인 페이지로 리다이렉트
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    
    // 사용자의 게시물 가져오기
    const fetchUserPosts = async () => {
      try {
        setLoading(true);
        // 백엔드 API 경로를 실제에 맞게 수정해야 함
        const response = await axios.get<{ data: GetPostContentResponseDto[] }>(`/user/${user?.id}/posts`);
        setUserPosts(response.data.data || []);
      } catch (err) {
        console.error('Error fetching user posts:', err);
        setError('게시물을 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };
    
    fetchUserPosts();
  }, [isAuthenticated, navigate, user?.id]);

  return (
    <div className="profile-page">
      <Header />
      
      <div className="profile-container">
        <div className="profile-header">
          {user?.profileImageUrl ? (
            <img 
              src={user.profileImageUrl} 
              alt={user.name || '사용자'} 
              className="profile-image-large" 
            />
          ) : (
            <div className="profile-image-placeholder">
              {user?.name?.charAt(0).toUpperCase() || '?'}
            </div>
          )}
          
          <div className="profile-info">
            <h1>{user?.name || '사용자'}</h1>
            <p className="profile-email">{user?.email}</p>
          </div>
        </div>
        
        <div className="profile-content">
          <h2>내 게시물</h2>
          
          {loading ? (
            <div className="loading">로딩 중...</div>
          ) : error ? (
            <div className="error-message">{error}</div>
          ) : userPosts.length > 0 ? (
            <div className="user-posts">
              {userPosts.map(post => (
                <div key={post.id} className="post">
                  <div className="post-header">
                    <div className="post-date">
                      {new Date(post.createdAt).toLocaleString()}
                    </div>
                  </div>
                  
                  <div className="post-content">
                    {post.content}
                  </div>
                  
                  <div className="post-stats">
                    <span className="like-count">좋아요 {post.likeCount || 0}</span>
                    <span className="comment-count">댓글 {post.commentCount || 0}</span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="no-posts-message">
              아직 작성한 게시물이 없습니다.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Profile;