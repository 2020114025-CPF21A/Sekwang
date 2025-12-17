package Sekwang.Repository;

import Sekwang.Domain.QuizAnswerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizAnswerHistoryRepository extends JpaRepository<QuizAnswerHistory, Long> {

    // 특정 결과의 상세 기록 조회
    @Query("select h from QuizAnswerHistory h where h.quizType=:quizType and h.resultId=:resultId order by h.questionIndex asc")
    List<QuizAnswerHistory> findByQuizTypeAndResultId(String quizType, Long resultId);

    // 사용자별 기록 조회
    @Query("select h from QuizAnswerHistory h where h.username=:username order by h.createdAt desc")
    List<QuizAnswerHistory> findByUsername(String username);
}
