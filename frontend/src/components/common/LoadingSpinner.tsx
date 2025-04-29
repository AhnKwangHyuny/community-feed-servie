import React from 'react';
import '../../styles/LoadingSpinner.css';

const LoadingSpinner: React.FC = () => {
  return (
    <div className="loading-spinner-container">
      <div className="loading-spinner"></div>
      <p>로딩 중...</p>
    </div>
  );
};

export default LoadingSpinner;