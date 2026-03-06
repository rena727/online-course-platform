//
//
//package BrainRise.BrainRise.controller.AdminController;
//
//import BrainRise.BrainRise.dto.CourseDto;
//import BrainRise.BrainRise.service.CourseService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final CourseService courseService;
//
//    @GetMapping("/dashboard")
//    public String adminDashboard(Model model) {
//        // Dashboard həm gözləyənləri, həm də bütün kursları göstərir
//        model.addAttribute("pendingCourses", courseService.getPendingCourses());
//        model.addAttribute("allCourses", courseService.getAllkurs());
//        return "admin/dashboard";
//    }
//
//    @GetMapping("/moderation")
//    public String moderationPage(Model model) {
//        model.addAttribute("pendingCourses", courseService.getPendingCourses());
//        return "admin/moderation";
//    }
//
//    @PostMapping("/course/approve/{id}")
//    public String approveCourse(@PathVariable Long id) {
//        courseService.approveCourse(id);
//        // Təsdiqdən sonra Dashboard-dakı kurslar siyahısına yönləndirir
//        return "redirect:/admin/dashboard?tab=all-courses&approved";
//    }
//
//    @PostMapping("/course/reject/{id}")
//    public String rejectCourse(@PathVariable Long id, @RequestParam("reason") String reason) {
//        courseService.rejectCourse(id, reason);
//        // Bax bura əsasdır: səni yenidən moderasiya səhifəsinə qaytarır
//        return "redirect:/admin/moderation";
//    }
//
//    @PostMapping("/course/delete/{id}")
//    public String adminDelete(@PathVariable Long id) {
//        courseService.deleteCourse(id);
//        return "redirect:/admin/dashboard?tab=all-courses&deleted";
//    }
//}
package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.service.CourseService;
import BrainRise.BrainRise.service.MentorService;
import BrainRise.BrainRise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CourseService courseService;
    private final MentorService mentorService;
    private final UserService userService;

    // 1. DASHBOARD
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("pendingCourses", courseService.getPendingCourses());
        model.addAttribute("allCourses", courseService.getAllkurs());
        return "admin/dashboard";
    }




    @GetMapping("/moderation")
    public String moderationPage(Model model) {
        model.addAttribute("activePage", "moderation");
        model.addAttribute("pendingCourses", courseService.getPendingCourses());
        // Təsdiq gözləyən mentorlar (isVerified = false olanlar)
        model.addAttribute("pendingMentors", userService.getUnverifiedMentors());
        return "admin/moderation";
    }
    // 3. BÜTÜN KURSLAR (admin-courses.html)
    @GetMapping("/courses")
    public String coursesPage(Model model) {
        model.addAttribute("activePage", "courses");
        model.addAttribute("allCourses", courseService.getAllkurs());
        return "admin/admin-courses"; // Fayl adın nədirsə o olmalıdır
    }

    // 4. MENTORLAR
    @GetMapping("/mentors")
    public String mentorsPage(Model model) {
        model.addAttribute("activePage", "mentors");
        model.addAttribute("mentors", mentorService.getAllMentor()); // Bazadan gələn siyahı
        return "admin/mentors";
    }







    // --- EMELİYYATLAR (POST METODLARI) ---

    @PostMapping("/course/approve/{id}")
    public String approveCourse(@PathVariable Long id) {
        courseService.approveCourse(id);
        return "redirect:/admin/moderation?approved";
    }

    @PostMapping("/course/reject/{id}")
    public String rejectCourse(@PathVariable Long id, @RequestParam(value = "reason", required = false) String reason) {
        String rejectReason = (reason == null || reason.trim().isEmpty()) ? "Səbəb qeyd edilməyib" : reason;
        courseService.rejectCourse(id, rejectReason);
        return "redirect:/admin/moderation?rejected";
    }

    @PostMapping("/course/delete/{id}")
    public String adminDelete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses?deleted";
    }

    // Mentoru bazadan tamamilə silmək
    @PostMapping("/mentor/delete/{id}")
    public String deleteMentor(@PathVariable Long id) {
        mentorService.mentorDelete(id);
        return "redirect:/admin/mentors?deleted";
    }



    // Mentoru (profil yenilənməsini) təsdiqləmək
    @PostMapping("/mentor/verify/{id}")
    public String verifyMentor(@PathVariable Long id) {
        userService.verifyMentor(id);
        return "redirect:/admin/moderation?verified";
    }

}