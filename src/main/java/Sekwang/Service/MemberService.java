package Sekwang.Service;

import Sekwang.Domain.Member;
import Sekwang.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class MemberService {
    private final MemberRepository memberRepo;
    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder =
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @Transactional
    public Member signup(String username, String rawPassword, String displayName) {
        if (memberRepo.existsById(username)) throw new IllegalStateException("이미 존재하는 이름");
        var m = Member.builder()
                .username(username)
                .passwordHash(encoder.encode(rawPassword))
                .displayName(displayName != null ? displayName : username)
                .status(Member.Status.ACTIVE)
                .build();
        return memberRepo.save(m);
    }
}