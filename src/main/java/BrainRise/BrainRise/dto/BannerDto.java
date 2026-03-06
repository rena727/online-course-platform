package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
    private Long id;
    private String title;
    private String subTitle;
}
