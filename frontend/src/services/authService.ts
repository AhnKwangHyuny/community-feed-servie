// /Users/ahnkwanghyun/Documents/dev/community-feed-service/frontend/src/services/authService.ts

import axios from 'axios';

// API 응답 타입 정의
interface ApiResponse<T> {
  code: number;
  message: string;
  data?: T;
  value?: any; // 백엔드에서 사용하는 대체 필드
}

// 각 API 요청/응답 타입 정의
interface SendEmailRequestDto {
  email: string;
}

interface VerifyEmailRequestDto {
  email: string;
  token: string;
}

interface VerifyEmailResponseDto {
  email: string;
  verified: boolean;
  message: string;
}

interface CreateUserAuthRequestDto {
  email: string;
  password: string;
  role: string;
  name: string;
  profileImageUrl?: string;
}

interface UserAccessTokenResponseDto {
  accessToken: string;
}

interface LoginRequestDto {
  email: string;
  password: string;
  fcmToken?: string;
}

// API 클라이언트 생성
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 - 토큰이 있으면 헤더에 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * 인증 관련 API 호출 함수들을 제공하는 서비스
 */
const authService = {
  /**
   * 이메일 인증 요청을 보내는 함수
   * @param email 인증할 이메일 주소
   * @returns 응답 성공 여부
   */
  async sendVerificationEmail(email: string): Promise<boolean> {
    try {
      console.log('API 요청 URL:', '/signup/send-verification-email');
      console.log('이메일 데이터:', { email });
      
      const response = await api.post<ApiResponse<null>>('/signup/send-verification-email', 
        { email } as SendEmailRequestDto
      );
      
      console.log('API 응답:', response.data);
      return response.data.code === 0;
    } catch (error) {
      console.error('이메일 인증 요청 에러:', error);
      throw error;
    }
  },

  /**
   * 이메일 인증 코드를 확인하는 함수
   * @param email 인증할 이메일 주소
   * @param token 인증 토큰
   * @returns 인증 결과 정보
   */
  async verifyEmail(email: string, token: string): Promise<VerifyEmailResponseDto> {
    try {
      console.log('이메일 인증 확인 요청:', { email, token });
      
      const response = await api.post<any>(
        '/signup/verify-email',
        { email, token } as VerifyEmailRequestDto
      );
      
      console.log('이메일 인증 확인 응답:', response.data);
      
      // ApiResponse<VerifyEmailResponseDto> 형식 처리
      if (response.data.data) {
        return response.data.data;
      } 
      // 직접 VerifyEmailResponseDto 형식으로 반환된 경우
      else if (response.data.verified !== undefined) {
        return response.data;
      }
      // success/fail 형식 처리 (백엔드 ApiResponse 클래스 사용 시)
      else if (response.data.success !== undefined) {
        return {
          email,
          verified: response.data.success,
          message: response.data.message || '이메일 인증 ' + 
                  (response.data.success ? '성공' : '실패')
        };
      }
      
      // 기본 실패 응답
      return {
        email,
        verified: false,
        message: response.data.message || '서버 응답 형식 오류'
      };
    } catch (error) {
      console.error('이메일 인증 코드 확인 에러:', error);
      throw error;
    }
  },

  /**
   * 회원가입을 처리하는 함수
   * @param userData 회원가입 정보
   * @returns 회원가입 결과 및 액세스 토큰
   */
  async register(userData: CreateUserAuthRequestDto): Promise<UserAccessTokenResponseDto> {
    try {
      console.log('회원가입 요청:', userData);
      const response = await api.post<ApiResponse<UserAccessTokenResponseDto>>(
        '/signup/register',
        userData
      );
      console.log('회원가입 응답:', response.data);

      // response.data는 {code: 0, message: "ok", data: {accessToken: "..."}} 형식
      // 또는 {code: 0, message: "ok", value: {accessToken: "..."}} 형식일 수 있음
      if (response.data.code === 0) {
        // data 또는 value 필드에서 accessToken을 추출
        if (response.data.data) {
          return response.data.data;
        } else if (response.data.value) {
          // value 필드를 사용하는 경우 (백엔드 응답 형식에 따라)
          return response.data.value as unknown as UserAccessTokenResponseDto;
        }
      }
      
      // 에러가 발생하지 않았지만 토큰을 받지 못한 경우
      throw new Error('토큰을 받지 못했습니다');
    } catch (error) {
      console.error('회원가입 에러:', error);
      throw error;
    }
  },

  /**
   * 로그인을 처리하는 함수
   * @param email 이메일
   * @param password 비밀번호
   * @param fcmToken FCM 토큰 (선택사항)
   * @returns 로그인 결과 및 액세스 토큰
   */
  async login(email: string, password: string, fcmToken?: string): Promise<UserAccessTokenResponseDto> {
    try {
      const response = await api.post<ApiResponse<UserAccessTokenResponseDto>>(
        '/login',
        { email, password, fcmToken } as LoginRequestDto
      );
      
      // data 또는 value 필드에서 응답 추출
      if (response.data.data) {
        return response.data.data;
      } else if (response.data.value) {
        return response.data.value as unknown as UserAccessTokenResponseDto;
      }
      
      // 에러가 발생하지 않았지만 토큰을 받지 못한 경우
      throw new Error('토큰을 받지 못했습니다');
    } catch (error) {
      console.error('로그인 에러:', error);
      throw error;
    }
  },

  /**
   * 로컬 스토리지에 토큰을 저장하는 함수
   * @param token 액세스 토큰
   */
  saveToken(token: string): void {
    localStorage.setItem('accessToken', token);
  },

  /**
   * 로컬 스토리지에서 토큰을 가져오는 함수
   * @returns 저장된 액세스 토큰
   */
  getToken(): string | null {
    return localStorage.getItem('accessToken');
  },

  /**
   * 로컬 스토리지에서 토큰을 삭제하는 함수 (로그아웃)
   */
  removeToken(): void {
    localStorage.removeItem('accessToken');
  },

  /**
   * 사용자가 로그인되어 있는지 확인하는 함수
   * @returns 로그인 상태 여부
   */
  isAuthenticated(): boolean {
    return !!this.getToken();
  },
};

export default authService;