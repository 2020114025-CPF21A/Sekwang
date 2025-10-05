package Sekwang.Repository;
import Sekwang.Domain.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OfferingRepository extends JpaRepository<Offering, Long> {
    @Query("select sum(o.amount) from Offering o where o.member.username=:username")
    java.math.BigDecimal sumByUser(String username);
}