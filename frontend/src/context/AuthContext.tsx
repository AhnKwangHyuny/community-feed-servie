import React, { createContext, useState, useContext, useEffect, ReactNode } from 'react';
import api from '../services/api';

interface User {
  id: number;
  name: string;
  email: string;
  profileImage?: string;
  profileImageUrl?: string; // 호환성을 위해 추가
}

interface AuthContextType {
  user: User | null;
  loading: boolean;
  error: string | null;
  isAuthenticated: boolean; // 추가
  login: (email: string, password: string) => Promise<void>;
  signup: (name: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  register: (name: string, email: string, password: string) => Promise<void>; // signup의 별칭
  sendVerificationEmail: (email: string) => Promise<void>; // 추가
  verifyEmail: (email: string, code: string) => Promise<void>; // 추가
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 초기 사용자 정보 로드
  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        if (!token) {
          setLoading(false);
          return;
        }

        const response = await api.get('/api/auth/me');
        
        if (response.data && response.data.data) {
          setUser(response.data.data);
        }
      } catch (err) {
        console.error('사용자 정보를 가져오는 중 오류 발생:', err);
        // 토큰이 유효하지 않으면 로그아웃 처리
        localStorage.removeItem('accessToken');
      } finally {
        setLoading(false);
      }
    };

    fetchCurrentUser();
  }, []);

  // 로그인 함수
  const login = async (email: string, password: string) => {
    try {
      setLoading(true);
      setError(null);

      const response = await api.post('/api/auth/login', {
        email,
        password
      });

      if (response.data && response.data.data && response.data.data.token) {
        localStorage.setItem('accessToken', response.data.data.token);
        
        // 사용자 정보 가져오기
        const userResponse = await api.get('/api/auth/me');
        if (userResponse.data && userResponse.data.data) {
          const userData = userResponse.data.data;
          // profileImage와 profileImageUrl 동기화
          if (userData.profileImage && !userData.profileImageUrl) {
            userData.profileImageUrl = userData.profileImage;
          } else if (userData.profileImageUrl && !userData.profileImage) {
            userData.profileImage = userData.profileImageUrl;
          }
          setUser(userData);
        }
      } else {
        throw new Error('로그인에 실패했습니다.');
      }
    } catch (err: any) {
      console.error('로그인 중 오류 발생:', err);
      setError(err.response?.data?.message || '로그인에 실패했습니다.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // 회원가입 함수
  const signup = async (name: string, email: string, password: string) => {
    try {
      setLoading(true);
      setError(null);

      const response = await api.post('/api/auth/signup', {
        name,
        email,
        password
      });

      if (response.data && response.data.data && response.data.data.token) {
        localStorage.setItem('accessToken', response.data.data.token);
        
        // 사용자 정보 가져오기
        const userResponse = await api.get('/api/auth/me');
        if (userResponse.data && userResponse.data.data) {
          const userData = userResponse.data.data;
          // profileImage와 profileImageUrl 동기화
          if (userData.profileImage && !userData.profileImageUrl) {
            userData.profileImageUrl = userData.profileImage;
          } else if (userData.profileImageUrl && !userData.profileImage) {
            userData.profileImage = userData.profileImageUrl;
          }
          setUser(userData);
        }
      } else {
        throw new Error('회원가입에 실패했습니다.');
      }
    } catch (err: any) {
      console.error('회원가입 중 오류 발생:', err);
      setError(err.response?.data?.message || '회원가입에 실패했습니다.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // 로그아웃 함수
  const logout = () => {
    localStorage.removeItem('accessToken');
    setUser(null);
  };

  // register는 signup의 별칭
  const register = signup;

  // 이메일 인증 메일 전송 함수
  const sendVerificationEmail = async (email: string) => {
    try {
      setLoading(true);
      setError(null);
      await api.post('/api/auth/send-verification', { email });
    } catch (err: any) {
      console.error('인증 메일 전송 중 오류 발생:', err);
      setError(err.response?.data?.message || '인증 메일 전송에 실패했습니다.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // 이메일 인증 코드 확인 함수
  const verifyEmail = async (email: string, code: string) => {
    try {
      setLoading(true);
      setError(null);
      await api.post('/api/auth/verify-email', { email, code });
    } catch (err: any) {
      console.error('이메일 인증 중 오류 발생:', err);
      setError(err.response?.data?.message || '이메일 인증에 실패했습니다.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // isAuthenticated 계산
  const isAuthenticated = user !== null;

  const value = {
    user,
    loading,
    error,
    isAuthenticated,
    login,
    signup,
    logout,
    register,
    sendVerificationEmail,
    verifyEmail
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
