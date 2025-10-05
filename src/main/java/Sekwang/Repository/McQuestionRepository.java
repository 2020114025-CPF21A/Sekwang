package Sekwang.Repository;

import Sekwang.Domain.McQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface McQuestionRepository extends JpaRepository<McQuestion, Long> {
    List<McQuestion> findBySet_SetIdOrderByIdAsc(Long setId);
}
