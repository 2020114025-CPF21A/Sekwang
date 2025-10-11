package Sekwang.Repository;
import Sekwang.Domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByMemberUsernameAndAttendDate(String username, LocalDate attendDate);

    // 최근순 조회 (중첩 필드 접근: member.username)
    List<Attendance> findByMember_UsernameOrderByAttendDateDesc(String username);

    // 날짜별 조회가 필요하면 옵션으로 사용
    List<Attendance> findByAttendDate(LocalDate attendDate);

    @Query("select a from Attendance a where a.member.username=:username order by a.attendDate desc")
    List<Attendance> findRecentByUser(String username);

    Optional<Attendance> findByMemberUsernameAndAttendDate(String username, LocalDate attendDate);
}