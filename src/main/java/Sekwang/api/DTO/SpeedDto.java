package Sekwang.api.DTO;


import jakarta.validation.constraints.*;
import java.util.List;

public class SpeedDto {
    public record SetCreateReq(@NotBlank String setName, String createdBy) {}
    public record SetRes(Long setId, String setName, String createdBy, String createdAt) {}

    public record QCreateReq(
            @NotNull Long setId, @NotBlank String question, String accept1, String accept2, String accept3
    ) {}
    public record QRes(Long id, Long setId, String question, String accept1, String accept2, String accept3) {}

    public record SubmitReq(@NotBlank String username, @NotNull Long setId, @NotNull Integer score) {}
    public record ResultRes(Long id, String username, Long setId, Integer score, String takenAt) {}

    public record SetWithQuestions(SetRes set, List<QRes> questions) {}
}