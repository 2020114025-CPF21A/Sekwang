package Sekwang.Service;

import Sekwang.Domain.Bulletin;
import Sekwang.Domain.Member;
import Sekwang.Repository.BulletinRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.BulletinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BulletinService {

    private final BulletinRepository repo;
    private final MemberRepository memberRepo;
    private final S3Service s3Service; // ✅ 기존 S3Service 사용 (uploadFile(file, folder))

    /**
     * (기존) JSON 기반 생성
     */
    @Transactional
    public Bulletin create(BulletinDto.CreateReq req) {
        Member uploader = (req.uploader() == null) ? null :
                memberRepo.findById(req.uploader())
                        .orElseThrow(() -> new IllegalArgumentException("업로더 없음"));
        Bulletin b = Bulletin.builder()
                .uploader(uploader)
                .title(req.title())
                .publishDate(LocalDate.parse(req.publishDate()))
                .fileUrl(req.fileUrl())
                .build();
        return repo.save(b);
    }

    /**
     * ✅ S3 업로드 + DB 저장
     */
    @Transactional
    public Bulletin uploadToS3(MultipartFile file, String uploader, String title, String publishDate) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        Member uploaderEntity = (uploader == null) ? null :
                memberRepo.findById(uploader)
                        .orElseThrow(() -> new IllegalArgumentException("업로더 없음: " + uploader));

        String fileUrl = s3Service.uploadFile(file, "bulletins");

        LocalDate parsedDate = LocalDate.parse(publishDate);

        Bulletin b = Bulletin.builder()
                .uploader(uploaderEntity)
                .title(title)
                .publishDate(parsedDate)
                .createdAt(parsedDate.atStartOfDay()) // 이전 답변대로
                .fileUrl(fileUrl)
                .views(0)                              // ✅ 명시적으로 0
                .build();

        return repo.save(b);
    }

    /** 목록 (발행일 최신순) */
    @Transactional(readOnly = true)
    public Page<Bulletin> list(int page, int size) {
        return repo.findAllByOrderByPublishDateDesc(PageRequest.of(page, size));
    }

    // 조회수 증가 로직
    @Transactional
    public Bulletin view(Integer no) {
        Bulletin b = repo.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("주보 없음"));
        if (b.getViews() == null) b.setViews(0); // ✅ 방어
        b.setViews(b.getViews() + 1);
        return b;
    }

    /** 삭제 */
    @Transactional
    public void delete(Integer no) {
        repo.deleteById(no);
    }
}
