package Sekwang.api.Controller;

import Sekwang.Domain.Bulletin;
import Sekwang.Service.BulletinService;
import Sekwang.api.DTO.BulletinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/bulletins")
@RequiredArgsConstructor
public class BulletinController {
    private final BulletinService service;

    /**
     * ✅ 주보 업로드 (S3에 업로드 후 DB 저장)
     * - multipart/form-data 요청
     * - S3Service.uploadFile(file, "bulletins") 호출
     */
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public BulletinDto.Res upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam String uploader,
            @RequestParam String title,
            @RequestParam String publishDate
    ) throws IOException {
        Bulletin b = service.uploadToS3(file, uploader, title, publishDate);
        return new BulletinDto.Res(
                b.getBulletinNo(),
                b.getUploader() == null ? null : b.getUploader().getUsername(),
                b.getTitle(),
                b.getPublishDate().toString(),
                b.getFileUrl(),
                b.getViews()
        );
    }

    /**
     * ✅ 주보 목록 조회 (페이지네이션)
     */
    @GetMapping
    public List<BulletinDto.Res> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.list(page, size)
                .map(b -> new BulletinDto.Res(
                        b.getBulletinNo(),
                        b.getUploader() == null ? null : b.getUploader().getUsername(),
                        b.getTitle(),
                        b.getPublishDate().toString(),
                        b.getFileUrl(),
                        b.getViews()
                ))
                .getContent();
    }

    /**
     * ✅ 단일 주보 조회 (+조회수 증가)
     */
    @GetMapping("/{no}")
    public BulletinDto.Res view(@PathVariable Integer no) {
        Bulletin b = service.view(no);
        return new BulletinDto.Res(
                b.getBulletinNo(),
                b.getUploader() == null ? null : b.getUploader().getUsername(),
                b.getTitle(),
                b.getPublishDate().toString(),
                b.getFileUrl(),
                b.getViews()
        );
    }

    /**
     * ✅ 주보 삭제
     */
    @DeleteMapping("/{no}")
    public void delete(@PathVariable Integer no) {
        service.delete(no);
    }
}
