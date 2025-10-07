// src/main/java/Sekwang/Domain/QT.java
package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class QT {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Member user;

    private LocalDate qtDate;

    private String scriptureRef;

    @Column(length = 2000)
    private String meditation;

    @Column(length = 1000)
    private String prayerTopic;

    private boolean shared = false;

    private int likes = 0;

    @CreationTimestamp
    private OffsetDateTime createdAt;
}
