package Sekwang.Repository;

import Sekwang.Domain.OxQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OxQuestionRepository extends JpaRepository<OxQuestion, Long> {
    List<OxQuestion> findBySet_SetIdOrderByIdAsc(Long setId);
}