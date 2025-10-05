package Sekwang.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

@Entity @Table(name="notices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notice {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Lob
    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private Boolean isImportant = false;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="author")
    private Member author;

    @Column(nullable=false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Column(nullable=false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
}