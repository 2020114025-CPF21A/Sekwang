package Sekwang.api.DTO;

import jakarta.validation.constraints.*;

public class AuthDto {
    public record LoginReq(@NotBlank String username, @NotBlank String password) {}
    public record LoginRes(String token, String username, String role, String displayName) {}

    public record SignupReq(
            @NotBlank String username,
            @NotBlank String password,
            @NotBlank String displayName,
            String role // ADMIN/STAFF/LEADER/MEMBER (관리자만 사용 권장)
    ) {}
}