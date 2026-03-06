package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.QuestionDto;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> getQuestionsByExamId(Long examId);
    QuestionDto createQuestion(Long examId, QuestionDto questionDto);
    void deleteQuestion(Long id);
}