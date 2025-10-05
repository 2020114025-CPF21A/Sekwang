package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="bulletins")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class  Bulletin {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer bulletinNo;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="uploader")
    private Member uploader;

    @Column(nullable=false, length=200) private String title;
    @Column(nullable=false) private java.time.LocalDate publishDate;
    @Column(nullable=false, length=500) private String fileUrl;

    @Column(nullable=false) private Integer views = 0;

    @Column(nullable=false) private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();
}