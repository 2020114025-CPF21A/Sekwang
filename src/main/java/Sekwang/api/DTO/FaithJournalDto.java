package Sekwang.api.DTO;

import jakarta.validation.constraints.*;

public class FaithJournalDto {
    public record CreateReq(
            @NotBlank String author,
            @NotNull Integer moodCode,
            @NotNull Integer weatherCode,
            @NotBlank String title,
            @NotBlank String content
    ) {}
    public record Res(
            Long id, String author, Integer moodCode, Integer weatherCode,
            String title, String content, Integer views, String createdAt
    ) {}
}