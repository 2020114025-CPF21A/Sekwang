package Sekwang.Repository;

import Sekwang.Domain.MinecraftPlayerLog;
import Sekwang.Domain.MinecraftPlayerLog.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MinecraftPlayerLogRepository extends JpaRepository<MinecraftPlayerLog, Long> {

    // 최근 로그 조회 (최신순)
    List<MinecraftPlayerLog> findTop50ByOrderByEventTimeDesc();

    // 특정 플레이어의 로그 조회
    List<MinecraftPlayerLog> findByPlayerNameOrderByEventTimeDesc(String playerName);

    // 특정 기간의 로그 조회
    List<MinecraftPlayerLog> findByEventTimeBetweenOrderByEventTimeDesc(
            LocalDateTime start, LocalDateTime end);

    // 플레이어의 마지막 JOIN 이벤트 조회 (LEAVE 시 세션 시간 계산용)
    Optional<MinecraftPlayerLog> findTopByPlayerNameAndEventTypeOrderByEventTimeDesc(
            String playerName, EventType eventType);

    // 오늘의 고유 접속자 수
    @Query("SELECT COUNT(DISTINCT l.playerName) FROM MinecraftPlayerLog l " +
            "WHERE l.eventType = 'JOIN' AND l.eventTime >= :startOfDay")
    Long countUniquePlayersToday(@Param("startOfDay") LocalDateTime startOfDay);

    // 특정 플레이어의 총 플레이 시간 (분)
    @Query("SELECT COALESCE(SUM(l.sessionDurationMinutes), 0) FROM MinecraftPlayerLog l " +
            "WHERE l.playerName = :playerName AND l.eventType = 'LEAVE'")
    Long getTotalPlayTimeMinutes(@Param("playerName") String playerName);

    // 플레이 시간 랭킹 (상위 10명)
    @Query("SELECT l.playerName, SUM(l.sessionDurationMinutes) as totalTime " +
            "FROM MinecraftPlayerLog l WHERE l.eventType = 'LEAVE' " +
            "GROUP BY l.playerName ORDER BY totalTime DESC LIMIT 10")
    List<Object[]> getPlayTimeRanking();
}
