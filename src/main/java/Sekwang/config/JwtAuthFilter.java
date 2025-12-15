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

        // 0) CORS 프리플라이트는 무조건 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String path = request.getRequestURI();

        // 1) 공개 엔드포인트는 바로 통과
        if (isPublic(path, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Authorization 헤더 확인 (없으면 막지 않고 통과 → 이후 SecurityConfig에서 판정)
        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();

        try {
            String username = jwtUtil.extractUsername(token);

            if (StringUtils.hasText(username)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails user = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // 유효하지 않으면 컨텍스트만 비우고 계속 진행
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (Exception e) {
            // 파싱/검증 예외가 나도 여기서 401/403을 직접 쓰지 않고 체인 계속
            SecurityContextHolder.clearContext();
        }

        // 3) 다음 필터로
        filterChain.doFilter(request, response);
    }

    private boolean isPublic(String path, String method) {
        // 인증 관련 공개
        if (path.startsWith("/api/auth/"))
            return true;

        // 정적 리소스/헬스체크 등 필요하면 추가
        if (path.startsWith("/actuator/health"))
            return true;

        // 마인크래프트 이벤트 API 공개 (POST)
        if (path.startsWith("/api/minecraft/events/"))
            return true;

        // 읽기 공개 리소스 (GET만)
        if ("GET".equalsIgnoreCase(method)) {
            if (path.equals("/api/notices") || path.startsWith("/api/notices/"))
                return true;
            if (path.equals("/api/songs") || path.startsWith("/api/songs/"))
                return true;
            if (path.equals("/api/bulletins") || path.startsWith("/api/bulletins/"))
                return true;
            if (path.equals("/api/gallery") || path.startsWith("/api/gallery/"))
                return true;
        }
        return false;
    }
}
