package BrainRise.BrainRise.dto;

import lombok.Data;

@Data
public class QuestionDto {
    private Long id;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctIndex;
}