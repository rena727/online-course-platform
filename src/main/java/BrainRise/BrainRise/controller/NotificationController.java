package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/profile";
    }

    @PostMapping("/read-all")
    public String markAllAsRead(Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName()).orElse(null));

        if (user != null) {
            notificationService.markAllAsRead(user.getId());
        }

        return "redirect:/profile";
    }
}