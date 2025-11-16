package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="gallery")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GalleryItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200) private String title;
    private String category;

    @Column(nullable=false, length=500) private String fileUrl;
    @Lob private String description;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="uploader")
    private Member uploader;

    @Column(nullable=false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
    
    // 다중 이미지 그룹핑용 (같은 groupId = 같은 카드)
    @Column(name="group_id", length=100)
    private String groupId;
}