package Sekwang.api.DTO;

import jakarta.validation.constraints.NotBlank;

public record AttendanceCheckInReq(
        @NotBlank String code
) {}