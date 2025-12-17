package Sekwang.Service;

import Sekwang.Domain.QuizAnswerHistory;
import Sekwang.Repository.QuizAnswerHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizAnswerHistoryService {

    private final QuizAnswerHistoryRepository repo;

    @Transactional
    public void saveAll(List<QuizAnswerHistory> histories) {
        repo.saveAll(histories);
    }

    @Transactional(readOnly = true)
    public List<QuizAnswerHistory> getByResult(String quizType, Long resultId) {
        return repo.findByQuizTypeAndResultId(quizType, resultId);
    }

    @Transactional(readOnly = true)
    public List<QuizAnswerHistory> getByUsername(String username) {
        return repo.findByUsername(username);
    }
}
