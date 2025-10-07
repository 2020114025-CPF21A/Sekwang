package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@EntityListeners(AuditingEntityListener.class) // ✅ Auditing 활성화
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Song {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "uploader")
    private Member uploader;

    @Column(nullable = false, length = 200)
    private String title;

    private String artist;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Category category = Category.기타;

    public enum Category { 찬양, 경배, 복음성가, CCM, 기타 }

    private String musicalKey;
    private Integer tempoBpm;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default                                      // ✅ 빌더 초기값 적용
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist                                           // ✅ 안전망(감사 미동작/빌더 null 방지)
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
