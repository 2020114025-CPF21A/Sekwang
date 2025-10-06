package Sekwang.api.Controller;
import Sekwang.Domain.Notice;
import Sekwang.api.Service.NoticeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
class NoticeController {

    private final NoticeService noticeService;

    // 공지 생성
    @PostMapping
    public Notice create(@RequestBody @Valid CreateNoticeReq req) {
        return noticeService.create(
                req.title(),
                req.content(),
                req.isImportant(),
                req.authorId()   // String 타입
        );
    }

    // 공지 목록
    @GetMapping
    public List<Notice> list() {
        return noticeService.list();
    }

    // 공지 단건 조회
    @GetMapping("/{id}")
    public Notice get(@PathVariable Long id) {
        return noticeService.get(id);
    }

    // 공지 수정
    @PatchMapping("/{id}")
    public Notice update(@PathVariable Long id, @RequestBody UpdateNoticeReq req) {
        return noticeService.update(
                id,
                req.title(),
                req.content(),
                req.isImportant(),
                req.authorId()
        );
    }

    // 공지 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        noticeService.delete(id);
    }

    // ===== DTO =====
    public record CreateNoticeReq(
            @NotBlank String title,
            String content,
            Boolean isImportant,
            String authorId // 문자열 ID
    ) {}

    public record UpdateNoticeReq(
            String title,
            String content,
            Boolean isImportant,
            String authorId
    ) {}
}
