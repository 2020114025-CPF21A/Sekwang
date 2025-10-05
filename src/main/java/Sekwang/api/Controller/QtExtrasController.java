package Sekwang.api.Controller;

import Sekwang.Domain.QtEntry;
import Sekwang.Repository.QtEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/qt") @RequiredArgsConstructor
public class QtExtrasController {
    private final QtEntryRepository repo;

    @PatchMapping("/{id}/like")
    @Transactional
    public int like(@PathVariable Long id){
        QtEntry e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("QT 없음"));
        e.setLikes(e.getLikes()+1);
        return e.getLikes();
    }
}