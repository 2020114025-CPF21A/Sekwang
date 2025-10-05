package Sekwang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(cors -> {});
        http.sessionManagement(sm -> sm.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                // 인증 없이 허용 (auth)
                .requestMatchers("/api/auth/**").permitAll()

                // ✅ 공개 읽기 엔드포인트 (정확 경로 + 하위 경로 모두 허용)
                .requestMatchers(org.springframework.http.HttpMethod.GET,
                        "/api/notices", "/api/notices/**",
                        "/api/songs", "/api/songs/**",
                        "/api/bulletins", "/api/bulletins/**",
                        "/api/gallery", "/api/gallery/**"
                ).permitAll()

                // 관리자 전용 (예시)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 삭제는 관리자/리더만
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN","LEADER")

                // 그 외는 인증 필요
                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}