import React, { useEffect } from 'react';
import { useNotification } from '../../context/NotificationContext';

const Notifications: React.FC = () => {
  const { notifications, removeNotification } = useNotification();

  // 알림 등장 애니메이션을 위한 클래스 추가
  useEffect(() => {
    const notificationElements = document.querySelectorAll('.notification-item');
    
    notificationElements.forEach(element => {
      // 약간의 딜레이 후 visible 클래스 추가
      setTimeout(() => {
        element.classList.add('visible');
      }, 100);
    });
  }, [notifications]);

  if (notifications.length === 0) {
    return null;
  }

  return (
    <div className="notifications-container">
      {notifications.map((notification) => (
        <div 
          key={notification.id} 
          className={`notification-item notification-${notification.type}`}
        >
          <div className="notification-content">
            <span className="notification-message">{notification.message}</span>
          </div>
          <button 
            className="notification-close" 
            onClick={() => removeNotification(notification.id)}
          >
            &times;
          </button>
        </div>
      ))}
    </div>
  );
};

export default Notifications;