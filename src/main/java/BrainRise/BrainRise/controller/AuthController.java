package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto,
                               @RequestParam("selectedRole") String selectedRole) {
        userService.registerUser(userDto, userDto.getPassword(), selectedRole);
        return "redirect:/login?success";
    }
}