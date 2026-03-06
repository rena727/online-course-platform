package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.models.Notification;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.NotificationRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createNotification(Long userId, String title, String message, String type) {
        User user = userRepository.findById(userId).orElseThrow();

        Notification note = new Notification();
        note.setRecipient(user);
        note.setTitle(title);
        note.setMessage(message);
        note.setType(type);
        note.setLink("/profile");

        notificationRepository.save(note);
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notes = notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(userId);
        notes.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notes);
    }
    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}