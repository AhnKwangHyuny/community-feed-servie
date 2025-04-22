import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import PostList from '../components/feed/PostList';
import PostForm from '../components/feed/PostForm';
import Header from '../components/layout/Header';
import { GetPostContentResponseDto } from '../types/post';
import { useNotification } from '../context/NotificationContext';

const Home: React.FC = () => {
  const [posts, setPosts] = useState<GetPostContentResponseDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [lastContentId, setLastContentId] = useState<number | null>(null);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const { user } = useAuth();
  const { addNotification } = useNotification();

  // 피드 게시물 불러오기
  const fetchPosts = useCallback(async () => {
    if (!hasMore || loading) return;

    try {
      setLoading(true);
      const response = await axios.get<{ data: GetPostContentResponseDto[] }>('/feed', {
        params: { lastContentId }
      });
      
      const newPosts = response.data.data;
      
      if (newPosts.length === 0) {
        setHasMore(false);
      } else {
        setPosts(prevPosts => [...prevPosts, ...newPosts]);
        setLastContentId(newPosts[newPosts.length - 1].id);
      }
      
      setLoading(false);
    } catch (err) {
      setError('포스트를 불러오는데 실패했습니다.');
      setLoading(false);
      console.error('Failed to fetch posts:', err);
      
      addNotification({
        message: '게시물을 불러오는 중 오류가 발생했습니다.',
        type: 'error',
        duration: 5000
      });
    }
  }, [hasMore, lastContentId, loading, addNotification]);

  // 새 게시물 추가
  const handleNewPost = (newPost: GetPostContentResponseDto) => {
    setPosts(prevPosts => [newPost, ...prevPosts]);
    
    addNotification({
      message: '새 게시물이 등록되었습니다.',
      type: 'success',
      duration: 3000
    });
  };

  // 좋아요 처리
  const handlePostLike = (postId: number, liked: boolean) => {
    setPosts(prevPosts => 
      prevPosts.map(post => 
        post.id === postId 
          ? { 
              ...post, 
              isLikedByMe: liked, 
              likeCount: liked ? (post.likeCount || 0) + 1 : Math.max((post.likeCount || 1) - 1, 0)
            } 
          : post
      )
    );
  };

  // 피드 새로고침
  const refreshFeed = () => {
    setPosts([]);
    setLastContentId(null);
    setHasMore(true);
    setError(null);
    fetchPosts();
    
    addNotification({
      message: '피드가 새로고침되었습니다.',
      type: 'info',
      duration: 2000
    });
  };

  // 초기 로딩
  useEffect(() => {
    fetchPosts();
  }, [fetchPosts]);

  return (
    <div className="home-container">
      <Header />
      
      <div className="content-container">
        <div className="feed-header">
          <h1>커뮤니티 피드</h1>
          <button className="refresh-button" onClick={refreshFeed}>
            <span>새로고침</span>
          </button>
        </div>
        
        {user && <PostForm onPostCreated={handleNewPost} />}
        
        {error && <div className="error-message">{error}</div>}
        
        <div className="feed-section">
          <h2>최신 포스트</h2>
          
          <PostList 
            posts={posts} 
            onLike={handlePostLike} 
          />
          
          {loading && (
            <div className="loading-indicator">
              <div className="spinner"></div>
              <p>게시물을 불러오는 중...</p>
            </div>
          )}
          
          {!loading && posts.length === 0 && (
            <div className="empty-state">
              <div className="empty-state-icon">📭</div>
              <h3>게시물이 없습니다</h3>
              <p>아직 게시물이 없습니다. 첫 번째 게시물을 작성해보세요!</p>
            </div>
          )}
          
          {hasMore && !loading && posts.length > 0 && (
            <button 
              className="load-more-button"
              onClick={fetchPosts}
            >
              더 불러오기
            </button>
          )}
          
          {!hasMore && posts.length > 0 && (
            <div className="end-of-feed">
              더 이상 게시물이 없습니다.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;