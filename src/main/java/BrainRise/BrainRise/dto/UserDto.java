package BrainRise.BrainRise.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String password;
    private Set<String> roles;

    private String bio;
    private String specialty;
    private String imgUrl;
    private String linkedinUrl;
    private String githubUrl;

    private boolean isVerified = false;
    private String enrolledCourses;
}