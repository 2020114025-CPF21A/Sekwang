package Sekwang.Repository;

import Sekwang.Domain.OxQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OxQuizResultRepository extends JpaRepository<OxQuizResult, Long> {
    @Query("select r from OxQuizResult r where r.set.setId=:setId order by r.score desc, r.takenAt asc")
    List<OxQuizResult> leaderboard(Long setId);
}
