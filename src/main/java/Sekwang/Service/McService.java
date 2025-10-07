package Sekwang.Service;

import Sekwang.Domain.McQuestion;
import Sekwang.Domain.McQuizResult;
import Sekwang.Domain.McQuizSet;
import Sekwang.Domain.Member;
import Sekwang.Repository.McQuestionRepository;
import Sekwang.Repository.McQuizResultRepository;
import Sekwang.Repository.McQuizSetRepository;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.McDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class McService {
    private final McQuizSetRepository setRepo;
    private final McQuestionRepository qRepo;
    private final McQuizResultRepository rRepo;
    private final MemberRepository memberRepo;

    // ✅ 전체 세트 목록 (랜덤 선택용)
    @Transactional(readOnly = true)
    public List<McQuizSet> listAllSets() { return setRepo.findAll(); }

    // ✅ 특정 세트 조회 추가 (getSetById)
    @Transactional(readOnly = true)
    public McQuizSet getSetById(Long setId) {
        return setRepo.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("세트를 찾을 수 없습니다: " + setId));
    }

    @Transactional
    public McQuizSet createSet(McDto.SetCreateReq req){
        Member creator = req.createdBy()==null? null :
                memberRepo.findById(req.createdBy()).orElseThrow(() -> new IllegalArgumentException("작성자 없음"));
        McQuizSet s = McQuizSet.builder().setName(req.setName()).createdBy(creator).build();
        return setRepo.save(s);
    }

    @Transactional
    public McQuestion addQuestion(McDto.QCreateReq req){
        McQuizSet set = getSetById(req.setId());
        if (req.answerNo()<1 || req.answerNo()>4) throw new IllegalArgumentException("answerNo 1~4");
        McQuestion q = McQuestion.builder()
                .set(set).question(req.question())
                .choice1(req.choice1()).choice2(req.choice2())
                .choice3(req.choice3()).choice4(req.choice4())
                .answerNo(req.answerNo()).build();
        return qRepo.save(q);
    }

    @Transactional(readOnly = true)
    public List<McQuestion> listQuestions(Long setId){ return qRepo.findBySet_SetIdOrderByIdAsc(setId); }

    @Transactional
    public McQuizResult submit(McDto.SubmitReq req){
        Member user = memberRepo.findById(req.username()).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        McQuizSet set = getSetById(req.setId());
        McQuizResult r = McQuizResult.builder().user(user).set(set).score(req.score()).build();
        return rRepo.save(r);
    }

    @Transactional(readOnly = true)
    public List<McQuizResult> leaderboard(Long setId){ return rRepo.leaderboard(setId); }
}
