package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="songs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Song {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="uploader")
    private Member uploader;

    @Column(nullable=false, length=200) private String title;
    private String artist;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Category category = Category.기타;
    public enum Category { 찬양, 경배, 복음성가, CCM, 기타 }

    private String musicalKey;
    private Integer tempoBpm;

    @Column(nullable=false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}