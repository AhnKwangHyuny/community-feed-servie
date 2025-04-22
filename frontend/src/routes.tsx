import React from 'react';
import { Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Profile from './pages/Profile';

interface Route {
  path: string;
  element: React.ReactNode;
  auth?: boolean;
}

// 인증이 필요한 라우트와 그렇지 않은 라우트 정의
export const routes: Route[] = [
  {
    path: '/',
    element: <Home />,
    auth: false,
  },
  {
    path: '/login',
    element: <Login />,
    auth: false,
  },
  {
    path: '/signup',
    element: <Signup />,
    auth: false,
  },
  {
    path: '/profile',
    element: <Profile />,
    auth: true,
  },
  {
    path: '*',
    element: <Navigate to="/" replace />,
    auth: false,
  },
];

export default routes;