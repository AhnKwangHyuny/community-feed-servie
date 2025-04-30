import React, { useState, useEffect, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import PostList from '../components/feed/PostList';
import FeedFilter from '../components/feed/FeedFilter';
import Layout from '../components/layout/Layout';
import api from '../services/api';
import { GetPostContentResponseDto } from '../types/post';
import '../styles/Home.css';
import LoadingSpinner from '../components/common/LoadingSpinner';

const Home: React.FC = () => {
  const [posts, setPosts] = useState<GetPostContentResponseDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [hasMore, setHasMore] = useState(true);
  const [sortType, setSortType] = useState<string>('latest'); // 기본값 'latest'로 설정됨
  const [lastContentId, setLastContentId] = useState<number | null>(null);
  const observer = useRef<IntersectionObserver | null>(null);
  const navigate = useNavigate();

  // 정렬 타입에 따른 API 엔드포인트 매핑
  const apiEndpoints: Record<string, string> = {
    latest: '/api/feeds', // 최신순 (기본값)
    oldest: '/api/feeds', // 오래된순 (sort=oldest 파라미터 필요)
    popular: '/api/feeds/popular', // 인기순
    recommended: '/api/feeds/recommended', // 추천순
    my: '/api/feeds/my' // 내 게시물
  };

  // 마지막 요소 참조 콜백 - 무한 스크롤 구현
  const lastPostElementRef = useCallback((node: HTMLDivElement) => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();
    
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        loadMorePosts();
      }
    }, { threshold: 0.5 });
    
    if (node) observer.current.observe(node);
  }, [loading, hasMore]);

  // 게시물 로드 함수
  const fetchPosts = async (sort: string, initialLoad = true) => {
    try {
      setLoading(true);
      setError(null);
      
      const endpoint = apiEndpoints[sort] || '/api/feeds';
      const queryParams = new URLSearchParams();
      
      // 일반 피드 API인 경우 정렬 파라미터 추가
      if (endpoint === '/api/feeds') {
        queryParams.append('sort', sort);
      }
      
      // 초기 로드가 아닌 경우(무한 스크롤) lastContentId 추가
      if (!initialLoad && lastContentId) {
        queryParams.append('lastContentId', lastContentId.toString());
      }
      
      // 내 게시물 조회는 로그인 필요
      if (sort === 'my' && !localStorage.getItem('accessToken')) {
        console.warn('내 게시물을 불러오려면 로그인이 필요합니다.');
        setError('내 게시물을 불러오려면 로그인이 필요합니다.');
        setLoading(false);
        return;
      }
      
      console.log(`Fetching posts: ${endpoint}?${queryParams.toString()}`);
      const response = await api.get(`${endpoint}?${queryParams.toString()}`);
      
      if (response.data && response.data.data) {
        // 초기 로드인 경우 posts 초기화, 아니면 기존 posts에 추가
        const newPosts = response.data.data;
        
        if (initialLoad) {
          setPosts(newPosts);
        } else {
          setPosts(prev => [...prev, ...newPosts]);
        }
        
        // 마지막 게시물 ID 저장 (커서 기반 페이징)
        if (newPosts.length > 0) {
          setLastContentId(newPosts[newPosts.length - 1].id);
        } else {
          // 결과가 없는 경우
          setHasMore(false);
        }
        
        // 더 불러올 게시물이 없는 경우 (10개 미만이면 더 없다고 가정)
        if (newPosts.length < 10) {
          setHasMore(false);
        } else {
          setHasMore(true);
        }
      } else {
        // 응답은 성공했지만 데이터가 없는 경우
        if (initialLoad) {
          setPosts([]);
        }
        setHasMore(false);
      }
    } catch (err: any) {
      console.error('게시글을 불러오는 중 오류가 발생했습니다:', err);
      
      // 오류 메시지 상세화
      const errorMessage = err.response?.data?.message || '게시글을 불러오는 중 오류가 발생했습니다.';
      setError(errorMessage);
      
      // 토큰 관련 오류인 경우 (401, 403)
      if (err.response?.status === 401 || err.response?.status === 403) {
        if (sort === 'my') {
          setError('내 게시물을 불러오려면 로그인이 필요합니다.');
        }
      }
    } finally {
      setLoading(false);
    }
  };

  // 더 많은 게시물 로드 (무한 스크롤)
  const loadMorePosts = () => {
    if (!loading && hasMore) {
      fetchPosts(sortType, false);
    }
  };

  // 정렬 타입 변경 핸들러
  const handleSortChange = (sort: string) => {
    setSortType(sort);
    setLastContentId(null);
    setHasMore(true);
    window.scrollTo(0, 0);
  };

  // 새 게시물 작성 페이지로 이동
  const handleNewPostClick = () => {
    navigate('/post/new');
  };

  // 좋아요 상태 업데이트
  const handlePostLike = (postId: number, liked: boolean) => {
    setPosts(prevPosts => 
      prevPosts.map(post => 
        post.id === postId 
          ? { 
              ...post, 
              isLikedByMe: liked, 
              likeCount: liked ? post.likeCount + 1 : post.likeCount - 1 
            } 
          : post
      )
    );
  };

  // 컴포넌트 마운트 시와 정렬 타입이 변경될 때마다 게시물 다시 로드
  useEffect(() => {
    // 인증 토큰 유무에 관계없이 fetchPosts 호출
    console.log(`정렬 기준 ${sortType}으로 피드 불러오기`);
    fetchPosts(sortType);
  }, [sortType]);

  return (
    <Layout>
      <div className="home-container">
        <div className="tab-navigation">
          <div className="tab-button-container">
            <button 
              className={`tab-button ${sortType === 'latest' ? 'active' : ''}`}
              onClick={() => handleSortChange('latest')}
            >
              <span className="tab-icon">🕒</span>
              <span>최신순</span>
            </button>
            <button 
              className={`tab-button ${sortType === 'oldest' ? 'active' : ''}`}
              onClick={() => handleSortChange('oldest')}
            >
              <span className="tab-icon">📅</span>
              <span>오래된순</span>
            </button>
            <button 
              className={`tab-button ${sortType === 'popular' ? 'active' : ''}`}
              onClick={() => handleSortChange('popular')}
            >
              <span className="tab-icon">🔥</span>
              <span>인기순</span>
            </button>
            <button 
              className={`tab-button ${sortType === 'recommended' ? 'active' : ''}`}
              onClick={() => handleSortChange('recommended')}
            >
              <span className="tab-icon">✨</span>
              <span>추천순</span>
            </button>
          </div>
        </div>
        
        <button className="new-post-button" onClick={handleNewPostClick}>
          <span className="plus-icon">+</span>
          <span>새 포스트 작성</span>
        </button>
        
        {posts.length > 0 ? (
          <PostList 
            posts={posts} 
            onLike={handlePostLike} 
            lastPostRef={lastPostElementRef}
          />
        ) : !loading && !error ? (
          <div className="empty-feed">
            <p>게시물이 없습니다.</p>
            <button className="first-post-button" onClick={handleNewPostClick}>
              첫 게시물 작성하기
            </button>
          </div>
        ) : null}
        
        {loading && <LoadingSpinner />}
        {error && <div className="error-message">{error}</div>}
        
        {!hasMore && posts.length > 0 && (
          <div className="end-of-feed">
            <p>더 이상 게시물이 없습니다.</p>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default Home;