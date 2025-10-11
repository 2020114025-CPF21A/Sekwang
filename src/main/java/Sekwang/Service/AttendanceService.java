package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.api.DTO.AttendanceCreateReq;
import Sekwang.Domain.Attendance;
import Sekwang.Repository.AttendanceRepository;
import Sekwang.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepo;
    private final MemberRepository memberRepo;
    private final AttendanceRepository repo;

    @Transactional
    public Attendance create(AttendanceCreateReq req) {
        if (!memberRepo.existsById(req.username())) {
            throw new IllegalArgumentException("존재하지 않는 사용자");
        }
        if (attendanceRepo.existsByMemberUsernameAndAttendDate(req.username(), req.attendDate())) {
            throw new IllegalStateException("이미 해당 날짜 출석이 존재");
        }
        Member member = memberRepo.getReferenceById(req.username());
        Attendance entity = Attendance.builder()
                .member(member)
                .attendDate(req.attendDate())
                .status(Attendance.Status.valueOf(req.status().name()))
                .checkedAt(java.time.LocalDateTime.now())
                .build();
        return attendanceRepo.save(entity);
    }

    // ✅ 여기 추가: 사용자별 출석 목록(최신일자 순)
    @Transactional(readOnly = true)
    public List<Attendance> listByUser(String username) {
        return attendanceRepo.findByMember_UsernameOrderByAttendDateDesc(username);
        // 또는: return attendanceRepo.findRecentByUser(username);
    }

    // (선택) 특정 날짜의 전체 출석 조회
    @Transactional(readOnly = true)
    public List<Attendance> listByDate(LocalDate date) {
        return attendanceRepo.findByAttendDate(date);
    }

    @Transactional
    public Attendance createIfNotExists(String username, LocalDate attendDate, String status) {
        return repo.findByMemberUsernameAndAttendDate(username, attendDate)
                .orElseGet(() -> {
                    Member m = memberRepo.findByUsername(username)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
                    Attendance a = new Attendance();
                    a.setMember(m);
                    a.setAttendDate(attendDate);
                    a.setStatus(Attendance.Status.valueOf(status));
                    return repo.save(a);
                });
    }
}