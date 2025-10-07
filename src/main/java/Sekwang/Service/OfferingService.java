// src/main/java/Sekwang/Service/OfferingService.java
package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.Domain.Offering;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.OfferingRepository;
import Sekwang.api.DTO.OfferingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfferingService {

    private final OfferingRepository repo;
    private final MemberRepository memberRepo;

    /** 등록 */
    @Transactional
    public Offering create(OfferingDto.CreateReq req) {
        Member m = memberRepo.findById(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("회원(작성자) 없음: " + req.getUsername()));

        Offering o = Offering.builder()
                .member(m)
                .amount(req.getAmount())
                .note(req.getNote())
                // 프론트에서 offeredAt을 안 주면 엔티티 기본값(now) 사용
                .build();

        // created/offeredAt이 null이 되는 경우를 강제로 방지
        if (o.getOfferedAt() == null) {
            o.setOfferedAt(LocalDateTime.now());
        }
        return repo.save(o);
    }

    /** 사용자별 목록 */
    public List<Offering> findByUsername(String username) {
        return repo.findByMember_UsernameOrderByOfferedAtDesc(username);
    }

    /** 요약(이번 달/지난 달/총) */
    public OfferingDto.Summary getSummary(String username) {
        // 이번 달 범위
        YearMonth ymNow = YearMonth.now();
        LocalDateTime thisStart = ymNow.atDay(1).atStartOfDay();
        LocalDateTime thisEnd   = ymNow.plusMonths(1).atDay(1).atStartOfDay();

        // 지난 달 범위
        YearMonth ymPrev = ymNow.minusMonths(1);
        LocalDateTime prevStart = ymPrev.atDay(1).atStartOfDay();
        LocalDateTime prevEnd   = ymNow.atDay(1).atStartOfDay();

        BigDecimal thisMonth = repo.sumByUserAndRange(username, thisStart, thisEnd);
        BigDecimal lastMonth = repo.sumByUserAndRange(username, prevStart, prevEnd);
        BigDecimal total     = repo.sumByUser(username);

        // null 방지
        if (thisMonth == null) thisMonth = BigDecimal.ZERO;
        if (lastMonth == null) lastMonth = BigDecimal.ZERO;
        if (total == null) total = BigDecimal.ZERO;

        return new OfferingDto.Summary(thisMonth, lastMonth, total);
    }

    /** DTO 변환 도우미 */
    public static OfferingDto.Res toRes(Offering o) {
        return new OfferingDto.Res(
                o.getId(),
                o.getMember() == null ? null : o.getMember().getUsername(),
                o.getAmount(),
                o.getNote(),
                o.getOfferedAt()
        );
    }
}
