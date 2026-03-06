package BrainRise.BrainRise.service;

import BrainRise.BrainRise.models.Notification;
import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String title, String message, String type);

    List<Notification> getNotificationsForUser(Long userId);

    long getUnreadCount(Long userId);

    void markAllAsRead(Long userId);

    void deleteNotification(Long notificationId);
}