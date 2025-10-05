package Sekwang.Repository;

import Sekwang.Domain.McQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface McQuizResultRepository extends JpaRepository<McQuizResult, Long> {
    @Query("select r from McQuizResult r where r.set.setId=:setId order by r.score desc, r.takenAt asc")
    List<McQuizResult> leaderboard(Long setId);
}
