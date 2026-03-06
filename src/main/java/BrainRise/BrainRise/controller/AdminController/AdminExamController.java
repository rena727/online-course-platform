package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.dto.ExamsDto;
import BrainRise.BrainRise.service.ExamsService;
import BrainRise.BrainRise.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/exams")
@RequiredArgsConstructor
public class AdminExamController {

    private final ExamsService examsService;
    private final CourseService courseService;

    // 1. İmtahanlar siyahısı
    @GetMapping
    public String listExams(Model model) {
        model.addAttribute("activePage", "exams"); // <-- aktiv səhifə
        model.addAttribute("exams", examsService.getAllExam());
        return "admin/exams/list";
    }


    // 2. İmtahan əlavə et formu
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("activePage", "exams"); // <-- aktiv səhifə
        model.addAttribute("exam", new ExamsDto());
        model.addAttribute("courses", courseService.getAllkurs());
        return "admin/exams/form";
    }

    // 3. Yeni imtahanı saxlamaq
    @PostMapping("/save")
    public String saveExam(@ModelAttribute("exam") ExamsDto examDto) {
        examsService.createExam(examDto);
        return "redirect:/admin/exams";
    }

    // 4. İmtahan redaktə formu
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "exams"); // <-- aktiv səhifə
        model.addAttribute("exam", examsService.getById(id));
        model.addAttribute("courses", courseService.getAllkurs());
        return "admin/exams/form";
    }

    // 5. İmtahan update etmək
    @PostMapping("/update/{id}")
    public String updateExam(@PathVariable Long id, @ModelAttribute("exam") ExamsDto examDto) {
        examsService.updateExam(id, examDto);
        return "redirect:/admin/exams";
    }

    // 6. İmtahan silmək
    @PostMapping("/delete/{id}")
    public String deleteExam(@PathVariable Long id) {
        examsService.deleteExam(id);
        return "redirect:/admin/exams";
    }
}
