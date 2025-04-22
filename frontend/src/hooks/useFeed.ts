import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { GetPostContentResponseDto } from '../types/post';

interface UseFeedOptions {
  initialLimit?: number;
}

export const useFeed = (options: UseFeedOptions = {}) => {
  const { initialLimit = 10 } = options;
  
  const [posts, setPosts] = useState<GetPostContentResponseDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [hasMore, setHasMore] = useState(true);
  const [lastContentId, setLastContentId] = useState<number | null>(null);

  const fetchPosts = useCallback(async () => {
    if (!hasMore || loading) return;
    
    try {
      setLoading(true);
      setError(null);
      
      const response = await axios.get<{ data: GetPostContentResponseDto[] }>('/feed', {
        params: {
          lastContentId,
          limit: initialLimit,
        },
      });
      
      const newPosts = response.data.data;
      
      if (newPosts.length === 0) {
        setHasMore(false);
      } else {
        setPosts(prevPosts => [...prevPosts, ...newPosts]);
        setLastContentId(newPosts[newPosts.length - 1].id);
      }
    } catch (err) {
      setError('포스트를 불러오는데 실패했습니다.');
      console.error('Failed to fetch posts:', err);
    } finally {
      setLoading(false);
    }
  }, [hasMore, initialLimit, lastContentId, loading]);

  const refreshFeed = useCallback(async () => {
    setLastContentId(null);
    setPosts([]);
    setHasMore(true);
    await fetchPosts();
  }, [fetchPosts]);
  
  useEffect(() => {
    fetchPosts();
  }, []);
  
  const handlePostLike = (postId: number, liked: boolean) => {
    setPosts(prevPosts => 
      prevPosts.map(post => 
        post.id === postId 
          ? { 
              ...post, 
              isLikedByMe: liked, 
              likeCount: liked ? (post.likeCount || 0) + 1 : (post.likeCount || 1) - 1 
            } 
          : post
      )
    );
  };
  
  return {
    posts,
    loading,
    error,
    hasMore,
    fetchMorePosts: fetchPosts,
    refreshFeed,
    handlePostLike,
  };
};

export default useFeed;