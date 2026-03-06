package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.BlogDto;
import BrainRise.BrainRise.dto.CategoryDto;
import BrainRise.BrainRise.models.Blog;
import BrainRise.BrainRise.repository.BlogRepository;
import BrainRise.BrainRise.service.BlogService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    @Override
    public BlogDto getById(long id) {
        Blog blog=blogRepository.findById(id).orElseThrow(()->new RuntimeException("Blog tapilmaadi"));
        return modelMapper.map(blog,BlogDto.class);
    }

    @Override
    public List<BlogDto> getLatest3Blogs() {
        // Repository-dən son 3-ünü çəkirik
        return blogRepository.findFirst3ByOrderByIdDesc().stream()
                .map(blog -> {
                    BlogDto dto = modelMapper.map(blog, BlogDto.class);
                    if (blog.getCategory() != null) {
                        dto.setCategoryName(blog.getCategory().getName());
                    }
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<BlogDto> getAllBlog() {
        List<Blog> blogs = blogRepository.findAll();

        return blogs.stream()
                .map(blog -> {
                    BlogDto dto = modelMapper.map(blog, BlogDto.class);

                    if (blog.getCategory() != null) {
                        // Kateqoriya obyektini öz DTO-suna çevirib set edirik
                        CategoryDto catDto = modelMapper.map(blog.getCategory(), CategoryDto.class);
                        dto.setCategoryDto(catDto);
                    }

                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public BlogDto createBlog(BlogDto blogDto) {
        Blog blog=modelMapper.map(blogDto,Blog.class);
        Blog saveBlog=blogRepository.save(blog);
        return modelMapper.map(saveBlog,BlogDto.class);
    }

    @Override
    public BlogDto updateBlog(long id, BlogDto blogDto) {
        Blog blog=blogRepository.findById(id).orElseThrow(()->new RuntimeException("Blog tapilmadi"));
        modelMapper.map(blogDto,blog);
        blog.setId(id);
        Blog update=blogRepository.save(blog);
        return modelMapper.map(update,BlogDto.class);
    }

    @Override
    public void deleteBlog(long id) {
        if(!blogRepository.existsById(id))
        {
            throw  new RuntimeException("Blig tapilmadi");
        }
        blogRepository.deleteById(id);
    }
}
