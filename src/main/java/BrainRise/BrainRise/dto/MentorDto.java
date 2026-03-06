package BrainRise.BrainRise.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorDto {

    private Long id;
    private String name;
    private String imgUrl;
    private String specialty;
    private String linkedinUrl;
    private String gitUrl;
    private List<ReviewDto> reviews;
    private String email;
    private int courseCount;

    @JsonIgnore
    private List<CourseDto> courses;

    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        double sum = reviews.stream().mapToDouble(ReviewDto::getRating).sum();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}
