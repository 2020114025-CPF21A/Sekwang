package Sekwang.api.DTO;

public record AttendanceCheckInRes(
        boolean ok,
        String attendDate, // YYYY-MM-DD
        String status,     // PRESENT/LATE/...
        String message
) {}