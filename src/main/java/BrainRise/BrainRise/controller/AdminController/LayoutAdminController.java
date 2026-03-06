package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.dto.BannerDto;
import BrainRise.BrainRise.dto.FootDto;
import BrainRise.BrainRise.dto.LayoutFormDto;
import BrainRise.BrainRise.service.BannerService;
import BrainRise.BrainRise.service.FootService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LayoutAdminController {

    private final BannerService bannerService;
    private final FootService footService;

    @GetMapping("/layout")
    public String layoutPage(Model model) {
        LayoutFormDto layoutFormDto = new LayoutFormDto();

        layoutFormDto.setBanner(bannerService.getBanner());
        layoutFormDto.setFoot(footService.getFoot());

        model.addAttribute("layoutForm", layoutFormDto);
        model.addAttribute("activePage", "layout");

        return "admin/layout-banner";
    }

    @PostMapping("/layout/save")
    public String saveLayout(@ModelAttribute("layoutForm") LayoutFormDto layoutFormDto,
                             RedirectAttributes redirectAttributes) {

        bannerService.updateBanner(layoutFormDto.getBanner().getId(), layoutFormDto.getBanner());
        footService.updateFood(layoutFormDto.getFoot().getId(), layoutFormDto.getFoot());

        redirectAttributes.addFlashAttribute("success", "Məlumatlar uğurla yeniləndi!");
        return "redirect:/admin/layout";
    }
}