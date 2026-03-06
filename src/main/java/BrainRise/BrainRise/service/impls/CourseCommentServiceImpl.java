package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.CourseComment;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.CourseCommentRepository;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.CourseCommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseCommentServiceImpl implements CourseCommentService {

    private final CourseCommentRepository commentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
 private final UserRepository userRepository;
    @Override
    public List<CourseCommentDto> getCommentsByCourseId(Long courseId) {
        List<CourseComment> comments = commentRepository.findByCourseIdAndParentIsNull(courseId);
        return comments.stream()
                .map(this::toDtoWithReplies)
                .collect(Collectors.toList());
    }

    @Override
    public CourseCommentDto toDtoWithReplies(CourseComment comment) {
        if (comment == null) return null;

        CourseCommentDto dto = new CourseCommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikes(comment.getLikes());

        if (comment.getCourse() != null) {
            dto.setCourseId(comment.getCourse().getId());
        }


        if (comment.getUser() != null) {
            dto.setUserName(comment.getUser().getFullName());
        } else {
            dto.setUserName("Naməlum İstifadəçi");
        }

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            List<CourseCommentDto> replyDtos = comment.getReplies().stream()
                    .map(this::toDtoWithReplies)
                    .collect(Collectors.toList());
            dto.setReplies(replyDtos);
        } else {
            dto.setReplies(new java.util.ArrayList<>());
        }

        return dto;
    }

    @Override
    @Transactional
    public void saveComment(Long courseId, CourseCommentDto dto, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        CourseComment comment = new CourseComment();
        comment.setText(dto.getText());
        comment.setCourse(course);
        comment.setUser(user);

        if (dto.getParentId() != null) {
            CourseComment parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment tapılmadı"));
            comment.setParent(parent);
        }

        commentRepository.save(comment);
    }
    @Transactional
    @Override
    public int likeComment(Long commentId) {
        CourseComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Rəy tapılmadı"));

        int currentLikes = comment.getLikes();
        comment.setLikes(currentLikes + 1);

        commentRepository.save(comment);

        return comment.getLikes();
    }


}
