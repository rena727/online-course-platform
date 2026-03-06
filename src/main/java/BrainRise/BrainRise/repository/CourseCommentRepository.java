package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseCommentRepository extends JpaRepository<CourseComment,Long> {
    List<CourseComment> findByCourseIdOrderByCreatedAtDesc(Long courseId);
    List<CourseComment> findByCourseIdAndParentIsNull(Long courseId);
    List<CourseComment> findByCourseMentorsIdOrderByCreatedAtDesc(Long mentorId);

}
