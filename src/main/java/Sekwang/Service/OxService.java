package Sekwang.Service;

import Sekwang.Domain.OxQuestion;
import Sekwang.Domain.OxQuizResult;
import Sekwang.Domain.OxQuizSet;
import Sekwang.Domain.Member;
import Sekwang.Repository.OxQuestionRepository;
import Sekwang.Repository.OxQuizResultRepository;
import Sekwang.Repository.OxQuizSetRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.OxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class OxService {
    private final OxQuizSetRepository setRepo;
    private final OxQuestionRepository qRepo;
    private final OxQuizResultRepository rRepo;
    private final MemberRepository memberRepo;

    // ✅ 전체 세트 목록 (랜덤 선택용)
    @Transactional(readOnly = true)
    public List<OxQuizSet> listAllSets() { return setRepo.findAll(); }

    // ✅ 특정 세트 조회 추가 (getSetById)
    @Transactional(readOnly = true)
    public OxQuizSet getSetById(Long setId) {
        return setRepo.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("세트를 찾을 수 없습니다: " + setId));
    }

    @Transactional
    public OxQuizSet createSet(OxDto.SetCreateReq req){
        Member creator = req.createdBy()==null? null :
                memberRepo.findById(req.createdBy()).orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        OxQuizSet s = OxQuizSet.builder().setName(req.setName()).createdBy(creator).build();
        return setRepo.save(s);
    }

    @Transactional
    public OxQuestion addQuestion(OxDto.QCreateReq req){
        OxQuizSet set = getSetById(req.setId());
        OxQuestion q = OxQuestion.builder()
                .set(set).question(req.question())
                .answer(req.answer()).build();
        return qRepo.save(q);
    }

    @Transactional(readOnly = true)
    public List<OxQuestion> listQuestions(Long setId){ return qRepo.findBySet_SetIdOrderByIdAsc(setId); }

    @Transactional
    public OxQuizResult submit(OxDto.SubmitReq req){
        Member user = memberRepo.findById(req.username()).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        OxQuizSet set = getSetById(req.setId());
        OxQuizResult r = OxQuizResult.builder().user(user).set(set).score(req.score()).build();
        return rRepo.save(r);
    }

    @Transactional(readOnly = true)
    public List<OxQuizResult> leaderboard(Long setId){ return rRepo.leaderboard(setId); }
}
