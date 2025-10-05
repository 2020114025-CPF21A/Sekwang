package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.Domain.OxQuestion;
import Sekwang.Domain.OxQuizResult;
import Sekwang.Domain.OxQuizSet;
import Sekwang.Repository.MemberRepository;
import Sekwang.Repository.OxQuestionRepository;
import Sekwang.Repository.OxQuizResultRepository;
import Sekwang.Repository.OxQuizSetRepository;
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

    @Transactional
    public OxQuizSet createSet(OxDto.SetCreateReq req){
        Member creator = req.createdBy()==null? null :
                memberRepo.findById(req.createdBy()).orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        OxQuizSet s = OxQuizSet.builder().setName(req.setName()).createdBy(creator).build();
        return setRepo.save(s);
    }

    @Transactional
    public OxQuestion addQuestion(OxDto.QCreateReq req){
        if (req.answer()!=1 && req.answer()!=2) throw new IllegalArgumentException("answer는 1(O) 또는 2(X)");
        OxQuizSet set = setRepo.findById(req.setId()).orElseThrow(() -> new IllegalArgumentException("세트 없음"));
        OxQuestion q = OxQuestion.builder().set(set).question(req.question()).answer(req.answer()).build();
        return qRepo.save(q);
    }

    @Transactional(readOnly = true)
    public List<OxQuestion> listQuestions(Long setId){ return qRepo.findBySet_SetIdOrderByIdAsc(setId); }

    @Transactional
    public OxQuizResult submit(OxDto.SubmitReq req){
        Member user = memberRepo.findById(req.username()).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        OxQuizSet set = setRepo.findById(req.setId()).orElseThrow(() -> new IllegalArgumentException("세트 없음"));
        OxQuizResult r = OxQuizResult.builder().user(user).set(set).score(req.score()).build();
        return rRepo.save(r);
    }

    @Transactional(readOnly = true)
    public List<OxQuizResult> leaderboard(Long setId){ return rRepo.leaderboard(setId); }
}