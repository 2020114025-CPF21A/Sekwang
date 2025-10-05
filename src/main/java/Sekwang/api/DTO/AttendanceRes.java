package Sekwang.api.DTO;

import java.time.LocalDate;

public record AttendanceRes(
        Long id, String username, LocalDate attendDate, String status
) {}
