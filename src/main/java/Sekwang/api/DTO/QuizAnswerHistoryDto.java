package Sekwang.api.DTO;

import java.util.List;

public class QuizAnswerHistoryDto {

    public record AnswerReq(
            Integer questionIndex,
            String question,
            String userAnswer,
            String correctAnswer,
            Boolean correct) {
    }

    public record SaveReq(
            String username,
            String quizType,
            Long resultId,
            List<AnswerReq> answers) {
    }

    public record AnswerRes(
            Integer questionIndex,
            String question,
            String userAnswer,
            String correctAnswer,
            Boolean correct) {
    }
}
