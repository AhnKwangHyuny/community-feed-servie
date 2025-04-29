import React, { createContext, useState, useContext, useEffect, ReactNode } from 'react';
import api from '../services/api';
import { useAuth } from './AuthContext';

interface Notification {
  id: number;
  type: string;
  content: string;
  message: string;
  isRead: boolean;
  createdAt: string;
  targetId?: number;
  duration?: number; // 알림 표시 시간(ms)
}

// 간소화된 알림 타입 (addNotification 함수용)
interface SimpleNotification {
  message: string;
  type: string;
  duration?: number;
  content?: string;
  isRead?: boolean;
  createdAt?: string;
}

interface NotificationContextType {
  notifications: Notification[];
  unreadCount: number;
  loading: boolean;
  error: string | null;
  fetchNotifications: () => Promise<void>;
  markAsRead: (notificationId: number) => Promise<void>;
  markAllAsRead: () => Promise<void>;
  addNotification: (notification: SimpleNotification) => void; // 타입 수정
  removeNotification: (notificationId: number) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export const useNotification = () => {
  const context = useContext(NotificationContext);
  if (context === undefined) {
    throw new Error('useNotification must be used within a NotificationProvider');
  }
  return context;
};

interface NotificationProviderProps {
  children: ReactNode;
}

export const NotificationProvider: React.FC<NotificationProviderProps> = ({ children }) => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();

  // 알림 목록 가져오기
  const fetchNotifications = async () => {
    if (!user) return;
    
    try {
      setLoading(true);
      setError(null);

      const response = await api.get('/api/notifications');
      
      if (response.data && response.data.data) {
        setNotifications(response.data.data);
        
        // 읽지 않은 알림 개수 계산
        const unread = response.data.data.filter((notification: Notification) => !notification.isRead).length;
        setUnreadCount(unread);
      }
    } catch (err) {
      console.error('알림을 가져오는 중 오류 발생:', err);
      setError('알림을 가져오는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 알림 읽음 표시
  const markAsRead = async (notificationId: number) => {
    if (!user) return;
    
    try {
      await api.post(`/api/notifications/${notificationId}/read`);
      
      // 상태 업데이트
      setNotifications(prev => 
        prev.map(notification => 
          notification.id === notificationId 
            ? { ...notification, isRead: true } 
            : notification
        )
      );
      
      // 읽지 않은 알림 개수 업데이트
      setUnreadCount(prev => Math.max(0, prev - 1));
    } catch (err) {
      console.error('알림 읽음 표시 중 오류 발생:', err);
      throw new Error('알림 읽음 표시 중 오류가 발생했습니다.');
    }
  };

  // 모든 알림 읽음 표시
  const markAllAsRead = async () => {
    if (!user) return;
    
    try {
      await api.post('/api/notifications/read-all');
      
      // 상태 업데이트
      setNotifications(prev => 
        prev.map(notification => ({ ...notification, isRead: true }))
      );
      
      // 읽지 않은 알림 개수 업데이트
      setUnreadCount(0);
    } catch (err) {
      console.error('모든 알림 읽음 표시 중 오류 발생:', err);
      throw new Error('모든 알림 읽음 표시 중 오류가 발생했습니다.');
    }
  };

  // 로그인한 사용자가 있는 경우 알림 가져오기
  useEffect(() => {
    if (user) {
      fetchNotifications();
    } else {
      setNotifications([]);
      setUnreadCount(0);
    }
  }, [user]);

  // 알림 추가 함수
  const addNotification = (notification: SimpleNotification) => {
    const newNotification = {
      ...notification,
      id: Date.now(), // 임시 ID 생성
      content: notification.content || notification.message, // content가 없으면 message를 사용
      isRead: notification.isRead !== undefined ? notification.isRead : false,
      createdAt: notification.createdAt || new Date().toISOString()
    };
    
    setNotifications(prev => [newNotification, ...prev]);
    setUnreadCount(prev => prev + 1);
  };

  // 알림 제거 함수
  const removeNotification = (notificationId: number) => {
    setNotifications(prev => prev.filter(notification => notification.id !== notificationId));
    // 제거된 알림이 읽지 않은 알림이었는지 확인 후 unreadCount 조정
    const removedNotification = notifications.find(n => n.id === notificationId);
    if (removedNotification && !removedNotification.isRead) {
      setUnreadCount(prev => Math.max(0, prev - 1));
    }
  };

  const value = {
    notifications,
    unreadCount,
    loading,
    error,
    fetchNotifications,
    markAsRead,
    markAllAsRead,
    addNotification,
    removeNotification
  };

  return <NotificationContext.Provider value={value}>{children}</NotificationContext.Provider>;
};
