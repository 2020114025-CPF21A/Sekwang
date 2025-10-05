package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="speed_questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpeedQuestion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="set_id", nullable=false)
    private SpeedQuizSet set;

    @Lob @Column(nullable=false) private String question;

    private String accept1;
    private String accept2;
    private String accept3;
}