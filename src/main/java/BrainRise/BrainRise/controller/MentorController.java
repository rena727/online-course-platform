package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.MentorDto;
import BrainRise.BrainRise.dto.ReviewDto;
import BrainRise.BrainRise.service.MentorService;
import BrainRise.BrainRise.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;
    private final ReviewService reviewService;

    // Bütün mentorların siyahısı
    @GetMapping
    public String showAllMentors(Model model) {
        List<MentorDto> mentors = mentorService.getAllMentor();
        model.addAttribute("mentors", mentors);
        return "mentor/list";
    }

    @GetMapping("/{id}")
    public String showMentorDetails(@PathVariable Long id, Model model) {
        MentorDto mentor = mentorService.getById(id);


        List<ReviewDto> reviews = reviewService.getReviewsByMentorId(id);

        model.addAttribute("mentor", mentor);
        model.addAttribute("reviews", reviews);


        return "mentor/details";
    }

    @PostMapping("/rate")
    public String addRating(@ModelAttribute ReviewDto reviewDto, java.security.Principal principal) {

        if (principal != null) {
            reviewDto.setUserName(principal.getName());
        }

        reviewService.addReview(reviewDto);
        return "redirect:/mentors/" + reviewDto.getMentorId();
    }
}