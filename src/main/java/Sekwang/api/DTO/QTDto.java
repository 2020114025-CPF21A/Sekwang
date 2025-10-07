// src/main/java/Sekwang/api/DTO/QTDto.java
package Sekwang.api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QTDto {

    public record CreateReq(
            @NotBlank String username,
            @NotNull String qtDate,            // "YYYY-MM-DD"
            @NotBlank String scriptureRef,
            @NotBlank String meditation,
            @NotBlank String prayerTopic
    ) {}

    public record Res(
            Long id,
            String username,
            String qtDate,        // ISO date string
            String scriptureRef,
            String meditation,
            String prayerTopic,
            boolean shared,
            int likes,
            String createdAt      // ISO datetime string
    ) {}
}
