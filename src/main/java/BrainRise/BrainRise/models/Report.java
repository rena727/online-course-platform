package BrainRise.BrainRise.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String subject;

    @Column(length = 2000)
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isRead = false;
}