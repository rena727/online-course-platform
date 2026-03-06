package BrainRise.BrainRise.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User recipient;

    private String title;
    private String message;
    private String link;

    private boolean isRead = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    private String type;
}