package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.dto.QuestionDto;
import BrainRise.BrainRise.service.QuestionService;
import BrainRise.BrainRise.service.ExamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/exams/{examId}/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;
    private final ExamsService examsService;

    // Sualların siyahısı
    @GetMapping
    public String listQuestions(@PathVariable Long examId, Model model) {
        model.addAttribute("exam", examsService.getById(examId));
        model.addAttribute("questions", questionService.getQuestionsByExamId(examId));
        model.addAttribute("activePage", "exams");
        return "admin/exams/questions-list";
    }

    // Yeni sual formu
    @GetMapping("/create")
    public String createQuestionForm(@PathVariable Long examId, Model model) {
        model.addAttribute("question", new QuestionDto());
        model.addAttribute("examId", examId);
        model.addAttribute("activePage", "exams");
        return "admin/exams/question-form";
    }

    // Sualı yadda saxla
    @PostMapping("/save")
    public String saveQuestion(@PathVariable Long examId, @ModelAttribute QuestionDto questionDto) {
        questionService.createQuestion(examId, questionDto);
        return "redirect:/admin/exams/" + examId + "/questions";
    }

    // Sualı sil
    @PostMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long examId, @PathVariable Long id) {
        questionService.deleteQuestion(id);
        return "redirect:/admin/exams/" + examId + "/questions";
    }
}