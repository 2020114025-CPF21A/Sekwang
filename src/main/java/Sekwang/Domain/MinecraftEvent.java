package Sekwang.Domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "minecraft_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinecraftEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType; // SPAWN, DEATH, CHAT, COMMAND, etc.

    @Column(name = "player_name")
    private String playerName;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "server_address")
    private String serverAddress;
}
