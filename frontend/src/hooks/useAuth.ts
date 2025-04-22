import { useAuth as useAuthFromContext } from '../context/AuthContext';

// 이 파일은 context에 있는 useAuth를 재내보내는 역할을 합니다.
// 실제 구현은 AuthContext.tsx에 있습니다.
export const useAuth = useAuthFromContext;

export default useAuth;