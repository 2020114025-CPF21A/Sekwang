package Sekwang.Repository;

import Sekwang.Domain.MinecraftEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MinecraftEventRepository extends JpaRepository<MinecraftEvent, Long> {

    // 최근 이벤트 조회
    List<MinecraftEvent> findAllByOrderByEventTimeDesc(Pageable pageable);

    // 특정 타입의 이벤트 조회
    List<MinecraftEvent> findByEventTypeOrderByEventTimeDesc(String eventType, Pageable pageable);

    // 특정 플레이어의 이벤트 조회
    List<MinecraftEvent> findByPlayerNameOrderByEventTimeDesc(String playerName, Pageable pageable);

    // 오늘 이벤트 조회
    @Query("SELECT e FROM MinecraftEvent e WHERE DATE(e.eventTime) = CURRENT_DATE ORDER BY e.eventTime DESC")
    List<MinecraftEvent> findTodayEvents(Pageable pageable);

    // 특정 시간 이후의 이벤트 (실시간 업데이트용)
    List<MinecraftEvent> findByEventTimeAfterOrderByEventTimeDesc(LocalDateTime after);
}
