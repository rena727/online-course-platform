package BrainRise.BrainRise.service;

import BrainRise.BrainRise.dto.UserExamResultDto;

import java.util.List;

public interface UserExamResultService {
    void saveResult(Long examId, String username, int score);
    List<UserExamResultDto> getUserHistory(String username);
}
