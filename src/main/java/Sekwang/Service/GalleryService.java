package Sekwang.Service;

import Sekwang.Domain.GalleryItem;
import Sekwang.Domain.Member;
import Sekwang.Repository.GalleryRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.GalleryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository repo;
    private final MemberRepository memberRepo;

    @Transactional
    public GalleryItem create(GalleryDto.CreateReq req) {
        Member uploader = req.uploader() == null ? null :
                memberRepo.findById(req.uploader())
                        .orElseThrow(() -> new IllegalArgumentException("업로더 없음"));

        LocalDateTime now = LocalDateTime.now(); // ✅ 추가됨

        GalleryItem gi = GalleryItem.builder()
                .title(req.title())
                .category(req.category())
                .fileUrl(req.fileUrl())
                .description(req.description())
                .uploader(uploader)
                .createdAt(now)   // ✅ 추가됨
                .build();

        return repo.save(gi);
    }

    @Transactional(readOnly = true)
    public Page<GalleryItem> list(String category, int page, int size) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (category == null || category.isBlank()) return repo.findAll(pr);
        return repo.findByCategoryOrderByCreatedAtDesc(category, pr);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
