package BrainRise.BrainRise.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String iconClass;
    private String description;
    private Double price = 0.0;
    private String videoUrl;
    private Boolean certified = false;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved = false;


    private String mentorName;
    private String rejectReason;

    @ManyToMany
    @JoinTable(
            name = "course_user_relations",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> mentors;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Exams> exams;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CourseComment> comments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}