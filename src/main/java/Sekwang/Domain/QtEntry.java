package Sekwang.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

@Entity @Table(name="qt_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QtEntry {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private java.time.LocalDate qtDate;

    @Column(nullable=false, length=100)
    private String scriptureRef;

    @Lob
    private String meditation;

    @Lob
    private String prayerTopic;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="username", nullable=false)
    private Member member;

    @Column(nullable=false)
    private Integer likes = 0;

    @Column(nullable=false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(nullable=false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
}