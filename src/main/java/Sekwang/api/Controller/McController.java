package Sekwang.api.Controller;

import Sekwang.Domain.McQuestion;
import Sekwang.Domain.McQuizResult;
import Sekwang.Domain.McQuizSet;
import Sekwang.Service.McService;
import Sekwang.api.DTO.McDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/mc") @RequiredArgsConstructor
public class McController {
    private final McService svc;

    @PostMapping("/sets")
    public McDto.SetRes createSet(@RequestBody @Valid McDto.SetCreateReq req){
        McQuizSet s = svc.createSet(req);
        return new McDto.SetRes(s.getSetId(), s.getSetName(),
                s.getCreatedBy()==null? null : s.getCreatedBy().getUsername(),
                s.getCreatedAt().toString());
    }

    @PostMapping("/questions")
    public McDto.QRes addQuestion(@RequestBody @Valid McDto.QCreateReq req){
        McQuestion q = svc.addQuestion(req);
        return new McDto.QRes(q.getId(), q.getSet().getSetId(), q.getQuestion(),
                q.getChoice1(), q.getChoice2(), q.getChoice3(), q.getChoice4(), q.getAnswerNo());
    }

    @GetMapping("/sets/{setId}")
    public McDto.SetWithQuestions getSet(@PathVariable Long setId){
        // 간단 세트 정보
        McQuizSet s = new McQuizSet(); s.setSetId(setId); // 셋 정보는 최소 제공 (필요시 repo로 보강)
        List<McQuestion> qs = svc.listQuestions(setId);
        var setRes = new McDto.SetRes(setId, null, null, null);
        var qRes = qs.stream().map(q -> new McDto.QRes(q.getId(), setId, q.getQuestion(),
                q.getChoice1(), q.getChoice2(), q.getChoice3(), q.getChoice4(), q.getAnswerNo())).toList();
        return new McDto.SetWithQuestions(setRes, qRes);
    }

    @PostMapping("/results")
    public McDto.ResultRes submit(@RequestBody @Valid McDto.SubmitReq req){
        McQuizResult r = svc.submit(req);
        return new McDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString());
    }

    @GetMapping("/leaderboard/{setId}")
    public List<McDto.ResultRes> leaderboard(@PathVariable Long setId){
        return svc.leaderboard(setId).stream()
                .map(r -> new McDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString()))
                .toList();
    }
}