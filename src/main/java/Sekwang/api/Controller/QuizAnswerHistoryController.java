package Sekwang.api.Controller;

import Sekwang.Domain.QuizAnswerHistory;
import Sekwang.Service.QuizAnswerHistoryService;
import Sekwang.api.DTO.QuizAnswerHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-history")
@RequiredArgsConstructor
public class QuizAnswerHistoryController {

    private final QuizAnswerHistoryService svc;

    // 답변 기록 저장
    @PostMapping
    public void saveHistory(@RequestBody QuizAnswerHistoryDto.SaveReq req) {
        List<QuizAnswerHistory> histories = req.answers().stream()
                .map(a -> QuizAnswerHistory.builder()
                        .username(req.username())
                        .quizType(req.quizType())
                        .resultId(req.resultId())
                        .questionIndex(a.questionIndex())
                        .question(a.question())
                        .userAnswer(a.userAnswer())
                        .correctAnswer(a.correctAnswer())
                        .correct(a.correct())
                        .build())
                .toList();
        svc.saveAll(histories);
    }

    // 특정 결과의 상세 기록 조회
    @GetMapping("/{quizType}/{resultId}")
    public List<QuizAnswerHistoryDto.AnswerRes> getByResult(
            @PathVariable String quizType,
            @PathVariable Long resultId) {
        return svc.getByResult(quizType, resultId).stream()
                .map(h -> new QuizAnswerHistoryDto.AnswerRes(
                        h.getQuestionIndex(),
                        h.getQuestion(),
                        h.getUserAnswer(),
                        h.getCorrectAnswer(),
                        h.getCorrect()))
                .toList();
    }
}
