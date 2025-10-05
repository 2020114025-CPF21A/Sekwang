package Sekwang.api.DTO;

import jakarta.validation.constraints.*;

public class BulletinDto {
    public record CreateReq(
            String uploader,
            @NotBlank String title,
            @NotBlank String publishDate, // yyyy-MM-dd
            @NotBlank String fileUrl
    ) {}
    public record Res(
            Integer bulletinNo, String uploader, String title, String publishDate, String fileUrl, Integer views
    ) {}
}