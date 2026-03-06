package BrainRise.BrainRise.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String imgUrl;
    private String name;
    private String specialty;
    private String linkedinUrl;
    private String gitUrl;
    private String email;
    @Transient
    private int courseCount;


    @ManyToMany
    @JoinTable(
            name = "mentor_course_legacy",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonBackReference
    private List<Course> courses;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    public Double getAverageRating() {
        if (this.reviews == null || this.reviews.isEmpty()) return 0.0;
        double sum = reviews.stream()
                .filter(r -> r != null && r.getRating() != null)
                .mapToDouble(Review::getRating).sum();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0;
    }
}