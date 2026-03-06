package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.service.CourseCommentService;
import BrainRise.BrainRise.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseCommentService courseCommentService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @GetMapping
    public String showCourses(Model model) {
        model.addAttribute("courses", courseService.getAllkurs());
        return "courses/list";
    }



    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model, Principal principal) {
        CourseDto course = courseService.getById(id);
        model.addAttribute("course", course);

        boolean isEnrolled = false;
        if (principal != null) {

            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {

                isEnrolled = user.getCourses().stream()
                        .anyMatch(c -> c.getId().equals(id));
            }
        }

        model.addAttribute("isEnrolled", isEnrolled);
        model.addAttribute("lessons", course.getLessons());
        return "courses/details";
    }


    @PostMapping("/{courseId}/comment/reply")
    public String replyComment(@PathVariable Long courseId,
                               @RequestParam Long commentId,
                               @RequestParam String text,
                               Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        CourseCommentDto dto = new CourseCommentDto();
        dto.setText(text);
        dto.setParentId(commentId);


        courseCommentService.saveComment(courseId, dto, principal.getName());

        return "redirect:/courses/" + courseId;
    }
    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam("text") String text, Principal principal) {
        if (principal == null) return "redirect:/login";

        CourseCommentDto commentDto = new CourseCommentDto();
        commentDto.setText(text);

        courseCommentService.saveComment(id, commentDto, principal.getName());

        return "redirect:/courses/" + id;
    }




    @PostMapping("/enroll")
    public String enroll(@RequestParam Long courseId, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));


        if (!user.getCourses().contains(course)) {
            user.getCourses().add(course);
        }


        if (!course.getMentors().contains(user)) {
            course.getMentors().add(user);
        }


        userRepository.save(user);
        courseRepository.save(course);

        return "redirect:/courses/" + courseId + "?enrolled=true";
    }


    @PostMapping("/save-new")
    public String createCourse(@ModelAttribute CourseDto courseDto, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        System.out.println(">>> DEBUG: Controller-ə giriş edildi. User: " + principal.getName());
        courseService.saveWithMentor(courseDto, principal.getName());

        return "redirect:/admin/mentor-dashboard";    }

    @PostMapping("/delete/{id}")
    public String mentorDeleteCourse(@PathVariable Long id, Principal principal) {

        courseService.deleteCourse(id);
        return "redirect:/admin/mentor-dashboard?deleted=true";    }


    @PostMapping("/{courseId}/comment/{commentId}/like")
    @ResponseBody
    public int likeComment(@PathVariable Long courseId, @PathVariable Long commentId) {
        return courseCommentService.likeComment(commentId);
    }

    @PostMapping("/{courseId}/comment/reply-ajax")
    @ResponseBody
    public String replyCommentAjax(@PathVariable Long courseId,
                                   @RequestParam Long commentId,
                                   @RequestParam String text,
                                   Principal principal) {
        if (principal == null) return "error";

        CourseCommentDto dto = new CourseCommentDto();
        dto.setText(text);
        dto.setParentId(commentId);
        courseCommentService.saveComment(courseId, dto, principal.getName());

        return "success";
    }
    @GetMapping("/{id}/checkout")
    public String showCheckout(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        CourseDto course = courseService.getById(id);
        model.addAttribute("course", course);
        return "courses/checkout";
    }


    @PostMapping("/{id}/pay")
    public String processPayment(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));


        if (!user.getCourses().contains(course)) {
            user.getCourses().add(course);
            userRepository.save(user);
        }

        return "redirect:/courses/" + id + "?success=true";
    }


}