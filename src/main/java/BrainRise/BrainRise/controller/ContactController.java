package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.models.Report;
import BrainRise.BrainRise.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



    @Controller
    @RequestMapping("/contact")
    @RequiredArgsConstructor
    public class ContactController {

        private final ReportService reportService;
        private final BrainRise.BrainRise.service.FootService footService;

        @GetMapping
        public String showContactPage(org.springframework.ui.Model model) {
            model.addAttribute("foot", footService.getFoot());
            return "contact";
        }

        @PostMapping("/send")
        public String sendReport(@ModelAttribute Report report) {
            reportService.saveReport(report);
            return "redirect:/contact?success";
        }
    }
