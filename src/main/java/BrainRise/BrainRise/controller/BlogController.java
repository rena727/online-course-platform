package BrainRise.BrainRise.controller;

import BrainRise.BrainRise.dto.BlogDto;
import BrainRise.BrainRise.dto.CategoryDto;
import BrainRise.BrainRise.service.BlogService;
import BrainRise.BrainRise.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blog") // Brauzerdə əsas yol: /blog
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final CategoryService categoryService;


    @GetMapping
    public String blogPage(Model model) {
        List<BlogDto> blogs = blogService.getAllBlog();
        List<CategoryDto> categories = categoryService.getAllCategories();

        System.out.println("DIQQET: Bazadan gələn kateqoriya sayı ===> " + categories.size());

        model.addAttribute("blogs", blogs);
        model.addAttribute("categories", categories);
        return "blog/blog-list";
    }


    @GetMapping("/{id}")
    public String getBlogDetail(@PathVariable Long id, Model model) {
        BlogDto blog = blogService.getById(id);
        model.addAttribute("blog", blog);
        return "blog/blog-detail";
    }
}