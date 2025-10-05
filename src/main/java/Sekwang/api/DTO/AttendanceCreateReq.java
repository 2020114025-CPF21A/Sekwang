package Sekwang.api.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AttendanceCreateReq(
        @NotBlank String username,
        @NotNull LocalDate attendDate,
        @NotNull AttendanceStatus status
) {
    public enum AttendanceStatus { PRESENT, ABSENT, LATE }
}

