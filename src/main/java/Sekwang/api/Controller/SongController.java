package Sekwang.api.Controller;

import Sekwang.Domain.Song;
import Sekwang.Service.S3Service;
import Sekwang.Service.SongService;
import Sekwang.api.DTO.SongDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService service;
    private final S3Service s3Service; // ✅ S3 업로드용

    /**
     * (기존) JSON 바디로 직접 URL을 넘기는 방식도 유지
     */
    @PostMapping
    public SongDto.Res create(@RequestBody @Valid SongDto.CreateReq req){
        Song s = service.create(req);
        return new SongDto.Res(
                s.getId(),
                s.getUploader()==null? null : s.getUploader().getUsername(),
                s.getTitle(),
                s.getArtist(),
                s.getImageUrl(),
                s.getCategory().name(),
                s.getMusicalKey(),
                s.getTempoBpm(),
                s.getCreatedAt().toString()
        );
    }

    /**
     * (신규) S3 업로드 방식
     * - multipart/form-data 로 이미지 파일과 메타데이터를 함께 전송
     * - 프론트에서는 FormData에 image 파일과 아래 필드를 담아 POST하면 됩니다.
     *
     * 필드:
     *  - image: MultipartFile
     *  - title: String
     *  - artist: String
     *  - category: String  (예: 'CCM', '찬양' 등, Enum 매핑은 Service/Entity에 맞게)
     *  - musicalKey: String (예: 'C', 'G')
     *  - tempoBpm: Integer
     *  - uploader: String  (username)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SongDto.Res upload(
            @RequestPart("image") MultipartFile image,
            @RequestParam String title,
            @RequestParam String artist,
            @RequestParam String category,
            @RequestParam String musicalKey,
            @RequestParam Integer tempoBpm,
            @RequestParam String uploader
    ) {
        // 1) S3 업로드
        String imageUrl = s3Service.uploadFile(image, "song");

        // 2) Service에 저장 위임 (기존 CreateReq를 재사용)ß
        SongDto.CreateReq req = new SongDto.CreateReq(
                uploader, title, artist, category, musicalKey, tempoBpm, imageUrl
        );
        Song s = service.create(req);

        // 3) 응답 DTO로 반환
        return new SongDto.Res(
                s.getId(),
                s.getUploader()==null? null : s.getUploader().getUsername(),
                s.getTitle(),
                s.getArtist(),
                s.getImageUrl(),
                s.getCategory().name(),
                s.getMusicalKey(),
                s.getTempoBpm(),
                s.getCreatedAt().toString()
        );
    }

    @GetMapping
    public List<SongDto.Res> list(@RequestParam(required=false) String category,
                                  @RequestParam(defaultValue="0") int page,
                                  @RequestParam(defaultValue="20") int size){
        return service.list(category, page, size)
                .map(s -> new SongDto.Res(
                        s.getId(),
                        s.getUploader()==null? null : s.getUploader().getUsername(),
                        s.getTitle(),
                        s.getArtist(),
                        s.getImageUrl(),
                        s.getCategory().name(),
                        s.getMusicalKey(),
                        s.getTempoBpm(),
                        s.getCreatedAt().toString()
                ))
                .getContent();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
