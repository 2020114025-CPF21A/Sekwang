package Sekwang.Service;

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

    // 생성: author는 username 문자열로 저장
    @Transactional
    public Notice create(String title, String content, Boolean isImportant, String authorId) {
        if (authorId == null || authorId.isBlank()) {
            throw new IllegalArgumentException("작성자(username)가 필요합니다.");
        }

        // 작성자 존재 검증만 수행 (문자열만 저장)
        memberRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("작성자(Member) 없음: id=" + authorId));

        LocalDateTime now = LocalDateTime.now();

        Notice n = Notice.builder()
                .title(title)
                .content(content)
                .isImportant(Boolean.TRUE.equals(isImportant))
                .author(authorId)            // ✅ 문자열 username 저장
                .createdAt(now)
                .updatedAt(now)
                .build();

        return noticeRepository.save(n);
    }

    // 수정: 넘어온 값만 반영, author는 username 문자열로 교체
    @Transactional
    public Notice update(Long id, String title, String content, Boolean isImportant, String authorId) {
        Notice n = get(id);

        if (title != null) n.setTitle(title);
        if (content != null) n.setContent(content);
        if (isImportant != null) n.setIsImportant(isImportant);

        if (authorId != null) {
            // 존재 검증만 하고 문자열로 셋팅
            memberRepository.findById(authorId)
                    .orElseThrow(() -> new IllegalArgumentException("작성자(Member) 없음: id=" + authorId));
            n.setAuthor(authorId);          // ✅ 문자열 username 저장
        }

        n.setUpdatedAt(LocalDateTime.now());
        return n; // Dirty checking으로 반영
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        Notice n = get(id);
        noticeRepository.delete(n);
    }
}
