package Sekwang.api.Controller;

import Sekwang.Domain.Attendance;
import Sekwang.api.DTO.AttendanceCreateReq;
import Sekwang.api.DTO.AttendanceRes;
import Sekwang.Service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @PostMapping
    public AttendanceRes create(@Valid @RequestBody AttendanceCreateReq req) {
        Attendance a = service.create(req);
        return new AttendanceRes(a.getId(), a.getMember().getUsername(), a.getAttendDate(), a.getStatus().name());
    }

    @GetMapping("/user/{username}")
    public List<AttendanceRes> listByUser(@PathVariable String username) {
        return service.listByUser(username).stream()
                .map(a -> new AttendanceRes(a.getId(), a.getMember().getUsername(), a.getAttendDate(), a.getStatus().name()))
                .toList();
    }
}