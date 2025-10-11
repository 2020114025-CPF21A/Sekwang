package Sekwang.api.DTO;

public record QrCreateRes(
        String code,
        String expiresAt // ISO-8601 문자열
) {}