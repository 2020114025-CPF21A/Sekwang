package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="mc_questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class McQuestion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="set_id", nullable=false)
    private McQuizSet set;

    @Lob @Column(nullable=false) private String question;
    @Column(nullable=false) private String choice1;
    @Column(nullable=false) private String choice2;
    @Column(nullable=false) private String choice3;
    @Column(nullable=false) private String choice4;

    @Column(nullable=false) private Integer answerNo; // 1..4
}