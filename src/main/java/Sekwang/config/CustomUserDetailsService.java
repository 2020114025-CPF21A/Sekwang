package Sekwang.config;

import Sekwang.Domain.Member;
import Sekwang.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member m = memberRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        if (m.getStatus() == Member.Status.INACTIVE) {
            throw new UsernameNotFoundException("비활성화된 계정");
        }
        return new User(m.getUsername(), m.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + m.getRole().name())));
    }
}