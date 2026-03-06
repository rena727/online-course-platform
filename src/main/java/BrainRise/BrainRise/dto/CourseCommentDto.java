package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCommentDto {

    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Long courseId;
    private Long parentId;
    private int likes = 0;
    private List<CourseCommentDto> replies = new ArrayList<>();
    private String userName;
}
