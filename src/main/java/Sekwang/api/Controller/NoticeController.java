package Sekwang.api.Controller;

import Sekwang.Domain.Member;
import Sekwang.Domain.Notice;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.NoticeRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
class NoticeController {
    private final NoticeRepository repo;
    private final MemberRepository memberRepo;

    @PostMapping
    public Notice create(@RequestBody @Valid CreateNoticeReq req) {
        Member author = req.author()==null? null : memberRepo.findById(req.author())
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        Notice n = Notice.builder()
                .title(req.title())
                .content(req.content())
                .isImportant(Boolean.TRUE.equals(req.isImportant()))
                .author(author)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        return repo.save(n);
    }

    @GetMapping
    public List<Notice> list() {
        return repo.findAll(Sort.by(Sort.Order.desc("isImportant"), Sort.Order.desc("createdAt")));
    }
}

record CreateNoticeReq(
        @NotBlank String title,
        @NotBlank String content,
        Boolean isImportant,
        String author
) {}