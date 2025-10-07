// src/main/java/Sekwang/Repository/OfferingRepository.java
package Sekwang.Repository;

import Sekwang.Domain.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {

    @Query("select sum(o.amount) from Offering o where o.member.username = :username")
    BigDecimal sumByUser(String username);

    List<Offering> findByMember_UsernameOrderByOfferedAtDesc(String username);

    @Query("""
           select coalesce(sum(o.amount), 0)
           from Offering o
           where o.member.username = :username
             and o.offeredAt >= :start and o.offeredAt < :end
           """)
    BigDecimal sumByUserAndRange(String username, LocalDateTime start, LocalDateTime end);
}