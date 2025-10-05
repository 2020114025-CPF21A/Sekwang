package Sekwang.Service;

import Sekwang.Domain.Bulletin;
import Sekwang.Domain.Member;
import Sekwang.Repository.BulletinRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.BulletinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class BulletinService {
    private final BulletinRepository repo;
    private final MemberRepository memberRepo;

    @Transactional
    public Bulletin create(BulletinDto.CreateReq req){
        Member uploader = req.uploader()==null? null :
                memberRepo.findById(req.uploader()).orElseThrow(() -> new IllegalArgumentException("업로더 없음"));
        Bulletin b = Bulletin.builder()
                .uploader(uploader)
                .title(req.title())
                .publishDate(java.time.LocalDate.parse(req.publishDate()))
                .fileUrl(req.fileUrl())
                .build();
        return repo.save(b);
    }

    @Transactional(readOnly = true)
    public Page<Bulletin> list(int page, int size){
        return repo.findAllByOrderByPublishDateDesc(PageRequest.of(page, size));
    }

    @Transactional
    public Bulletin view(Integer no){
        Bulletin b = repo.findById(no).orElseThrow(() -> new IllegalArgumentException("주보 없음"));
        b.setViews(b.getViews()+1);
        return b;
    }

    @Transactional public void delete(Integer no){ repo.deleteById(no); }
}