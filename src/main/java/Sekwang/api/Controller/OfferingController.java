// src/main/java/Sekwang/api/Controller/OfferingController.java
package Sekwang.api.Controller;

import Sekwang.Domain.Offering;
import Sekwang.Service.OfferingService;
import Sekwang.api.DTO.OfferingDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offerings")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService service;

    /** 헌금 등록 (JSON) */
    @PostMapping
    public OfferingDto.Res create(@RequestBody @Valid OfferingDto.CreateReq req) {
        Offering o = service.create(req);
        return OfferingService.toRes(o);
    }

    /** 사용자별 헌금 목록 */
    @GetMapping("/user/{username}")
    public List<OfferingDto.Res> userOfferings(@PathVariable String username) {
        return service.findByUsername(username)
                .stream().map(OfferingService::toRes)
                .toList();
    }

    /** 요약(이번 달/지난 달/총) */
    @GetMapping("/summary/user/{username}")
    public OfferingDto.Summary summary(@PathVariable String username) {
        return service.getSummary(username);
    }
}
