package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Integer rating;
    private String comment;
    private Long mentorId;
    private String userName;
    private String userFullName;
}
