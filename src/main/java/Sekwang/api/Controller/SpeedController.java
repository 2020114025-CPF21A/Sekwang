package Sekwang.api.Controller;

import Sekwang.Domain.SpeedQuestion;
import Sekwang.Domain.SpeedQuizResult;
import Sekwang.Domain.SpeedQuizSet;
import Sekwang.Service.SpeedService;
import Sekwang.api.DTO.SpeedDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/speed") @RequiredArgsConstructor
public class SpeedController {
    private final SpeedService svc;

    @PostMapping("/sets")
    public SpeedDto.SetRes createSet(@RequestBody @Valid SpeedDto.SetCreateReq req){
        SpeedQuizSet s = svc.createSet(req);
        return new SpeedDto.SetRes(s.getSetId(), s.getSetName(),
                s.getCreatedBy()==null? null : s.getCreatedBy().getUsername(),
                s.getCreatedAt().toString());
    }

    @PostMapping("/questions")
    public SpeedDto.QRes addQuestion(@RequestBody @Valid SpeedDto.QCreateReq req){
        SpeedQuestion q = svc.addQuestion(req);
        return new SpeedDto.QRes(q.getId(), q.getSet().getSetId(), q.getQuestion(), q.getAccept1(), q.getAccept2(), q.getAccept3());
    }

    @GetMapping("/sets/{setId}")
    public SpeedDto.SetWithQuestions getSet(@PathVariable Long setId){
        var setRes = new SpeedDto.SetRes(setId, null, null, null);
        var qRes = svc.listQuestions(setId).stream()
                .map(q -> new SpeedDto.QRes(q.getId(), setId, q.getQuestion(), q.getAccept1(), q.getAccept2(), q.getAccept3()))
                .toList();
        return new SpeedDto.SetWithQuestions(setRes, qRes);
    }

    @PostMapping("/results")
    public SpeedDto.ResultRes submit(@RequestBody @Valid SpeedDto.SubmitReq req){
        SpeedQuizResult r = svc.submit(req);
        return new SpeedDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString());
    }

    @GetMapping("/leaderboard/{setId}")
    public List<SpeedDto.ResultRes> leaderboard(@PathVariable Long setId){
        return svc.leaderboard(setId).stream()
                .map(r -> new SpeedDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString()))
                .toList();
    }
}