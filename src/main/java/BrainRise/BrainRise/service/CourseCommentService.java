package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.models.CourseComment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseCommentService {

    CourseCommentDto toDtoWithReplies(CourseComment comment);

    void saveComment(Long courseId, CourseCommentDto commentDto, String username);
    List<CourseCommentDto> getCommentsByCourseId(Long courseId);

    @Transactional
    int likeComment(Long commentId);
}
