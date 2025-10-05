package Sekwang.api.DTO;
import jakarta.validation.constraints.*;


public class SongDto {
    public record CreateReq(
            String uploader,
            @NotBlank String title,
            String artist,
            String imageUrl,
            String category,   // 찬양/경배/복음성가/CCM/기타
            String musicalKey,
            Integer tempoBpm
    ) {}
    public record Res(
            Long id, String uploader, String title, String artist, String imageUrl,
            String category, String musicalKey, Integer tempoBpm, String createdAt
    ) {}
}