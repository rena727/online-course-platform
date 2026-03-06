package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.models.Report;
import BrainRise.BrainRise.repository.ReportRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.NotificationService;
import BrainRise.BrainRise.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public String listReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("activePage", "reports");
        return "admin/reports/list";
    }

    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable Long id) {
        reportService.markAsRead(id);
        return "redirect:/admin/reports";
    }

    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/admin/reports";
    }

    @PostMapping("/reply/{id}")
    public String replyToReport(@PathVariable Long id, @RequestParam String replyMessage) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Şikayət tapılmadı: " + id));


        userRepository.findByEmail(report.getEmail()).ifPresent(user -> {

            notificationService.createNotification(
                    user.getId(),
                    "Şikayətinizə Cavab",
                    "Sizin '" + report.getSubject() + "' mövzulu müraciətinizə cavab: " + replyMessage,
                    "SYSTEM"
            );
        });


        reportService.markAsRead(id);

        return "redirect:/admin/reports?success=replied";
    }
}