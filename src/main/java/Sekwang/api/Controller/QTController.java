// src/main/java/Sekwang/api/Controller/QTController.java
package Sekwang.api.Controller;

import Sekwang.Domain.QT;
import Sekwang.Service.QTService;
import Sekwang.api.DTO.QTDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qt")
@RequiredArgsConstructor
public class QTController {

    private final QTService service;

    @PostMapping
    public QTDto.Res create(@RequestBody @Valid QTDto.CreateReq req) {
        QT qt = service.create(req);
        return toRes(qt);
    }

    @GetMapping("/user/{username}")
    public List<QTDto.Res> listByUser(@PathVariable String username) {
        return service.listByUser(username).stream().map(this::toRes).toList();
    }

    @PatchMapping("/{id}/like")
    public QTDto.Res like(@PathVariable Long id) {
        QT qt = service.like(id);
        return toRes(qt);
    }

    private QTDto.Res toRes(QT qt) {
        return new QTDto.Res(
                qt.getId(),
                qt.getUser() == null ? null : qt.getUser().getUsername(),
                qt.getQtDate() == null ? null : qt.getQtDate().toString(),
                qt.getScriptureRef(),
                qt.getMeditation(),
                qt.getPrayerTopic(),
                qt.isShared(),
                qt.getLikes(),
                qt.getCreatedAt() == null ? null : qt.getCreatedAt().toString()
        );
    }
}
