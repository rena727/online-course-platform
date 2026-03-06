package BrainRise.BrainRise.controller.AdminController;

import BrainRise.BrainRise.dto.BlogDto;
import BrainRise.BrainRise.service.BlogService;
import BrainRise.BrainRise.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/blog")
@RequiredArgsConstructor
public class AdminBlogController {

    private final BlogService blogService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogService.getAllBlog());
        return "admin/blog/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("blog", new BlogDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/blog/form";
    }

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute("blog") BlogDto blogDto) {
        blogService.createBlog(blogDto);
        return "redirect:/admin/blog";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/blog/form";
    }

    @PostMapping("/update/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute("blog") BlogDto blogDto) {
        blogService.updateBlog(id, blogDto);
        return "redirect:/admin/blog";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return "redirect:/admin/blog";
    }
}
