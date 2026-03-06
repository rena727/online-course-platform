package BrainRise.BrainRise.service.impls;
import BrainRise.BrainRise.dto.CourseCommentDto;
import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.dto.UserDto;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.CourseComment;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final BrainRise.BrainRise.service.CourseCommentService courseCommentService;

    @Override
    @Transactional(readOnly = true)
    public CourseDto getById(Long id) {
        Course kurs = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kurs tapılmadı!"));

        CourseDto dto = new CourseDto();
        dto.setId(kurs.getId());
        dto.setName(kurs.getName());
        dto.setPrice(kurs.getPrice() != null ? kurs.getPrice() : 0.0);
        dto.setDescription(kurs.getDescription());
        dto.setVideoUrl(kurs.getVideoUrl());
        dto.setIconClass(kurs.getIconClass());
        dto.setCertified(kurs.getCertified() != null ? kurs.getCertified() : false);

        dto.setApproved(kurs.isApproved());
        dto.setRejectReason(kurs.getRejectReason());
        dto.setMentorName(kurs.getMentorName());

        dto.setMentors(new java.util.ArrayList<>());
        dto.setLessons(new java.util.ArrayList<>());

        dto.setComments(kurs.getComments() != null ? kurs.getComments().stream()
                .filter(c -> c.getParent() == null)
                .map(courseCommentService::toDtoWithReplies)
                .collect(Collectors.toList()) : new java.util.ArrayList<>());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllkurs() {
        return courseRepository.findAll().stream()
                .filter(Course::isApproved)
                .map(kurs -> {
                    // 1. Əsas mapping
                    CourseDto dto = modelMapper.map(kurs, CourseDto.class);
                    dto.setMentorName(kurs.getMentorName());

                    if (kurs.getComments() != null) {
                        dto.setComments(kurs.getComments().stream()
                                .filter(c -> c.getParent() == null)
                                .map(courseCommentService::toDtoWithReplies)
                                .collect(Collectors.toList()));
                    } else {
                        dto.setComments(new java.util.ArrayList<>());
                    }

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveWithMentor(CourseDto dto, String input) {
        User mentor = userRepository.findByUsernameOrEmail(input, input)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor tapılmadı!"));

        Course course = new Course();
        course.setName(dto.getName());
        course.setPrice(dto.getPrice());
        course.setVideoUrl(dto.getVideoUrl());
        course.setDescription(dto.getDescription());
        course.setIconClass(dto.getIconClass());

        course.setApproved(false);
        course.setCertified(false);
        course.setMentorName(mentor.getFullName());

        course.setMentors(new java.util.ArrayList<>(Collections.singletonList(mentor)));
        courseRepository.save(course);
    }
    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByMentor(String input) {
        User user = userRepository.findByUsernameOrEmail(input, input)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor tapılmadı!"));

        return courseRepository.findAll().stream()
                .filter(c -> c.getMentors() != null &&
                        c.getMentors().stream().anyMatch(m -> m.getId().equals(user.getId())))
                .map(course -> {
                    CourseDto dto = modelMapper.map(course, CourseDto.class);
                    dto.setApproved(course.isApproved());
                    dto.setRejectReason(course.getRejectReason());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getPendingCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.isApproved() && (course.getRejectReason() == null || course.getRejectReason().isEmpty()))
                .map(course -> {
                    CourseDto dto = modelMapper.map(course, CourseDto.class);
                    dto.setMentorName(course.getMentorName());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        course.setApproved(true);
        course.setCertified(true);
        course.setRejectReason(null);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void rejectCourse(Long id, String reason) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        course.setApproved(false);
        course.setCertified(false);
        course.setRejectReason(reason);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        if (course.getMentors() != null) {
            course.getMentors().forEach(m -> m.getCourses().remove(course));
            course.getMentors().clear();
        }

        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public CourseDto update(Long id, CourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        course.setName(dto.getName());
        course.setPrice(dto.getPrice());
        course.setDescription(dto.getDescription());
        course.setVideoUrl(dto.getVideoUrl());

        course.setApproved(false);
        course.setRejectReason(null);

        Course updated = courseRepository.save(course);

        return getById(updated.getId());
    }

    @Override
    public CourseDto create(CourseDto kurslarDto) {
        return null;
    }
    @Override
    public List<CourseDto> getAllApprovedCourses() {
        List<Course> approvedCourses = courseRepository.findAllByApprovedTrue();

        return approvedCourses.stream()
                .map(course -> {
                    CourseDto dto = new CourseDto();
                    dto.setId(course.getId());
                    dto.setName(course.getName());
                    dto.setPrice(course.getPrice());
                    dto.setMentorName(course.getMentorName());
                    dto.setApproved(course.isApproved());
                    return dto;
                })
                .collect(Collectors.toList());
    }
//    @Override
//    public List<UserDto> getEnrolledStudents(Long courseId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
//
//        // DÜZƏLİŞ: Kursu alan bütün istifadəçiləri tapırıq
//        return userRepository.findAll().stream()
//                .filter(user -> user.getCourses().contains(course)) // Bu kursu alaraq qeydiyyatdan keçmiş tələbələr
//                .filter(user -> user.getRoles().stream()
//                        .noneMatch(role -> role.getName().equals("ROLE_MENTOR"))) // Mentorları çıxarırıq
//                .map(user -> {
//                    UserDto d = new UserDto();
//                    d.setId(user.getId());
//                    d.setFullName(user.getFullName());
//                    d.setEmail(user.getEmail());
//                    d.setUsername(user.getUsername());
//                    return d;
//                }).collect(Collectors.toList());
//    }
@Override
public List<UserDto> getEnrolledStudents(Long courseId) {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

    return userRepository.findAll().stream()
            .filter(user -> user.getCourses().contains(course))
            .filter(user -> user.getRoles().stream()
                    .noneMatch(role -> role.getName().equals("ROLE_MENTOR")))
            .map(user -> {
                UserDto d = new UserDto();
                d.setId(user.getId());
                d.setFullName(user.getFullName());
                d.setEmail(user.getEmail());
                d.setUsername(user.getUsername());

                d.setEnrolledCourses(course.getName());

                return d;
            }).collect(Collectors.toList());
}

}
