package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.ExamsDto;
import BrainRise.BrainRise.dto.QuestionDto;
import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.Exams;
import BrainRise.BrainRise.repository.CourseRepository;
import BrainRise.BrainRise.repository.ExamsRepository;
import BrainRise.BrainRise.service.ExamsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamsServiceImpl implements ExamsService {

    private final ExamsRepository examsRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;

    @Override
    @Transactional(readOnly = true)
    public ExamsDto getById(Long id) {
        Exams exam = examsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));

        ExamsDto dto = modelMapper.map(exam, ExamsDto.class);

        if (exam.getQuestions() != null) {
            List<QuestionDto> questionDtos = exam.getQuestions().stream()
                    .map(q -> modelMapper.map(q, QuestionDto.class))
                    .collect(Collectors.toList());
            dto.setQuestions(questionDtos);
        } else {
            dto.setQuestions(new ArrayList<>());
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamsDto> getAllExam() {
        return examsRepository.findAll().stream()
                .map(exam -> {
                    ExamsDto dto = modelMapper.map(exam, ExamsDto.class);

                    if (exam.getCourse() != null) {
                        dto.setCourseName(exam.getCourse().getName());
                    }

                    if (exam.getQuestions() != null) {
                        dto.setQuestionCount(exam.getQuestions().size());
                    } else {
                        dto.setQuestionCount(0);
                    }

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ExamsDto updateExam(Long id, ExamsDto examDto) {
        Exams existingExam = examsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İmtahan tapılmadı: " + id));

        existingExam.setName(examDto.getName());
        existingExam.setImageUrl(examDto.getImageUrl());
        existingExam.setLevel(examDto.getLevel());
        existingExam.setQuestionCount(examDto.getQuestionCount());
        existingExam.setExamDuration(examDto.getExamDuration());
        existingExam.setType(examDto.getType());
        existingExam.setPrice(examDto.getPrice());

        if (examDto.getCourseName() != null) {
            Course course = courseRepository.findByName(examDto.getCourseName())
                    .orElse(null);
            existingExam.setCourse(course);
        }

        Exams updatedExam = examsRepository.save(existingExam);
        return modelMapper.map(updatedExam, ExamsDto.class);
    }
    @Override
    @Transactional
    public ExamsDto createExam(ExamsDto examDto) {
        // 1. DTO-dan Entity-ə çevir
        Exams exam = modelMapper.map(examDto, Exams.class);

        if (examDto.getCourseName() != null) {
            Course course = courseRepository.findByName(examDto.getCourseName())
                    .orElseThrow(() -> new RuntimeException("Kurs tapılmadı: " + examDto.getCourseName()));
            exam.setCourse(course);
        }

        Exams savedExam = examsRepository.save(exam);
        return modelMapper.map(savedExam, ExamsDto.class);
    }

    @Override
    @Transactional
    public void deleteExam(long id) {
        if (!examsRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Exam not found with id: " + id);
        }
        examsRepository.deleteById(id);
    }
}