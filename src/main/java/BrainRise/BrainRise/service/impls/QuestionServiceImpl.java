package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.QuestionDto;
import BrainRise.BrainRise.models.Exams;
import BrainRise.BrainRise.models.Question;
import BrainRise.BrainRise.repository.ExamsRepository;
import BrainRise.BrainRise.repository.QuestionRepository;
import BrainRise.BrainRise.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ExamsRepository examsRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId).stream()
                .map(q -> modelMapper.map(q, QuestionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionDto createQuestion(Long examId, QuestionDto questionDto) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Question question = modelMapper.map(questionDto, Question.class);
        question.setExam(exam);

        Question savedQuestion = questionRepository.save(question);
        return modelMapper.map(savedQuestion, QuestionDto.class);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}