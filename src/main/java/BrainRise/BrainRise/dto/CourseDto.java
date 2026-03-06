package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private String iconClass;
    private String description;
    private Double price = 0.0;
    private String videoUrl;
    private Boolean certified = false;
    private List<MentorDto> mentors = new java.util.ArrayList<>();
    private List<LessonDto> lessons;
    private String mentorName;// bu əlavə et
    private boolean isApproved = false;
    private String rejectReason; // Adminin yazdığı rədd səbəbi bura yazılacaq
    private List<CourseCommentDto> comments = new java.util.ArrayList<>();



}
