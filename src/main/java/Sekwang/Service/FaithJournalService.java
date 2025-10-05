package Sekwang.Service;

import Sekwang.api.DTO.FaithJournalDto;
import Sekwang.Domain.*;
import Sekwang.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class FaithJournalService {
    private final FaithJournalRepository repo;
    private final MemberRepository memberRepo;

    @Transactional
    public FaithJournal create(FaithJournalDto.CreateReq req){
        Member m = memberRepo.findById(req.author())
                .orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        FaithJournal fj = FaithJournal.builder()
                .author(m).moodCode(req.moodCode()).weatherCode(req.weatherCode())
                .title(req.title()).content(req.content()).build();
        return repo.save(fj);
    }

    @Transactional(readOnly = true)
    public Page<FaithJournal> listByAuthor(String username, int page, int size){
        return repo.findByAuthorUsernameOrderByCreatedAtDesc(username, PageRequest.of(page, size));
    }

    @Transactional
    public FaithJournal view(Long id){
        FaithJournal fj = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일지"));
        fj.setViews(fj.getViews()+1);
        return fj;
    }

    @Transactional
    public void delete(Long id){ repo.deleteById(id); }
}