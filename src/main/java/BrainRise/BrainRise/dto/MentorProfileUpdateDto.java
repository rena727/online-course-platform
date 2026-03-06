package BrainRise.BrainRise.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorProfileUpdateDto {
    private String fullName;
    private String specialty;
    private String bio;
    private String imgUrl;
    private String linkedinUrl;
    private String githubUrl;
}