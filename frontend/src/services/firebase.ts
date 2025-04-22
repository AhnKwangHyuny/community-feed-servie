// Firebase 관련 설정
// 실제 프로젝트에서는 Firebase SDK를 설치하고 import 해야 합니다.
// npm install firebase

// Firebase 설정
const firebaseConfig = {
  apiKey: "AIzaSyBY1BzeMHDKuAR2rYkNq1FMOsdTm-4GjE8", // 이 값은 예시이므로 수정하셔야 합니다
  authDomain: "community-service-91877.firebaseapp.com",
  projectId: "community-service-91877",
  storageBucket: "community-service-91877.appspot.com",
  messagingSenderId: "123456789",  // 이 값은 예시이므로 수정하셔야 합니다
  appId: "1:123456789:web:abcdef123456789"  // 이 값은 예시이므로 수정하셔야 합니다
};

// Firebase 초기화 함수
export const initializeFirebase = () => {
  try {
    // 여기서 실제로 Firebase를 초기화합니다
    // const app = initializeApp(firebaseConfig);
    // const messaging = getMessaging(app);
    console.log('Firebase 초기화 성공');
    return true;
  } catch (error) {
    console.error('Firebase 초기화 중 오류:', error);
    return false;
  }
};

// FCM 토큰 요청 함수
export const requestFCMToken = async () => {
  try {
    // 실제 구현에서는 다음과 같이 작성합니다:
    // const messaging = getMessaging();
    // const token = await getToken(messaging, { vapidKey: 'YOUR_VAPID_KEY' });
    
    // 임시 토큰 반환
    const mockToken = 'mock-fcm-token-' + Math.random().toString(36).substring(2, 9);
    console.log('FCM 토큰:', mockToken);
    return mockToken;
  } catch (error) {
    console.error('FCM 토큰 요청 중 오류:', error);
    return null;
  }
};

// 알림 권한 요청 함수
export const requestNotificationPermission = async () => {
  if (!("Notification" in window)) {
    console.log("이 브라우저는 알림을 지원하지 않습니다.");
    return false;
  }
  
  try {
    const permission = await Notification.requestPermission();
    return permission === 'granted';
  } catch (error) {
    console.error('알림 권한 요청 중 오류:', error);
    return false;
  }
};

// 알림 표시 함수
export const showNotification = (title: string, options: NotificationOptions = {}) => {
  if (!("Notification" in window)) {
    console.log("이 브라우저는 알림을 지원하지 않습니다.");
    return;
  }
  
  if (Notification.permission === 'granted') {
    new Notification(title, options);
  }
};

export default {
  initializeFirebase,
  requestFCMToken,
  requestNotificationPermission,
  showNotification
};