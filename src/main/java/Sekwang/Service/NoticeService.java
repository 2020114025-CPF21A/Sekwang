package Sekwang.api.Service;

import Sekwang.Domain.Member;
import Sekwang.Domain.Notice;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    // 목록 조회
    public List<Notice> list() {
        Sort sort = Sort.by(
                Sort.Order.desc("isImportant"),
                Sort.Order.desc("createdAt")
        );
        return noticeRepository.findAll(sort);
    }

    // 단건 조회
    public Notice get(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. id=" + id));
    }

    // 생성
    @Transactional
    public Notice create(String title, String content, Boolean isImportant, String authorId) {
        Member author = (authorId == null)
                ? null
                : memberRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자(Member) 없음: id=" + authorId));

        LocalDateTime now = LocalDateTime.now();

        Notice n = Notice.builder()
                .title(title)
                .content(content)
                .isImportant(Boolean.TRUE.equals(isImportant))
                .author(author)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return noticeRepository.save(n);
    }

    // 수정
    @Transactional
    public Notice update(Long id, String title, String content, Boolean isImportant, String authorId) {
        Notice n = get(id);

        if (title != null) n.setTitle(title);
        if (content != null) n.setContent(content);
        if (isImportant != null) n.setIsImportant(isImportant);

        if (authorId != null) {
            Member author = memberRepository.findById(authorId)
                    .orElseThrow(() -> new IllegalArgumentException("작성자(Member) 없음: id=" + authorId));
            n.setAuthor(author);
        }

        n.setUpdatedAt(LocalDateTime.now());
        return n; // dirty checking 자동 반영
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        Notice n = get(id);
        noticeRepository.delete(n);
    }
}
