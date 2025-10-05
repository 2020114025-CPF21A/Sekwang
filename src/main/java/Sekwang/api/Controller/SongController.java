package Sekwang.api.Controller;

import Sekwang.Domain.Song;
import Sekwang.Service.SongService;
import Sekwang.api.DTO.SongDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/songs") @RequiredArgsConstructor
public class SongController {
    private final SongService service;

    @PostMapping
    public SongDto.Res create(@RequestBody @Valid SongDto.CreateReq req){
        Song s = service.create(req);
        return new SongDto.Res(s.getId(), s.getUploader()==null? null : s.getUploader().getUsername(),
                s.getTitle(), s.getArtist(), s.getImageUrl(), s.getCategory().name(),
                s.getMusicalKey(), s.getTempoBpm(), s.getCreatedAt().toString());
    }

    @GetMapping
    public List<SongDto.Res> list(@RequestParam(required=false) String category,
                                  @RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="20") int size){
        return service.list(category, page, size)
                .map(s -> new SongDto.Res(s.getId(), s.getUploader()==null? null : s.getUploader().getUsername(),
                        s.getTitle(), s.getArtist(), s.getImageUrl(), s.getCategory().name(),
                        s.getMusicalKey(), s.getTempoBpm(), s.getCreatedAt().toString()))
                .getContent();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }
}