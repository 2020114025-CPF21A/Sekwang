package Sekwang.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

@Entity @Table(name="offerings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Offering {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="username", nullable=false)
    private Member member;

    @Column(nullable=false, precision=12, scale=2)
    private java.math.BigDecimal amount;

    private String note;

    @Column(nullable=false)
    private java.time.LocalDateTime offeredAt = java.time.LocalDateTime.now();
}