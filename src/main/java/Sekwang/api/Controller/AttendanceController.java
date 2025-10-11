package Sekwang.api.Controller;

import Sekwang.Domain.Attendance;
import Sekwang.Domain.AttendanceQrSession;
import Sekwang.Service.AttendanceQrService;
import Sekwang.Service.AttendanceService;
import Sekwang.api.DTO.AttendanceCheckInReq;
import Sekwang.api.DTO.AttendanceCheckInRes;
import Sekwang.api.DTO.AttendanceCreateReq;
import Sekwang.api.DTO.AttendanceRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;
    private final AttendanceQrService qrService;

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** (관리/내부) 직접 출석 생성 – 기존 로직 유지 */
    @PostMapping
    public AttendanceRes create(@Valid @RequestBody AttendanceCreateReq req) {
        Attendance a = service.create(req);
        return new AttendanceRes(a.getId(), a.getMember().getUsername(), a.getAttendDate(), a.getStatus().name());
    }

    /** 사용자별 출석 목록 */
    @GetMapping("/user/{username}")
    public List<AttendanceRes> listByUser(@PathVariable String username) {
        return service.listByUser(username).stream()
                .map(a -> new AttendanceRes(a.getId(), a.getMember().getUsername(), a.getAttendDate(), a.getStatus().name()))
                .toList();
    }

    /** (관리자) QR 세션 생성: 현재 로그인 관리자의 username 기록, 유효시간은 서비스에서 10분 */
    @PostMapping("/qr")
    @PreAuthorize("hasRole('ADMIN')")
    public Sekwang.api.DTO.QrCreateRes createQr(@AuthenticationPrincipal UserDetails user) {
        String adminUsername = user.getUsername(); // JwtAuthFilter가 세팅해둔 인증 주체
        AttendanceQrSession s = qrService.createSession(adminUsername);
        return new Sekwang.api.DTO.QrCreateRes(s.getCode(), s.getExpiresAt().format(ISO));
    }

    /** (일반) QR 코드로 체크인: 요청 바디의 username은 무시하고, 인증 주체에서 획득 */
    @PostMapping("/check-in")
    @PreAuthorize("isAuthenticated()")
    public AttendanceCheckInRes checkIn(@AuthenticationPrincipal UserDetails user,
                                        @Valid @RequestBody AttendanceCheckInReq req) {
        try {
            String username = user.getUsername();      // 토큰 주체
            String code = req.code();                  // 바디는 code만 실제 사용
            Attendance a = qrService.checkIn(username, code);

            return new AttendanceCheckInRes(
                    true,
                    a.getAttendDate().toString(),
                    a.getStatus().name(),
                    "출석이 확인되었습니다."
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new AttendanceCheckInRes(false, null, null, e.getMessage());
        }
    }
}
