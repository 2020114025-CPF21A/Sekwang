package Sekwang.api.Controller;

import Sekwang.Domain.Member;
import Sekwang.Repository.MemberRepository;
import Sekwang.api.DTO.AuthDto;
import Sekwang.config.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepo;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public AuthDto.LoginRes login(@RequestBody @Valid AuthDto.LoginReq req){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        Member m = memberRepo.findById(req.username()).orElseThrow();
        String token = jwtUtil.generateToken(m.getUsername(), m.getRole().name());
        return new AuthDto.LoginRes(token, m.getUsername(), m.getRole().name(), m.getDisplayName());
    }

    // 간편 회원가입 (운영에서는 관리자만 허용 권장)
    @PostMapping("/signup")
    public AuthDto.LoginRes signup(@RequestBody @Valid AuthDto.SignupReq req){
        if (memberRepo.existsById(req.username())) {
            throw new IllegalStateException("이미 존재하는 사용자");
        }
        Member.Role role = Member.Role.MEMBER;
        if (req.role()!=null) {
            try { role = Member.Role.valueOf(req.role()); } catch (Exception ignore) {}
        }
        Member m = Member.builder()
                .username(req.username())
                .passwordHash(encoder.encode(req.password()))
                .displayName(req.displayName())
                .status(Member.Status.ACTIVE)
                .role(role)
                .build();
        memberRepo.save(m);
        String token = jwtUtil.generateToken(m.getUsername(), m.getRole().name());
        return new AuthDto.LoginRes(token, m.getUsername(), m.getRole().name(), m.getDisplayName());
    }
}