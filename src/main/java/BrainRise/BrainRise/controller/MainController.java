//package BrainRise.BrainRise.controller;
//
//import BrainRise.BrainRise.dto.BlogDto;
//import BrainRise.BrainRise.dto.CourseDto;
//import BrainRise.BrainRise.dto.MentorDto;
//import BrainRise.BrainRise.dto.ExamsDto; // İmtahan DTO-nu import et
//import BrainRise.BrainRise.service.BlogService;
//import BrainRise.BrainRise.service.CourseService;
//import BrainRise.BrainRise.service.MentorService;
//import BrainRise.BrainRise.service.ExamsService; // İmtahan Servisini import et
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class MainController {
//
//    private final BlogService blogService;
//    private final MentorService mentorService;
//    private final CourseService courseService;
//    private final ExamsService examsService; // BU SƏTİRİ ƏLAVƏ ETDİK
//
//    @GetMapping("/")
//    public String indexPage(Model model) {
//        // Bloqlar
//        try {
//            model.addAttribute("blogs", blogService.getLatest3Blogs());
//        } catch (Exception e) {
//            model.addAttribute("blogs", new ArrayList<>());
//            System.err.println("Bloq xətası: " + e.getMessage());
//        }
//
//        // Mentorlar - Problem böyük ehtimalla buradadır!
//        try {
//            model.addAttribute("mentors", mentorService.getAllMentor());
//        } catch (Exception e) {
//            model.addAttribute("mentors", new ArrayList<>());
//            System.err.println("Mentor xətası: " + e.getMessage());
//        }
//
//        // Kurslar
//        try {
//            model.addAttribute("courses", courseService.getAllkurs());
//        } catch (Exception e) {
//            model.addAttribute("courses", new ArrayList<>());
//        }
//
//        // İmtahanlar
//        try {
//            model.addAttribute("exams", examsService.getAllExam());
//        } catch (Exception e) {
//            model.addAttribute("exams", new ArrayList<>());
//        }
//
//        return "index";
//    }
//}

package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.*;
import BrainRise.BrainRise.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BlogService blogService;
    private final MentorService mentorService;
    private final CourseService courseService;
    private final ExamsService examsService;
    private final BannerService bannerService;
    private final FootService footService;

    @GetMapping("/")
    public String indexPage(Model model) {


        try {
            model.addAttribute("banner", bannerService.getBanner());
            model.addAttribute("foot", footService.getFoot());
        } catch (Exception e) {
            model.addAttribute("banner", new BannerDto());
            model.addAttribute("foot", new FootDto());
            System.err.println("Layout xətası: " + e.getMessage());
        }

        // Bloqlar
        try {
            model.addAttribute("blogs", blogService.getLatest3Blogs());
        } catch (Exception e) {
            model.addAttribute("blogs", new ArrayList<>());
        }

        // Mentorlar
        try {
            model.addAttribute("mentors", mentorService.getAllMentor());
        } catch (Exception e) {
            model.addAttribute("mentors", new ArrayList<>());
        }

        // Kurslar
        try {
            model.addAttribute("courses", courseService.getAllkurs());
        } catch (Exception e) {
            model.addAttribute("courses", new ArrayList<>());
        }

        // İmtahanlar
        try {
            model.addAttribute("exams", examsService.getAllExam());
        } catch (Exception e) {
            model.addAttribute("exams", new ArrayList<>());
        }

        return "index";
    }
}