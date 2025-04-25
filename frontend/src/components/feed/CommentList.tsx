import React, { useState, useEffect } from 'react';
import { CommentDto } from '../../types/post';
import Comment from './Comment';
import '../../styles/CommentList.css';

interface CommentListProps {
  comments: CommentDto[];
  postAuthorId?: number;
}

interface GroupedComments {
  [parentId: string]: {
    parent: CommentDto;
    replies: CommentDto[];
  }
}

const CommentList: React.FC<CommentListProps> = ({ comments, postAuthorId }) => {
  const [visibleComments, setVisibleComments] = useState<CommentDto[]>([]);
  
  // 댓글 삭제 처리
  const handleDeleteComment = (commentId: number) => {
    setVisibleComments(prev => prev.filter(c => c.id !== commentId));
  };
  
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
          // 베스트 댓글이 먼저 표시되도록 함
          if (a.isBestComment && !b.isBestComment) return -1;
          if (!a.isBestComment && b.isBestComment) return 1;
          // 그 다음 최신순 (ID 기준)
          return b.id - a.id;
        }));
      }, delay);
      delay += 100; // 각 댓글 사이의 표시 간격
    });
  }, [comments]);

  // 댓글을 2단계 구조로 그룹화 (부모 댓글과 그에 대한 답글)
  const groupedComments = visibleComments.reduce((acc: GroupedComments, comment) => {
    const parentId = comment.parentId?.toString() || comment.id.toString();
    
    // 부모 댓글인 경우
    if (!comment.parentId) {
      if (!acc[parentId]) {
        acc[parentId] = {
          parent: comment,
          replies: []
        };
      }
    } 
    // 답글인 경우
    else {
      const parentCommentId = comment.parentId.toString();
      if (!acc[parentCommentId]) {
        // 부모 댓글이 아직 로드되지 않은 경우, 일단 임시 저장
        acc[parentCommentId] = {
          parent: {} as CommentDto, // 임시 부모 객체
          replies: [comment]
        };
      } else {
        acc[parentCommentId].replies.push(comment);
      }
    }
    
    return acc;
  }, {});

  // 그룹화된 댓글을 배열로 변환 (베스트 댓글 우선, 그 다음 최신순)
  const sortedCommentGroups = Object.values(groupedComments)
    .filter(group => group.parent.id) // 유효한 부모 댓글만 필터링
    .sort((a, b) => {
      // 베스트 댓글이 먼저 표시
      if (a.parent.isBestComment && !b.parent.isBestComment) return -1;
      if (!a.parent.isBestComment && b.parent.isBestComment) return 1;
      // 그 다음 최신순
      return b.parent.id - a.parent.id;
    });

  if (visibleComments.length === 0) {
    return <div className="no-comments">아직 댓글이 없습니다. 첫 댓글을 작성해보세요!</div>;
  }

  return (
    <div className="comment-list">
      <div className="comment-header">
        <div className="comment-count">댓글 {visibleComments.length}개</div>
      </div>
      
      {sortedCommentGroups.map(group => (
        <div key={group.parent.id} className="comment-group">
          <Comment 
            comment={group.parent} 
            isAuthor={group.parent.userId === postAuthorId}
            onDelete={handleDeleteComment}
          />
          
          {group.replies.length > 0 && (
            <div className="comment-replies">
              {group.replies.map(reply => (
                <Comment 
                  key={reply.id} 
                  comment={reply}
                  isAuthor={reply.userId === postAuthorId}
                  onDelete={handleDeleteComment}
                />
              ))}
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default CommentList;