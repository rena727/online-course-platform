package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.LessonDto;
import BrainRise.BrainRise.service.CourseService;
import BrainRise.BrainRise.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final CourseService courseService;


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("lesson", new LessonDto());
        model.addAttribute("courses", courseService.getAllkurs());
        return "admin/add-lesson";
    }


    @PostMapping("/save")
    public String saveLesson(@ModelAttribute("lesson") LessonDto lessonDto, @RequestParam("courseId") Long courseId) {
        lessonService.saveLesson(courseId, lessonDto);
        return "redirect:/courses/" + courseId;
    }
}