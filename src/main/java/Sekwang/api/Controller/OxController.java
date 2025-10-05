package Sekwang.api.Controller;

import Sekwang.Domain.OxQuestion;
import Sekwang.Domain.OxQuizResult;
import Sekwang.Domain.OxQuizSet;
import Sekwang.Service.OxService;
import Sekwang.api.DTO.OxDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/ox") @RequiredArgsConstructor
public class OxController {
    private final OxService svc;

    @PostMapping("/sets")
    public OxDto.SetRes createSet(@RequestBody @Valid OxDto.SetCreateReq req){
        OxQuizSet s = svc.createSet(req);
        return new OxDto.SetRes(s.getSetId(), s.getSetName(),
                s.getCreatedBy()==null? null : s.getCreatedBy().getUsername(),
                s.getCreatedAt().toString());
    }

    @PostMapping("/questions")
    public OxDto.QRes addQuestion(@RequestBody @Valid OxDto.QCreateReq req){
        OxQuestion q = svc.addQuestion(req);
        return new OxDto.QRes(q.getId(), q.getSet().getSetId(), q.getQuestion(), q.getAnswer());
    }

    @GetMapping("/sets/{setId}")
    public OxDto.SetWithQuestions getSet(@PathVariable Long setId){
        var setRes = new OxDto.SetRes(setId, null, null, null);
        var qRes = svc.listQuestions(setId).stream()
                .map(q -> new OxDto.QRes(q.getId(), setId, q.getQuestion(), q.getAnswer()))
                .toList();
        return new OxDto.SetWithQuestions(setRes, qRes);
    }

    @PostMapping("/results")
    public OxDto.ResultRes submit(@RequestBody @Valid OxDto.SubmitReq req){
        OxQuizResult r = svc.submit(req);
        return new OxDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString());
    }

    @GetMapping("/leaderboard/{setId}")
    public List<OxDto.ResultRes> leaderboard(@PathVariable Long setId){
        return svc.leaderboard(setId).stream()
                .map(r -> new OxDto.ResultRes(r.getId(), r.getUser().getUsername(), r.getSet().getSetId(), r.getScore(), r.getTakenAt().toString()))
                .toList();
    }
}