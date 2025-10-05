package Sekwang.Repository;

import Sekwang.Domain.SpeedQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpeedQuizResultRepository extends JpaRepository<SpeedQuizResult, Long> {
    @Query("select r from SpeedQuizResult r where r.set.setId=:setId order by r.score desc, r.takenAt asc")
    List<SpeedQuizResult> leaderboard(Long setId);
}
