import React, { useEffect, useState, useCallback } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import PostList from '../components/feed/PostList';
import PostForm from '../components/feed/PostForm';
import Header from '../components/layout/Header';
import { GetPostContentResponseDto } from '../types/post';
import { useNotification } from '../context/NotificationContext';
import '../styles/Home.css';

type CategoryType = '인기글' | '전체글' | '마이페이지' | '추천순';

// 기본 게시글 3개 (하드코딩)
const DEFAULT_POSTS: GetPostContentResponseDto[] = [
  {
    id: 9999,
    content: '안녕하세요! Faddy 커뮤니티에 오신 것을 환영합니다. 여러분의 일상과 이야기를 공유해보세요.',
    userId: 1,
    userName: '안광현',
    userProfileImage: 'https://via.placeholder.com/100',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    likeCount: 42,
    isLikedByMe: false,
    commentCount: 5,
    images: [
      'https://images.unsplash.com/photo-1682686580391-615ee3e7b6d9',
      'https://images.unsplash.com/photo-1682686580186-b55d2a91053c',
      'https://images.unsplash.com/photo-1682687982167-d7fb3ed8541d'
    ]
  },
  {
    id: 9998,
    content: '오늘 진행한 새로운 프로젝트입니다. 리액트와 스프링부트를 활용한 SNS 서비스를 개발 중이에요. 많은 관심 부탁드립니다!',
    userId: 1,
    userName: '안광현',
    userProfileImage: 'https://via.placeholder.com/100',
    createdAt: new Date(Date.now() - 86400000).toISOString(), // 1일 전
    updatedAt: new Date(Date.now() - 86400000).toISOString(),
    likeCount: 24,
    isLikedByMe: false,
    commentCount: 3,
    images: [
      'https://images.unsplash.com/photo-1587620962725-abab7fe55159',
      'https://images.unsplash.com/photo-1517694712202-14dd9538aa97',
      'https://images.unsplash.com/photo-1555066931-4365d14bab8c'
    ]
  },
  {
    id: 9997,
    content: '커뮤니티 피드 서비스가 새롭게 오픈했습니다! 다양한 기능을 체험해보세요. 이미지를 업로드하고, 좋아요와 댓글로 소통해보세요.',
    userId: 1,
    userName: '안광현',
    userProfileImage: 'https://via.placeholder.com/100',
    createdAt: new Date(Date.now() - 172800000).toISOString(), // 2일 전
    updatedAt: new Date(Date.now() - 172800000).toISOString(),
    likeCount: 56,
    isLikedByMe: true,
    commentCount: 7,
    images: [
      'https://images.unsplash.com/photo-1522542550221-31fd19575a2d',
      'https://images.unsplash.com/photo-1522199755839-a2bacb67c546',
      'https://images.unsplash.com/photo-1520333789090-1afc82db536a'
    ]
  }
];

const Home: React.FC = () => {
  const [posts, setPosts] = useState<GetPostContentResponseDto[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [lastContentId, setLastContentId] = useState<number | null>(null);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [showPostForm, setShowPostForm] = useState<boolean>(false);
  const [activeCategory, setActiveCategory] = useState<CategoryType>('인기글');
  const { user } = useAuth();
  const { addNotification } = useNotification();

  // 피드 게시물 불러오기
  const fetchPosts = useCallback(async () => {
    if (!hasMore || loading) return;

    try {
      setLoading(true);
      const response = await api.get<{ data: GetPostContentResponseDto[] }>('/feed', {
        params: { 
          lastContentId,
          category: activeCategory === '인기글' ? 'popular' : 
                    activeCategory === '추천순' ? 'recommended' : 
                    activeCategory === '마이페이지' ? 'my' : 'all'
        }
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
      console.error('Failed to fetch posts:', err);
      
      // API 호출이 실패하더라도 기본 게시글을 보여주기
      setPosts(DEFAULT_POSTS);
      setHasMore(false);
      setLoading(false);
      
      // 개발 환경에서만 에러 메시지 표시
      if (process.env.NODE_ENV === 'development') {
        setError('포스트를 불러오는데 실패했습니다.');
        addNotification({
          message: '게시물을 불러오는 중 오류가 발생했습니다. 기본 게시글을 표시합니다.',
          type: 'warning',
          duration: 5000
        });
      }
    }
  }, [hasMore, lastContentId, loading, addNotification, activeCategory]);

  // 새 게시물 추가
  const handleNewPost = (newPost: GetPostContentResponseDto) => {
    setPosts(prevPosts => [newPost, ...prevPosts]);
    setShowPostForm(false);
    
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

  // 카테고리 변경
  const handleCategoryChange = (category: CategoryType) => {
    setActiveCategory(category);
    setPosts([]);
    setLastContentId(null);
    setHasMore(true);
    fetchPosts();
  };

  // 초기 로딩
  useEffect(() => {
    const timeout = setTimeout(() => {
      // 5초 동안 로딩 중이면 기본 게시글 표시
      if (loading && posts.length === 0) {
        setPosts(DEFAULT_POSTS);
        setLoading(false);
        setHasMore(false);
        console.log('로딩 타임아웃으로 기본 게시글 표시');
      }
    }, 5000);
    
    fetchPosts();
    
    return () => clearTimeout(timeout);
  }, [fetchPosts]);

  return (
    <div className="home-container">
      <Header />
      
      <div className="category-tabs">
        <button 
          className={`category-tab ${activeCategory === '인기글' ? 'active' : ''}`}
          onClick={() => handleCategoryChange('인기글')}
        >
          인기글
        </button>
        <button 
          className={`category-tab ${activeCategory === '전체글' ? 'active' : ''}`}
          onClick={() => handleCategoryChange('전체글')}
        >
          전체글
        </button>
        <button 
          className={`category-tab ${activeCategory === '마이페이지' ? 'active' : ''}`}
          onClick={() => handleCategoryChange('마이페이지')}
        >
          마이페이지
        </button>
        <button 
          className={`category-tab ${activeCategory === '추천순' ? 'active' : ''}`}
          onClick={() => handleCategoryChange('추천순')}
        >
          추천순
        </button>
      </div>
      
      <main className="main-content">
        <div className="feed-container">
          {error && <div className="error-message">{error}</div>}
          
          {user && (
            <div className="post-actions">
              <button 
                className="create-post-btn"
                onClick={() => setShowPostForm(!showPostForm)}
              >
                {showPostForm ? '취소' : '새 포스트 작성'}
              </button>
              <button className="refresh-button" onClick={refreshFeed}>
                새로고침
              </button>
            </div>
          )}
          
          {user && showPostForm && (
            <div className="post-form-container">
              <PostForm onPostCreated={handleNewPost} />
            </div>
          )}
          
          <div className="feed-section">
            {posts.length > 0 && (
              <PostList 
                posts={posts} 
                onLike={handlePostLike} 
              />
            )}
            
            {loading && posts.length === 0 && (
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
      </main>
    </div>
  );
};

export default Home;