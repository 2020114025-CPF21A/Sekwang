package Sekwang.Repository;

import Sekwang.Domain.AttendanceQrSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceQrSessionRepository extends JpaRepository<AttendanceQrSession, Long> {
    Optional<AttendanceQrSession> findByCode(String code);
}