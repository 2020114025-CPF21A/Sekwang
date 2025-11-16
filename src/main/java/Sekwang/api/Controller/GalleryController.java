package Sekwang.api.Controller;

import Sekwang.Domain.GalleryItem;
import Sekwang.Service.GalleryService;
import Sekwang.Service.S3Service;
import Sekwang.api.DTO.GalleryDto;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;
    private final S3Service s3Service;

    /**
     * ✅ [1] 파일 업로드 + DB 저장 (S3에 업로드 후 S3 URL 저장)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GalleryDto.Res uploadAndCreate(
            @RequestPart("file") MultipartFile file,
            @RequestPart("title") @NotBlank String title,
            @RequestPart(value = "category", required = false) String category,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart("uploader") String uploaderUsername
    ) {
        // 1️⃣ S3 업로드 → URL 반환
        String s3Url = s3Service.uploadFile(file, "gallery");

        // 2️⃣ DB 저장 요청 생성
        GalleryDto.CreateReq req = new GalleryDto.CreateReq(
                title, category, s3Url, description, uploaderUsername
        );

        // 3️⃣ 실제 DB 저장
        GalleryItem g = galleryService.create(req);

        // 4️⃣ 반환 DTO 생성
        return new GalleryDto.Res(
                g.getId(),
                g.getTitle(),
                g.getCategory(),
                g.getFileUrl(),
                g.getDescription(),
                g.getUploader() == null ? null : g.getUploader().getUsername(),
                g.getCreatedAt().toString(),
                g.getGroupId()
        );
    }

    /**
     * ✅ [2] 파일만 업로드 (URL만 반환) — 필요 시 사용
     */
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFileOnly(@RequestPart("file") MultipartFile file) {
        return s3Service.uploadFile(file, "gallery");
    }

    /**
     * ✅ [3] 갤러리 목록 조회ß
     */
    @GetMapping
    public List<GalleryDto.Res> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return galleryService.list(category, page, size)
                .map(g -> new GalleryDto.Res(
                        g.getId(),
                        g.getTitle(),
                        g.getCategory(),
                        g.getFileUrl(),
                        g.getDescription(),
                        g.getUploader() == null ? null : g.getUploader().getUsername(),
                        g.getCreatedAt().toString(),
                        g.getGroupId()
                ))
                .getContent();
    }

    /**
     * ✅ [4] 갤러리 항목 삭제
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        galleryService.delete(id);
    }
}
