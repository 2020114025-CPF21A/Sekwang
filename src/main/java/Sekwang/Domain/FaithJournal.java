package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="faith_journals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FaithJournal {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author", nullable=false)
    private Member author;

    @Column(nullable=false) private Integer moodCode;
    @Column(nullable=false) private Integer weatherCode;
    @Column(nullable=false, length=200) private String title;

    @Lob @Column(nullable=false) private String content;

    @Column(nullable=false) private Integer views = 0;

    @Column(nullable=false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    @Column(nullable=false) private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    @PreUpdate void onUpdate(){ this.updatedAt = java.time.LocalDateTime.now(); }
}
