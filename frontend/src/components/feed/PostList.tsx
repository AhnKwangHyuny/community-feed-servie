import React from 'react';
import Post from './Post';
import { GetPostContentResponseDto } from '../../types/post';
import '../../styles/PostList.css';

interface PostListProps {
  posts: GetPostContentResponseDto[];
  onLike: (postId: number, liked: boolean) => void;
  lastPostRef?: (node: HTMLDivElement) => void;
}

const PostList: React.FC<PostListProps> = ({ posts, onLike, lastPostRef }) => {
  return (
    <div className="post-list">
      {posts.map((post, index) => {
        // 마지막 게시물에 ref 연결 (무한 스크롤)
        if (index === posts.length - 1) {
          return (
            <div key={post.id} ref={lastPostRef}>
              <Post post={post} onLike={onLike} />
            </div>
          );
        }
        return <Post key={post.id} post={post} onLike={onLike} />;
      })}
    </div>
  );
};

export default PostList;