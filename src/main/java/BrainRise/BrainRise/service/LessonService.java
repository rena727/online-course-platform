package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.LessonDto;

import java.util.List;

public interface LessonService {
    LessonDto saveLesson(Long courseId, LessonDto lessonDto);
    List<LessonDto> getLessonsByCourseId(Long courseId);
}
