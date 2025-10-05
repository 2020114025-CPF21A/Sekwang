package Sekwang.api.Controller;

import Sekwang.Domain.GalleryItem;
import Sekwang.Service.GalleryService;
import Sekwang.api.DTO.GalleryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/gallery") @RequiredArgsConstructor
public class GalleryController {
    private final GalleryService service;

    @PostMapping
    public GalleryDto.Res create(@RequestBody @Valid GalleryDto.CreateReq req){
        GalleryItem g = service.create(req);
        return new GalleryDto.Res(g.getId(), g.getTitle(), g.getCategory(), g.getFileUrl(),
                g.getDescription(), g.getUploader()==null? null : g.getUploader().getUsername(),
                g.getCreatedAt().toString());
    }

    @GetMapping
    public List<GalleryDto.Res> list(@RequestParam(required=false) String category,
                                     @RequestParam(defaultValue="0") int page,
                                     @RequestParam(defaultValue="20") int size){
        return service.list(category, page, size)
                .map(g -> new GalleryDto.Res(g.getId(), g.getTitle(), g.getCategory(), g.getFileUrl(),
                        g.getDescription(), g.getUploader()==null? null : g.getUploader().getUsername(),
                        g.getCreatedAt().toString()))
                .getContent();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ service.delete(id); }
}