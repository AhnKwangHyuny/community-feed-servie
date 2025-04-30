import axios from 'axios';

// API 응답 타입 정의
export interface ApiResponse<T> {
  code: number;
  message: string;
  data?: T;
  value?: any; // 백엔드에서 사용하는 대체 필드
}

// API 인스턴스 생성
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || '',
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터 - 토큰이 있으면 헤더에 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    
    // 피드 API는 토큰이 없어도 사용 가능하도록 처리
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else if (config.url && (
      config.url.startsWith('/api/feeds') || 
      config.url.includes('/api/feeds?') ||
      config.url.includes('/api/feeds/popular') ||
      config.url.includes('/api/feeds/recommended')
    )) {
      // 피드 관련 요청이고 토큰이 없는 경우 - Authorization 헤더를 설정하지 않음
      console.log('비로그인 상태로 피드 요청:', config.url);
    }
    
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터 - 에러 처리 및 응답 데이터 포맷
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    // 피드 요청 관련 URL 체크
    const isFeedRequest = originalRequest?.url && (
      originalRequest.url.startsWith('/api/feeds') ||
      originalRequest.url.includes('/api/feeds?') ||
      originalRequest.url.includes('/api/feeds/popular') ||
      originalRequest.url.includes('/api/feeds/recommended')
    );
    
    // 내 피드 요청인지 확인
    const isMyFeedRequest = originalRequest?.url && 
      originalRequest.url.includes('/api/feeds/my');
    
    // 401 에러 (인증 실패)인 경우
    if (error.response?.status === 401) {
      // 내 피드 요청이 아닌 일반 피드 요청인 경우 - 비로그인 상태로 처리
      if (isFeedRequest && !isMyFeedRequest) {
        console.warn('비로그인 상태로 피드 요청');
        // 기존 에러 그대로 반환 - 컴포넌트에서 처리
        return Promise.reject(error);
      }
      
      // 토큰 관련 요청이거나 내 피드 요청인 경우 - 로그아웃 처리
      if (!originalRequest._retry) {
        originalRequest._retry = true;
        
        // 토큰 갱신 로직을 여기에 추가할 수 있음
        // 토큰 갱신에 실패하면 로그아웃 처리
        localStorage.removeItem('accessToken');
        
        // 현재 페이지가 명시적으로 로그인이 필요한 페이지인 경우만 로그인 페이지로 리다이렉트
        if (isMyFeedRequest || 
            window.location.pathname.includes('/profile') || 
            window.location.pathname.includes('/post/new')) {
          window.location.href = '/login';
        }
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;