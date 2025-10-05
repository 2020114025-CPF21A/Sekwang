package Sekwang.Repository;

import Sekwang.Domain.SpeedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeedQuestionRepository extends JpaRepository<SpeedQuestion, Long> {
    List<SpeedQuestion> findBySet_SetIdOrderByIdAsc(Long setId);
}
