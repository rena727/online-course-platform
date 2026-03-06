package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.models.CourseComment;

import java.util.List;

public interface CourseService {
    // Mövcud metodlar (toxunulmadı)
    CourseDto getById(Long id);
    List<CourseDto> getAllkurs();
    CourseDto update(Long id, CourseDto kurslarDto);
    CourseDto create(CourseDto kurslarDto);
    void deleteCourse(Long id);
    List<CourseDto> getPendingCourses();
    void approveCourse(Long id);
    void saveWithMentor(CourseDto dto, String mentorEmail);
    List<CourseDto> getCoursesByMentor(String email);

    // Yeni əlavə edilən metod (Adminin rədd etməsi üçün)
    void rejectCourse(Long id, String reason);
    // Bu sətri siyahıya əlavə et:
    List<CourseDto> getAllApprovedCourses();
    List<UserDto> getEnrolledStudents(Long courseId);

}