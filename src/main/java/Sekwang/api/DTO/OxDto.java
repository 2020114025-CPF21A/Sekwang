package Sekwang.api.DTO;

import jakarta.validation.constraints.*;
import java.util.List;

public class OxDto {
    public record SetCreateReq(@NotBlank String setName, String createdBy) {}
    public record SetRes(Long setId, String setName, String createdBy, String createdAt) {}

    public record QCreateReq(@NotNull Long setId, @NotBlank String question, @NotNull Integer answer) {}
    public record QRes(Long id, Long setId, String question, Integer answer) {}

    public record SubmitReq(@NotBlank String username, @NotNull Long setId, @NotNull Integer score) {}
    public record ResultRes(Long id, String username, Long setId, Integer score, String takenAt) {}

    public record SetWithQuestions(SetRes set, List<QRes> questions) {}
}