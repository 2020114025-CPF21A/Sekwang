package Sekwang.Service;

import Sekwang.Domain.SpeedQuestion;
import Sekwang.Domain.SpeedQuizResult;
import Sekwang.Domain.SpeedQuizSet;
import Sekwang.Domain.Member;
import Sekwang.Repository.SpeedQuestionRepository;
import Sekwang.Repository.SpeedQuizResultRepository;
import Sekwang.Repository.SpeedQuizSetRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.SpeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class SpeedService {
    private final SpeedQuizSetRepository setRepo;
    private final SpeedQuestionRepository qRepo;
    private final SpeedQuizResultRepository rRepo;
    private final MemberRepository memberRepo;

    // ✅ 전체 세트 목록 (랜덤 선택용)
    @Transactional(readOnly = true)
    public List<SpeedQuizSet> listAllSets() { return setRepo.findAll(); }

    // ✅ 특정 세트 조회 추가 (getSetById)
    @Transactional(readOnly = true)
    public SpeedQuizSet getSetById(Long setId) {
        return setRepo.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("세트를 찾을 수 없습니다: " + setId));
    }

    @Transactional
    public SpeedQuizSet createSet(SpeedDto.SetCreateReq req){
        Member creator = req.createdBy()==null? null :
                memberRepo.findById(req.createdBy()).orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        SpeedQuizSet s = SpeedQuizSet.builder().setName(req.setName()).createdBy(creator).build();
        return setRepo.save(s);
    }

    @Transactional
    public SpeedQuestion addQuestion(SpeedDto.QCreateReq req){
        SpeedQuizSet set = getSetById(req.setId());
        SpeedQuestion q = SpeedQuestion.builder()
                .set(set).question(req.question())
                .accept1(req.accept1()).accept2(req.accept2()).accept3(req.accept3())
                .build();
        return qRepo.save(q);
    }

    @Transactional(readOnly = true)
    public List<SpeedQuestion> listQuestions(Long setId){ return qRepo.findBySet_SetIdOrderByIdAsc(setId); }

    @Transactional
    public SpeedQuizResult submit(SpeedDto.SubmitReq req){
        Member user = memberRepo.findById(req.username()).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        SpeedQuizSet set = getSetById(req.setId());
        SpeedQuizResult r = SpeedQuizResult.builder().user(user).set(set).score(req.score()).build();
        return rRepo.save(r);
    }

    @Transactional(readOnly = true)
    public List<SpeedQuizResult> leaderboard(Long setId){ return rRepo.leaderboard(setId); }
}
