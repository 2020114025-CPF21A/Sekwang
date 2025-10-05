package Sekwang.api.DTO;

import jakarta.validation.constraints.*;
import java.util.List;

public class McDto {
    public record SetCreateReq(@NotBlank String setName, String createdBy) {}
    public record SetRes(Long setId, String setName, String createdBy, String createdAt) {}

    public record QCreateReq(
            @NotNull Long setId, @NotBlank String question,
            @NotBlank String choice1, @NotBlank String choice2,
            @NotBlank String choice3, @NotBlank String choice4,
            @NotNull Integer answerNo
    ) {}
    public record QRes(Long id, Long setId, String question,
                       String choice1, String choice2, String choice3, String choice4, Integer answerNo) {}

    public record SubmitReq(@NotBlank String username, @NotNull Long setId, @NotNull Integer score) {}
    public record ResultRes(Long id, String username, Long setId, Integer score, String takenAt) {}

    public record SetWithQuestions(SetRes set, List<QRes> questions) {}
}