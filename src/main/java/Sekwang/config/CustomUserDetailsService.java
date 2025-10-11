package Sekwang.config;

import Sekwang.Domain.Member;
import Sekwang.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member m = memberRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        if (m.getStatus() == Member.Status.INACTIVE) {
            // 보안상 UsernameNotFoundException으로 통일(활성/비활성 노출 방지)
            throw new UsernameNotFoundException("사용자 없음: " + username);
        }

        List<GrantedAuthority> authorities = mapAuthorities(m.getRole());

        // passwordHash 사용(폼 로그인/패스워드 비교 시), JWT만 쓰면 빈 문자열이어도 무방
        String password = m.getPasswordHash() != null ? m.getPasswordHash() : "";

        return User.builder()
                .username(m.getUsername())
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false) // INACTIVE는 위에서 걸렀음
                .build();
    }

    /**
     * 권한 계층:
     * ADMIN  ⊇ STAFF ⊇ LEADER ⊇ MEMBER
     * → ADMIN: ROLE_ADMIN, ROLE_STAFF, ROLE_LEADER, ROLE_MEMBER
     * → STAFF: ROLE_STAFF, ROLE_LEADER, ROLE_MEMBER
     * → LEADER: ROLE_LEADER, ROLE_MEMBER
     * → MEMBER: ROLE_MEMBER
     */
    private List<GrantedAuthority> mapAuthorities(Member.Role role) {
        List<GrantedAuthority> list = new ArrayList<>();
        switch (role) {
            case ADMIN -> {
                list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                list.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                list.add(new SimpleGrantedAuthority("ROLE_LEADER"));
                list.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            }
            case STAFF -> {
                list.add(new SimpleGrantedAuthority("ROLE_STAFF"));
                list.add(new SimpleGrantedAuthority("ROLE_LEADER"));
                list.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            }
            case LEADER -> {
                list.add(new SimpleGrantedAuthority("ROLE_LEADER"));
                list.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            }
            case MEMBER -> {
                list.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            }
        }
        return list;
    }
}
