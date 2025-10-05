package Sekwang.api.Controller;

import Sekwang.Domain.FaithJournal;
import Sekwang.Service.FaithJournalService;
import Sekwang.api.DTO.FaithJournalDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/faith-journals") @RequiredArgsConstructor
public class FaithJournalController {
    private final FaithJournalService service;

    @PostMapping
    public FaithJournalDto.Res create(@RequestBody @Valid FaithJournalDto.CreateReq req){
        FaithJournal fj = service.create(req);
        return new FaithJournalDto.Res(fj.getId(), fj.getAuthor().getUsername(), fj.getMoodCode(),
                fj.getWeatherCode(), fj.getTitle(), fj.getContent(), fj.getViews(),
                fj.getCreatedAt().toString());
    }

    @GetMapping("/user/{username}")
    public List<FaithJournalDto.Res> list(@PathVariable String username,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size){
        return service.listByAuthor(username, page, size)
                .map(fj -> new FaithJournalDto.Res(fj.getId(), fj.getAuthor().getUsername(), fj.getMoodCode(),
                        fj.getWeatherCode(), fj.getTitle(), fj.getContent(), fj.getViews(),
                        fj.getCreatedAt().toString()))
                .getContent();
    }

    @GetMapping("/{id}")
    public FaithJournalDto.Res view(@PathVariable Long id){
        FaithJournal fj = service.view(id);
        return new FaithJournalDto.Res(fj.getId(), fj.getAuthor().getUsername(), fj.getMoodCode(),
                fj.getWeatherCode(), fj.getTitle(), fj.getContent(), fj.getViews(),
                fj.getCreatedAt().toString());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }
}