package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "minecraft_player_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinecraftPlayerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType; // JOIN, LEAVE

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "session_duration_minutes")
    private Long sessionDurationMinutes; // LEAVE 이벤트 시 계산

    @Column(name = "server_address")
    private String serverAddress;

    public enum EventType {
        JOIN, LEAVE
    }
}
