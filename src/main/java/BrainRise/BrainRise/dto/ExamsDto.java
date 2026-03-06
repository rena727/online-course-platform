package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamsDto {

    private Long id;
    private String imageUrl;
    private String level;
    private int questionCount;
    private int examDuration;
    private String type;
    private String name;
    private String price;

    private String courseName;
    private List<QuestionDto> questions;
}