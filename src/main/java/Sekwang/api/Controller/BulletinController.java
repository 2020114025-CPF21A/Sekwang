package Sekwang.api.Controller;

import Sekwang.Domain.Bulletin;
import Sekwang.Service.BulletinService;
import Sekwang.api.DTO.BulletinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/bulletins") @RequiredArgsConstructor
public class BulletinController {
    private final BulletinService service;

    @PostMapping
    public BulletinDto.Res create(@RequestBody @Valid BulletinDto.CreateReq req){
        Bulletin b = service.create(req);
        return new BulletinDto.Res(b.getBulletinNo(),
                b.getUploader()==null? null : b.getUploader().getUsername(),
                b.getTitle(), b.getPublishDate().toString(), b.getFileUrl(), b.getViews());
    }

    @GetMapping
    public List<BulletinDto.Res> list(@RequestParam(defaultValue="0") int page,
                                      @RequestParam(defaultValue="20") int size){
        return service.list(page, size)
                .map(b -> new BulletinDto.Res(b.getBulletinNo(),
                        b.getUploader()==null? null : b.getUploader().getUsername(),
                        b.getTitle(), b.getPublishDate().toString(), b.getFileUrl(), b.getViews()))
                .getContent();
    }

    @GetMapping("/{no}")
    public BulletinDto.Res view(@PathVariable Integer no){
        Bulletin b = service.view(no);
        return new BulletinDto.Res(b.getBulletinNo(),
                b.getUploader()==null? null : b.getUploader().getUsername(),
                b.getTitle(), b.getPublishDate().toString(), b.getFileUrl(), b.getViews());
    }

    @DeleteMapping("/{no}")
    public void delete(@PathVariable Integer no){ service.delete(no); }
}