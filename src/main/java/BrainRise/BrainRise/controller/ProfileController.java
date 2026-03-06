package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.models.Notification;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.NotificationRepository;
import BrainRise.BrainRise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        if (user == null) {
            return "redirect:/logout"; // Sessiyanı təmizləmək üçün logout-a atırıq
        }

        // Rol Yoxlaması
        boolean isMentor = false;
        if (user.getRoles() != null) {
            isMentor = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_MENTOR"));
        }

        if (isMentor) {
            return "admin/mentor-dashboard";
        }

        try {
            List<Notification> notes = notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(user.getId());
            model.addAttribute("notifications", notes != null ? notes : new ArrayList<>());
            model.addAttribute("unreadCount", notes != null ? notes.size() : 0);
        } catch (Exception e) {
            model.addAttribute("notifications", new ArrayList<>());
            model.addAttribute("unreadCount", 0);
        }

        if (user.getCourses() == null) user.setCourses(new ArrayList<>());
        if (user.getExamResults() == null) user.setExamResults(new ArrayList<>());

        model.addAttribute("user", user);

        return "profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam(required = false) String bio,
                                Principal principal) {

        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        user.setFullName(fullName);
        user.setEmail(email);
        user.setBio(bio);

        userRepository.save(user);

        return "redirect:/profile?success=true";
    }
}
