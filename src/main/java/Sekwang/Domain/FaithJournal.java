package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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

    @Builder.Default
    @Column(nullable=false) private Integer views = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default                                      // ✅ 빌더 초기값 적용
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreatedDate
    @Column(name = "updated_at", nullable = false, updatable = false)
    @Builder.Default                                      // ✅ 빌더 초기값 적용
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();

    @PreUpdate void onUpdate(){ this.updatedAt = java.time.LocalDateTime.now(); }

}
