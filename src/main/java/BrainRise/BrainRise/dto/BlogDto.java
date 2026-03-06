package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {

    private Long id;
    private String title;
    private String subTitle;
    private String categoryName;
    private String topicName;
    private String topicDescription;
    private String imageUrl;
    private CategoryDto categoryDto;
}