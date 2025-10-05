package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="ox_quiz_sets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OxQuizSet {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long setId;

    @Column(nullable=false, length=100) private String setName;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="created_by")
    private Member createdBy;

    @Column(nullable=false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}