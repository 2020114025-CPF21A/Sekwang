// src/main/java/Sekwang/Service/QTService.java
package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.Domain.QT;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.QTRepository;
import Sekwang.api.DTO.QTDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QTService {

    private final QTRepository repo;
    private final MemberRepository memberRepo;

    @Transactional
    public QT create(QTDto.CreateReq req) {
        Member user = memberRepo.findById(req.username())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + req.username()));

        QT qt = QT.builder()
                .user(user)
                .qtDate(LocalDate.parse(req.qtDate()))
                .scriptureRef(req.scriptureRef())
                .meditation(req.meditation())
                .prayerTopic(req.prayerTopic())
                .shared(false)   // 기본값 (필요하면 프론트에서 공유토글 추가)
                .likes(0)
                .build();

        return repo.save(qt);
    }

    @Transactional(readOnly = true)
    public List<QT> listByUser(String username) {
        return repo.findByUser_UsernameOrderByQtDateDesc(username);
    }

    @Transactional
    public QT like(Long id) {
        QT qt = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("QT 없음: " + id));
        qt.setLikes(qt.getLikes() + 1);
        return qt;
    }
}
