package BrainRise.BrainRise.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserExamResultDto {

    private Long id;
    private Long userId;
    private Long examId;
    private String examName;
    private int score;
    private boolean isPassed;
    private LocalDateTime examDate;
}
