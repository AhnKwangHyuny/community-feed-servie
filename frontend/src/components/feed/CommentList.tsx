import React, { useState, useEffect } from 'react';
import { CommentDto } from '../../types/post';
import Comment from './Comment';

interface CommentListProps {
  comments: CommentDto[];
}

const CommentList: React.FC<CommentListProps> = ({ comments }) => {
  const [visibleComments, setVisibleComments] = useState<CommentDto[]>([]);
  
  // 댓글이 변경될 때마다 애니메이션을 위해 순차적으로 표시
  useEffect(() => {
    // 이미 표시된 댓글 ID 추적
    const existingCommentIds = new Set(visibleComments.map(comment => comment.id));
    
    // 새 댓글만 필터링
    const newComments = comments.filter(comment => !existingCommentIds.has(comment.id));
    
    if (newComments.length === 0) {
      // 댓글이 삭제된 경우를 처리
      if (comments.length < visibleComments.length) {
        setVisibleComments(comments);
      }
      return;
    }
    
    // 초기 로딩이면 모든 댓글을 한 번에 표시
    if (visibleComments.length === 0) {
      setVisibleComments(comments);
      return;
    }
    
    // 새 댓글을 순차적으로 표시
    let delay = 50;
    newComments.forEach(comment => {
      setTimeout(() => {
        setVisibleComments(prev => [
          ...prev.filter(c => c.id !== comment.id), // 중복 제거
          comment
        ].sort((a, b) => {
          // 정렬 기준: 최신순 (ID 기준)
          return b.id - a.id;
        }));
      }, delay);
      delay += 100; // 각 댓글 사이의 표시 간격
    });
  }, [comments]);

  if (comments.length === 0) {
    return <div className="no-comments">아직 댓글이 없습니다. 첫 댓글을 작성해보세요!</div>;
  }

  return (
    <div className="comment-list">
      {visibleComments.map((comment) => (
        <div key={comment.id} className="comment-item-container">
          <Comment comment={comment} />
        </div>
      ))}
    </div>
  );
};

export default CommentList;