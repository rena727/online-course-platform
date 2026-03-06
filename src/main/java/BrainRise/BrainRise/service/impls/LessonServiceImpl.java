package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.LessonDto;
import BrainRise.BrainRise.dto.MentorDto;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.Lesson;
import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.LessonRepository;
import BrainRise.BrainRise.repository.MentorRepository;
import BrainRise.BrainRise.service.LessonService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final MentorRepository mentorRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        modelMapper.typeMap(Mentor.class, MentorDto.class).addMappings(mapper -> {
            mapper.skip(MentorDto::setCourses);
        });
    }


    @Override
    @Transactional
    public LessonDto saveLesson(Long courseId, LessonDto lessonDto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
        lesson.setCourse(course);

        if (lessonDto.getMentor() != null && lessonDto.getMentor().getId() != null) {
            Mentor mentor = mentorRepository.findById(lessonDto.getMentor().getId())
                    .orElseThrow(() -> new RuntimeException("Mentor tapılmadı"));
            lesson.setMentor(mentor);
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        return modelMapper.map(savedLesson, LessonDto.class);
    }

    @Override
    public List<LessonDto> getLessonsByCourseId(Long courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        return lessons.stream().map(lesson -> {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setTitle(lesson.getTitle());
            dto.setVideoUrl(lesson.getVideoUrl());
            return dto;
        }).collect(Collectors.toList());
    }
}