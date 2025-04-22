// /Users/ahnkwanghyun/Documents/dev/community-feed-service/frontend/src/context/AuthContext.tsx

import React, { createContext, useState, useEffect, useContext } from 'react';
import authService from '../services/authService';

// 사용자 정보 타입
interface User {
  id?: number;
  email: string;
  name?: string;
  profileImageUrl?: string;
}

// 인증 컨텍스트 타입
interface AuthContextType {
  user: User | null;
  loading: boolean;
  isAuthenticated: boolean;
  login: (email: string, password: string, fcmToken?: string) => Promise<void>;
  logout: () => void;
  register: (userData: {
    email: string;
    password: string;
    name: string;
    profileImageUrl?: string;
  }) => Promise<void>;
  sendVerificationEmail: (email: string) => Promise<boolean>;
  verifyEmail: (email: string, token: string) => Promise<{verified: boolean, message: string}>;
  error: string | null;
}

// 기본값으로 빈 컨텍스트 생성
const AuthContext = createContext<AuthContextType>({
  user: null,
  loading: false,
  isAuthenticated: false,
  login: async () => {},
  logout: () => {},
  register: async () => {},
  sendVerificationEmail: async () => false,
  verifyEmail: async () => ({verified: false, message: ''}),
  error: null,
});

// 인증 상태 제공자 컴포넌트
export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  // 초기 로딩 시 토큰 확인
  useEffect(() => {
    const token = authService.getToken();
    if (token) {
      // 여기서 토큰으로 사용자 정보를 가져오는 API를 호출할 수 있음
      // 임시로 토큰이 있으면 인증된 것으로 처리
      setUser({ email: 'authenticated@example.com' });
    }
    setLoading(false);
  }, []);

  // 로그인 함수
  const login = async (email: string, password: string, fcmToken?: string) => {
    setError(null);
    setLoading(true);
    
    try {
      const response = await authService.login(email, password, fcmToken);
      
      // 토큰 저장
      authService.saveToken(response.accessToken);
      
      // 사용자 정보 설정 (실제로는 API로 사용자 정보 가져올 수 있음)
      setUser({ email });
    } catch (err) {
      setError('로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  // 로그아웃 함수
  const logout = () => {
    authService.removeToken();
    setUser(null);
  };

  // 회원가입 함수
  const register = async (userData: {
    email: string;
    password: string;
    name: string;
    profileImageUrl?: string;
  }) => {
    setError(null);
    setLoading(true);
    
    try {
      console.log('회원가입 요청:', userData);
      const response = await authService.register({
        ...userData,
        role: 'USER', // 기본 역할
      });
      console.log('회원가입 성공 응답:', response);
      
      // 토큰 저장
      if (response && response.accessToken) {
        authService.saveToken(response.accessToken);
        
        // 사용자 정보 설정
        setUser({
          email: userData.email,
          name: userData.name,
          profileImageUrl: userData.profileImageUrl,
        });
        
        console.log('회원가입 성공: 사용자 정보 설정 및 토큰 저장 완료');
        return; // 성공적으로 완료
      } else {
        throw new Error('유효한 액세스 토큰을 받지 못했습니다');
      }
    } catch (err) {
      console.error('회원가입 실패:', err);
      setError('회원가입에 실패했습니다. 다시 시도해주세요.');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // 이메일 인증 요청 함수
  const sendVerificationEmail = async (email: string): Promise<boolean> => {
    setError(null);
    setLoading(true);
    
    try {
      return await authService.sendVerificationEmail(email);
    } catch (err) {
      setError('이메일 인증 요청에 실패했습니다.');
      console.error('Email verification error:', err);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // 이메일 인증 코드 확인 함수
  const verifyEmail = async (email: string, token: string): Promise<{verified: boolean, message: string}> => {
    setError(null);
    setLoading(true);
    
    try {
      const response = await authService.verifyEmail(email, token);
      console.log("인증 응답:", response);
      return {
        verified: response.verified,
        message: response.message
      };
    } catch (err) {
      console.error('Email verification error:', err);
      setError('이메일 인증에 실패했습니다.');
      return {
        verified: false,
        message: '이메일 인증 처리 중 오류가 발생했습니다.'
      };
    } finally {
      setLoading(false);
    }
  };

  // 인증 상태 값
  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        isAuthenticated,
        login,
        logout,
        register,
        sendVerificationEmail,
        verifyEmail,
        error,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// 커스텀 훅으로 인증 컨텍스트 사용하기 쉽게 제공
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default AuthContext;