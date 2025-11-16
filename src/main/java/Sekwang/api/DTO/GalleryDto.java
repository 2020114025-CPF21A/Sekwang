package Sekwang.api.DTO;

import jakarta.validation.constraints.*;

public class GalleryDto {
    public record CreateReq(
            @NotBlank String title,
            String category,
            @NotBlank String fileUrl,
            String description,
            String uploader
    ) {}
    public record Res(
            Long id, String title, String category, String fileUrl, String description, String uploader, String createdAt, String groupId
    ) {}
}