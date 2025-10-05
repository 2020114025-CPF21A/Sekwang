package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="speed_quiz_results",
        uniqueConstraints=@UniqueConstraint(columnNames={"username","set_id","taken_at"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpeedQuizResult {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="username", nullable=false)
    private Member user;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="set_id", nullable=false)
    private SpeedQuizSet set;

    @Column(nullable=false) private Integer score;
    @Column(nullable=false) private java.time.LocalDateTime takenAt = java.time.LocalDateTime.now();
}