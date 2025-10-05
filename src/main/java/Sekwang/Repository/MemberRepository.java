package Sekwang.Repository;
import Sekwang.Domain.Member;
import org.springframework.data.jpa.repository.*;

public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByUsername(String username);
}