package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.ExamsDto;
import BrainRise.BrainRise.dto.UserExamResultDto;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.models.Exams;
import BrainRise.BrainRise.models.UserExamResult;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.repository.ExamsRepository;
import BrainRise.BrainRise.repository.UserExamResultRepository;
import BrainRise.BrainRise.service.ExamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/exams")
public class ExamsController {

    private final ExamsService examsService;
    private final UserRepository userRepository;
    private final ExamsRepository examsRepository;
    private final UserExamResultRepository userExamResultRepository;

    @GetMapping
    public String listExams(Model model, Principal principal) {

        List<ExamsDto> allExams = examsService.getAllExam();
        model.addAttribute("exams", allExams != null ? allExams : new ArrayList<>());


        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            if (user == null) {

                return "redirect:/logout";
            }


            if (user.getExamResults() == null) user.setExamResults(new ArrayList<>());

            model.addAttribute("user", user);
        }

        return "exams/list";
    }

    @GetMapping("/{id:[0-9]+}/quiz")
    public String startQuiz(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            if (user == null) return "redirect:/logout";


            boolean alreadyPassed = false;
            if (user.getExamResults() != null) {
                alreadyPassed = user.getExamResults().stream()
                        .anyMatch(r -> r.getExam() != null &&
                                r.getExam().getId().equals(id) &&
                                r.getScore() >= 50);
            }

            if (alreadyPassed) {
                return "redirect:/exams";
            }
        }

        ExamsDto exam = examsService.getById(id);
        if (exam == null) return "redirect:/exams";

        if (exam.getQuestions() == null) exam.setQuestions(new ArrayList<>());

        model.addAttribute("exam", exam);
        return "exams/quiz";
    }

    @PostMapping("/save-result")
    @ResponseBody
    public ResponseEntity<?> saveResult(@RequestBody UserExamResultDto resultDto, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("Giriş edilməyib");

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body("İstifadəçi tapılmadı");

        Exams exam = examsRepository.findById(resultDto.getExamId()).orElse(null);
        if (exam == null) return ResponseEntity.status(404).body("İmtahan tapılmadı");

        UserExamResult result = new UserExamResult();
        result.setUser(user);
        result.setExam(exam);
        result.setScore(resultDto.getScore());
        result.setExamDate(LocalDateTime.now());

        userExamResultRepository.save(result);

        return ResponseEntity.ok("Nəticə uğurla yadda saxlanıldı");
    }

}