import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Header: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="app-header">
      <div className="header-container">
        <div className="logo">
          <Link to="/">Community Feed</Link>
        </div>
        
        <nav className="main-nav">
          <ul>
            <li>
              <Link to="/">홈</Link>
            </li>
            {/* 추가 메뉴 항목들 */}
          </ul>
        </nav>
        
        <div className="user-actions">
          {user ? (
            <>
              <Link to="/profile" className="profile-link">
                {user.profileImageUrl ? (
                  <img 
                    src={user.profileImageUrl} 
                    alt={user.name || ''} 
                    className="header-profile-image" 
                  />
                ) : (
                  <span className="username">{user.name || ''}</span>
                )}
              </Link>
              <button 
                className="logout-button"
                onClick={handleLogout}
              >
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="login-button">로그인</Link>
              <Link to="/signup" className="signup-button">회원가입</Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;