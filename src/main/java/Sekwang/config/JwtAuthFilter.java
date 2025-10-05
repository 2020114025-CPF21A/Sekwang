package Sekwang.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 0) CORS 프리플라이트는 무조건 패스
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String path = request.getRequestURI();

        // 1) 공개 엔드포인트는 바로 패스
        if (isPublic(path, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Authorization 헤더 확인
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            // 헤더 없으면 차단하지 말고 다음 필터로 (SecurityConfig가 판정)
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();
        try {
            String username = jwtUtil.extractUsername(token);
            if (StringUtils.hasText(username) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails user = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            // 파싱/검증 에러가 나도 여기서 막지 않고 흘려보냄 (로그만 남기고 싶으면 주석 해제)
            // log.warn("JWT 검증 실패: {}", ex.getMessage());
        }

        // 3) 다음 필터로
        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path, String method) {
        // 인증/회원가입 계열
        if (path.startsWith("/api/auth/")) return true;

        // 읽기 공개 리소스 (GET만)
        if ("GET".equalsIgnoreCase(method)) {
            if (path.equals("/api/notices") || path.startsWith("/api/notices/")) return true;
            if (path.equals("/api/songs") || path.startsWith("/api/songs/")) return true;
            if (path.equals("/api/bulletins") || path.startsWith("/api/bulletins/")) return true;
            if (path.equals("/api/gallery") || path.startsWith("/api/gallery/")) return true;
        }

        // 필요 시 추가 공개 경로 여기에 더하기
        return false;
    }
}
