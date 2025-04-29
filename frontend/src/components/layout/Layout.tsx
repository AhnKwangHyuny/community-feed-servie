import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import '../../styles/Layout.css';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { user, logout } = useAuth();
  const location = useLocation();

  const isActive = (path: string) => {
    if (path === '/' && location.pathname === '/') return true;
    if (path !== '/' && location.pathname.startsWith(path)) return true;
    return false;
  };

  return (
    <div className="layout">
      <header className="header">
        <div className="header-container">
          <Link to="/" className="logo">
            Faddy
          </Link>
          <div className="nav-right">
            {user ? (
              <div className="user-menu">
                <Link to="/profile" className="profile-link">
                  <img 
                    src={user.profileImage || "https://via.placeholder.com/32"}
                    alt="프로필" 
                    className="user-avatar"
                  />
                  <span className="user-name">{user.name}</span>
                </Link>
                <button className="logout-button" onClick={logout}>
                  로그아웃
                </button>
              </div>
            ) : (
              <Link to="/login" className="login-button">
                로그인
              </Link>
            )}
          </div>
        </div>
      </header>
      
      <main className="main-content">
        {children}
      </main>
      
      <nav className="bottom-navigation">
        <Link to="/" className={`nav-item ${isActive('/') ? 'active' : ''}`}>
          <span className="nav-icon">🏠</span>
          <span className="nav-text">홈</span>
        </Link>
        <Link to="/search" className={`nav-item ${isActive('/search') ? 'active' : ''}`}>
          <span className="nav-icon">🔍</span>
          <span className="nav-text">검색</span>
        </Link>
        <Link to="/post/new" className={`nav-item ${isActive('/post/new') ? 'active' : ''}`}>
          <span className="nav-icon">➕</span>
          <span className="nav-text">작성</span>
        </Link>
        <Link to="/notifications" className={`nav-item ${isActive('/notifications') ? 'active' : ''}`}>
          <span className="nav-icon">🔔</span>
          <span className="nav-text">알림</span>
        </Link>
        <Link to="/profile" className={`nav-item ${isActive('/profile') ? 'active' : ''}`}>
          <span className="nav-icon">👤</span>
          <span className="nav-text">프로필</span>
        </Link>
      </nav>
    </div>
  );
};

export default Layout;