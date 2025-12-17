package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_answer_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String quizType; // "mc", "ox", "speed"

    @Column(nullable = false)
    private Long resultId; // 결과 ID

    @Column(nullable = false)
    private Integer questionIndex; // 문제 순서 (0-based)

    @Column(nullable = false, length = 1000)
    private String question;

    @Column(nullable = false, length = 500)
    private String userAnswer;

    @Column(length = 500)
    private String correctAnswer;

    @Column(nullable = false)
    private Boolean correct;

    @Builder.Default
    @Column(nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}
