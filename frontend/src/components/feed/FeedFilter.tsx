import React from 'react';
import { useAuth } from '../../context/AuthContext';
import '../../styles/FeedFilter.css';

interface FeedFilterProps {
  currentSort: string;
  onSortChange: (sort: string) => void;
}

const FeedFilter: React.FC<FeedFilterProps> = ({ currentSort, onSortChange }) => {
  const { user } = useAuth();

  const sortOptions = [
    { id: 'latest', label: '최신순', icon: '🕒' },
    { id: 'popular', label: '인기순', icon: '🔥' },
    { id: 'recommended', label: '추천순', icon: '✨' },
  ];

  // 로그인한 사용자만 '내 게시물' 옵션 표시
  if (user) {
    sortOptions.push({ id: 'my', label: '내 게시물', icon: '👤' });
  }

  return (
    <div className="feed-filter">
      <div className="filter-tabs">
        {sortOptions.map((option) => (
          <button
            key={option.id}
            className={`filter-tab ${currentSort === option.id ? 'active' : ''}`}
            onClick={() => onSortChange(option.id)}
          >
            <span className="filter-icon">{option.icon}</span>
            <span className="filter-label">{option.label}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

export default FeedFilter;