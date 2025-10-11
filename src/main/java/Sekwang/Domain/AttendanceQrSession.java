package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AttendanceQrSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 16, nullable = false)
    private String code;              // QR에 담길 코드(예: 8~12자리 랜덤)

    private LocalDateTime expiresAt;  // 만료시각(생성 + 60초)
    private boolean active;           // 강제 비활성화 플래그(필요시)

    private String createdBy;         // 생성 관리자 username(감사/감사로그용)
    private Integer usedCount;        // 사용 횟수 집계(옵션)
}