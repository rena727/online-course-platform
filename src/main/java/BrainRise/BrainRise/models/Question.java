package BrainRise.BrainRise.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")

public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctIndex;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exams exam;
}