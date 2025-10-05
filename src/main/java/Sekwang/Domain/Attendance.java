package Sekwang.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import jakarta.persistence.*;


@Entity
@Table(name="attendance",
        uniqueConstraints=@UniqueConstraint(name="uq_att", columnNames={"attend_date","username"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="attend_date", nullable=false)
    private java.time.LocalDate attendDate;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="username", nullable=false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status; // PRESENT/ABSENT/LATE

    @Column(nullable=false)
    private java.time.LocalDateTime checkedAt = java.time.LocalDateTime.now();

    public enum Status { PRESENT, ABSENT, LATE }
}