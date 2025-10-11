package Sekwang.Service;

import Sekwang.Domain.Attendance;
import Sekwang.Domain.AttendanceQrSession;
import Sekwang.Repository.AttendanceQrSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AttendanceQrService {

    private final AttendanceQrSessionRepository qrRepo;
    private final AttendanceService attendanceService; // 기존 출석 생성 로직 재사용

    private static final SecureRandom RAND = new SecureRandom();
    private static final HexFormat HEX = HexFormat.of();

    /** 관리자용 QR 세션 생성(유효 60초) */
    public AttendanceQrSession createSession(String adminUsername) {
        String code = genCode(8); // 8자리(16진 4바이트)
        LocalDateTime now = LocalDateTime.now();
        AttendanceQrSession s = AttendanceQrSession.builder()
                .code(code)
                .expiresAt(now.plusMinutes(10))
                .active(true)
                .createdBy(adminUsername)
                .usedCount(0)
                .build();
        return qrRepo.save(s);
    }

    /** QR 코드로 체크인(만료/비활성/미존재 → 실패) */
    public Attendance checkIn(String username, String code) {
        AttendanceQrSession s = qrRepo.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 코드입니다."));

        LocalDateTime now = LocalDateTime.now();
        if (!s.isActive() || s.getExpiresAt().isBefore(now)) {
            throw new IllegalStateException("코드가 만료되었거나 비활성화되었습니다.");
        }

        // 오늘 이미 출석했는지 검사 → AttendanceService 안에서 중복 방지 권장
        LocalDate today = LocalDate.now();
        Attendance a = attendanceService.createIfNotExists(username, today, "PRESENT");

        // 사용 카운트 증가(옵션)
        s.setUsedCount(s.getUsedCount() + 1);
        qrRepo.save(s);

        return a;
    }

    private String genCode(int hexLen) {
        // hexLen=8 → 4바이트 랜덤, 필요시 길이 늘리기
        byte[] buf = new byte[hexLen / 2];
        RAND.nextBytes(buf);
        return HEX.formatHex(buf).toUpperCase(); // 예: "A1B2C3D4"
    }
}
