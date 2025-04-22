import React, { useState, useEffect } from 'react';
import Post from './Post';
import { GetPostContentResponseDto } from '../../types/post';

interface PostListProps {
  posts: GetPostContentResponseDto[];
  onLike: (postId: number, liked: boolean) => void;
}

const PostList: React.FC<PostListProps> = ({ posts, onLike }) => {
  const [visiblePosts, setVisiblePosts] = useState<GetPostContentResponseDto[]>([]);

  // 게시물이 추가될 때마다 애니메이션을 위해 순차적으로 표시
  useEffect(() => {
    // 이미 표시된 게시물 ID 추적
    const existingPostIds = new Set(visiblePosts.map(post => post.id));
    
    // 새 게시물만 필터링
    const newPosts = posts.filter(post => !existingPostIds.has(post.id));
    
    if (newPosts.length === 0) {
      return;
    }
    
    // 초기 로딩이면 모든 게시물을 한 번에 표시
    if (visiblePosts.length === 0) {
      setVisiblePosts(posts);
      return;
    }
    
    // 새 게시물을 순차적으로 표시
    let delay = 100;
    newPosts.forEach(post => {
      setTimeout(() => {
        setVisiblePosts(prev => [
          ...prev.filter(p => p.id !== post.id), // 중복 제거
          post
        ].sort((a, b) => {
          // 정렬 기준: 최신순 (ID 기준)
          return b.id - a.id;
        }));
      }, delay);
      delay += 150; // 각 게시물 사이의 표시 간격
    });
  }, [posts]);

  if (visiblePosts.length === 0) {
    return null;
  }

  return (
    <div className="post-list">
      {visiblePosts.map((post) => (
        <div key={post.id} className="post-item-container">
          <Post 
            post={post} 
            onLike={onLike}
          />
        </div>
      ))}
    </div>
  );
};

export default PostList;