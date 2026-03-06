package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.BlogDto;

import java.util.List;

public interface BlogService {
    BlogDto getById(long id);
    List<BlogDto> getAllBlog();
    BlogDto createBlog(BlogDto blogDto);
    BlogDto updateBlog(long id,BlogDto blogDto);
    void deleteBlog(long id);
    List<BlogDto> getLatest3Blogs();
}
