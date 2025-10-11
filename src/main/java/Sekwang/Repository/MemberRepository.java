package Sekwang.Repository;
import Sekwang.Domain.Member;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByUsername(String username);
    Optional<Member> findByUsername(String username);  // ← 이 줄 추가
}