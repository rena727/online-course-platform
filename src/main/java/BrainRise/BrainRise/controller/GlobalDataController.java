package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.FootService;
import BrainRise.BrainRise.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalDataController {

    private final FootService footService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) {

        model.addAttribute("foot", footService.getFoot());


        if (principal != null) {
            userRepository.findByEmail(principal.getName()).ifPresent(user -> {

                long unread = notificationService.getUnreadCount(user.getId());

                model.addAttribute("unreadCount", unread);
                model.addAttribute("hasNewNotifications", unread > 0);


                model.addAttribute("currentUser", user);
            });
        }
    }
}