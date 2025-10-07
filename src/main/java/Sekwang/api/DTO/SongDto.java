package Sekwang.api.DTO;

import jakarta.validation.constraints.NotBlank;

public class SongDto {

    // 순서: uploader, title, artist, category, musicalKey, tempoBpm, imageUrl
    public record CreateReq(
            String uploader,
            @NotBlank String title,
            String artist,
            String category,   // 찬양/경배/복음성가/CCM/기타 (Entity의 Enum과 동일해야 함)
            String musicalKey,
            Integer tempoBpm,
            String imageUrl
    ) {}

    public record Res(
            Long id,
            String uploader,
            String title,
            String artist,
            String imageUrl,
            String category,
            String musicalKey,
            Integer tempoBpm,
            String createdAt
    ) {}
}
