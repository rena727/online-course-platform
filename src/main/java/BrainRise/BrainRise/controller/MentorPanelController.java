package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.dto.MentorProfileUpdateDto;
import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.MentorRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.CourseCommentService;
import BrainRise.BrainRise.service.CourseService;
import BrainRise.BrainRise.service.NotificationService;
import BrainRise.BrainRise.service.UserService;
import BrainRise.BrainRise.service.impls.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mentor")
@RequiredArgsConstructor
public class MentorPanelController {

    private final UserService userService;
    private final CourseService courseService;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final NotificationService notificationService;
    private final CourseCommentService courseCommentService;

    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";
        String identifier = principal.getName();

        UserDto mentor;
        List<CourseDto> mentorCourses;

        try {
            mentor = userService.getByUsername(identifier);
        } catch (Exception e) {
            mentor = new UserDto();
            mentor.setFullName("Məchul Mentor");
        }

        try {
            mentorCourses = courseService.getCoursesByMentor(identifier);
        } catch (Exception e) {
            mentorCourses = new ArrayList<>();
        }


        long totalStudentsCount = mentorCourses.stream()
                .flatMap(c -> courseService.getEnrolledStudents(c.getId()).stream())
                .map(UserDto::getId)
                .distinct()
                .count();

        model.addAttribute("user", mentor);
        model.addAttribute("courses", mentorCourses);
        model.addAttribute("totalStudents", totalStudentsCount);
        model.addAttribute("course", new CourseDto());

        return "admin/mentor-dashboard";
    }

    // KURSLARIN SİYAHISI
    @GetMapping("/courses")
    public String showMyCourses(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        String email = principal.getName();
        model.addAttribute("courses", courseService.getCoursesByMentor(email));
        model.addAttribute("course", new CourseDto());
        return "admin/mentor-courses";
    }

    // KURS YARATMAQ
    @PostMapping("/course/create")
    public String saveCourse(@ModelAttribute("course") CourseDto dto, Principal principal) {
        if (principal == null) return "redirect:/login";
        courseService.saveWithMentor(dto, principal.getName());
        return "redirect:/mentor/courses";
    }

    // KURS SİLMƏK
    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        courseService.deleteCourse(id);
        return "redirect:/mentor/courses";
    }

    @GetMapping("/course/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            CourseDto courseDto = courseService.getById(id);
            if (courseDto == null) {

                return "redirect:/mentor/courses";
            }
            model.addAttribute("course", courseDto);
            return "mentor/edit-course";
        } catch (Exception e) {

            e.printStackTrace();
            return "redirect:/mentor/courses";
        }
    }

    @PostMapping("/course/update/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") CourseDto dto, Principal principal) {
        if (principal == null) return "redirect:/login";
        courseService.update(id, dto);
        return "redirect:/mentor/courses";
    }

    @GetMapping("/profile/settings")
    public String showProfileSettings(Model model, Principal principal) {
        User user = userService.getByUsernameEntity(principal.getName());

        MentorProfileUpdateDto profileDto = new MentorProfileUpdateDto();
        profileDto.setFullName(user.getFullName());
        profileDto.setSpecialty(user.getSpecialty());
        profileDto.setBio(user.getBio());
        profileDto.setImgUrl(user.getImgUrl());
        profileDto.setLinkedinUrl(user.getLinkedinUrl());
        profileDto.setGithubUrl(user.getGithubUrl());

        model.addAttribute("profile", profileDto);

        return "mentor/profile-settings";
    }


    @PostMapping("/profile/update")
    @Transactional
    public String updateProfile(@ModelAttribute("profile") MentorProfileUpdateDto profileDto, Principal principal) {
        User existingUser = userService.getByUsernameEntity(principal.getName());

        // USER UPDATE
        existingUser.setFullName(profileDto.getFullName());
        existingUser.setSpecialty(profileDto.getSpecialty());
        existingUser.setBio(profileDto.getBio());
        existingUser.setImgUrl(profileDto.getImgUrl());
        existingUser.setLinkedinUrl(profileDto.getLinkedinUrl());
        existingUser.setGithubUrl(profileDto.getGithubUrl());

        userRepository.save(existingUser);

        // MENTOR UPDATE
        mentorRepository.findByUser(existingUser).ifPresent(mentor -> {
            mentor.setName(profileDto.getFullName());
            mentor.setSpecialty(profileDto.getSpecialty());
            mentor.setImgUrl(profileDto.getImgUrl());
            mentor.setLinkedinUrl(profileDto.getLinkedinUrl());
            mentor.setGitUrl(profileDto.getGithubUrl());
            mentorRepository.save(mentor);
        });

        return "redirect:/mentor/profile/settings?success";
    }


    // BİLDİRİŞLƏR SƏHİFƏSİ
    @GetMapping("/notifications")
    public String showNotifications(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        // Giriş edən istifadəçini tapırıq
        User user = userRepository.findByEmail(principal.getName())
                .orElseGet(() -> userRepository.findByUsername(principal.getName()).orElse(null));

        if (user != null) {

            model.addAttribute("notifications", notificationService.getNotificationsForUser(user.getId()));
        } else {
            model.addAttribute("notifications", new ArrayList<>());
        }

        model.addAttribute("activePage", "notifications");
        return "mentor/notifications";
    }

    // BİLDİRİŞ SİLMƏK
    @PostMapping("/notifications/delete/{id}")
    public String deleteNotification(@PathVariable Long id, Principal principal) {
        notificationService.deleteNotification(id);
        return "redirect:/mentor/notifications";
    }

    // HAMISINI OXUNDU ET
    @PostMapping("/notifications/read-all")
    public String markAllNotificationsAsRead(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseGet(() -> userRepository.findByUsername(principal.getName()).orElse(null));

        if (user != null) {
            notificationService.markAllAsRead(user.getId());
        }

        return "redirect:/mentor/notifications";
    }
    @GetMapping("/students")
    public String showAllStudents(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";


        List<CourseDto> mentorCourses = courseService.getCoursesByMentor(principal.getName());


        Map<Long, UserDto> studentMap = new HashMap<>();

        for (CourseDto course : mentorCourses) {
            List<UserDto> students = courseService.getEnrolledStudents(course.getId());

            for (UserDto student : students) {
                if (studentMap.containsKey(student.getId())) {

                    String existing = studentMap.get(student.getId()).getEnrolledCourses();
                    studentMap.get(student.getId()).setEnrolledCourses(existing + ", " + course.getName());
                } else {

                    student.setEnrolledCourses(course.getName());
                    studentMap.put(student.getId(), student);
                }
            }
        }

        model.addAttribute("students", new ArrayList<>(studentMap.values()));
        return "mentor/course-students";
    }




    // 1. REYLER SEHIFESINI GOSTEREN METOD
    @GetMapping("/reviews")
    public String showMentorReviews(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";


        List<CourseDto> mentorCourses = courseService.getCoursesByMentor(principal.getName());


        List<Map<String, Object>> reviewData = new ArrayList<>();

        for (CourseDto course : mentorCourses) {
            List<CourseCommentDto> courseComments = courseCommentService.getCommentsByCourseId(course.getId());

            for (CourseCommentDto comment : courseComments) {
                Map<String, Object> data = new HashMap<>();
                data.put("comment", comment);
                data.put("courseName", course.getName());
                data.put("courseId", course.getId());
                reviewData.add(data);
            }
        }

        reviewData.sort((a, b) -> {
            CourseCommentDto c1 = (CourseCommentDto) a.get("comment");
            CourseCommentDto c2 = (CourseCommentDto) b.get("comment");
            return c2.getCreatedAt().compareTo(c1.getCreatedAt());
        });

        model.addAttribute("reviewsData", reviewData);
        return "mentor/reviews";
    }

    @PostMapping("/reviews/reply")
    public String mentorReplyToComment(@RequestParam Long courseId,
                                       @RequestParam Long commentId,
                                       @RequestParam String text,
                                       Principal principal) {
        if (principal == null) return "redirect:/login";

        CourseCommentDto dto = new CourseCommentDto();
        dto.setText(text);
        dto.setParentId(commentId);


        courseCommentService.saveComment(courseId, dto, principal.getName());


        return "redirect:/mentor/reviews?success=true";
    }
}