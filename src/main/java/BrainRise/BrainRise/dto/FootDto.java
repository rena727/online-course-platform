package BrainRise.BrainRise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FootDto {
    private long id;
    private String title;
    private String instagramUrl;
    private String linkedinUrl;
    private String facebookUrl;
}
