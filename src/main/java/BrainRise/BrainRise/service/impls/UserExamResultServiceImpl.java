package BrainRise.BrainRise.service.impls;

import BrainRise.BrainRise.dto.UserExamResultDto;
import BrainRise.BrainRise.models.Exams;
import BrainRise.BrainRise.models.User;
import BrainRise.BrainRise.models.UserExamResult;
import BrainRise.BrainRise.repository.ExamsRepository;
import BrainRise.BrainRise.repository.UserExamResultRepository;
import BrainRise.BrainRise.repository.UserRepository;
import BrainRise.BrainRise.service.UserExamResultService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserExamResultServiceImpl implements UserExamResultService {
    private final UserExamResultRepository resultRepository;
    private final UserRepository userRepository;
    private final ExamsRepository examsRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void saveResult(Long examId, String username, int score) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        UserExamResult result = new UserExamResult();
        result.setUser(user);
        result.setExam(exam);
        result.setScore(score);
        result.setExamDate(LocalDateTime.now());
        result.setPassed(score >= 70); // Keçid balı 70

        resultRepository.save(result);
    }

    @Override
    public List<UserExamResultDto> getUserHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return resultRepository.findByUserId(user.getId()).stream()
                .map(res -> {
                    UserExamResultDto dto = modelMapper.map(res, UserExamResultDto.class);
                    dto.setExamName(res.getExam().getName());
                    return dto;
                }).collect(Collectors.toList());
    }
}
