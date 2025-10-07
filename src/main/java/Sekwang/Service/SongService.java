package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.Domain.Song;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.SongRepository;
import Sekwang.api.DTO.SongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository repo;
    private final MemberRepository memberRepo;

    @Transactional
    public Song create(SongDto.CreateReq req) {
        // 업로더(선택)
        Member uploader = (req.uploader() == null || req.uploader().isBlank())
                ? null
                : memberRepo.findById(req.uploader())
                .orElseThrow(() -> new IllegalArgumentException("업로더 없음"));

        // 카테고리 매핑 (없으면 기본: 기타)
        Song.Category cat;
        try {
            cat = (req.category() == null || req.category().isBlank())
                    ? Song.Category.기타
                    : Song.Category.valueOf(req.category());
        } catch (Exception e) {
            throw new IllegalArgumentException("category 값이 올바르지 않습니다.");
        }

        Song s = Song.builder()
                .uploader(uploader)
                .title(req.title())
                .artist(req.artist())
                .imageUrl(req.imageUrl())     // ✅ 컨트롤러에서 S3 업로드 후 전달됨
                .category(cat)
                .musicalKey(req.musicalKey())
                .tempoBpm(req.tempoBpm())
                .build();

        return repo.save(s);
    }

    @Transactional(readOnly = true)
    public Page<Song> list(String category, int page, int size) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (category == null || category.isBlank()) {
            return repo.findAll(pr);
        }

        Song.Category cat;
        try {
            cat = Song.Category.valueOf(category);
        } catch (Exception e) {
            throw new IllegalArgumentException("category 값이 올바르지 않습니다.");
        }

        return repo.findByCategoryOrderByCreatedAtDesc(cat, pr);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
