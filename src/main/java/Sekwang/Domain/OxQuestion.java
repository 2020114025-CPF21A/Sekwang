package Sekwang.Domain;


import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="ox_questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OxQuestion {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="set_id", nullable=false)
    private OxQuizSet set;

    @Lob @Column(nullable=false) private String question;

    @Column(nullable=false) private Integer answer; // 1(O) or 2(X)
}