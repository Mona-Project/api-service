package project.apiservice.infrastructure.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.apiservice.domain.exception.InvalidBody;
import project.apiservice.domain.exception.NoAccess;
import project.apiservice.shared.utils.TokenExtractionUtils;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String token = TokenExtractionUtils.extractToken(request);

        if (jwtUtil.validateJwtToken(token)) {
            final String username = jwtUtil.getUsernameFromJwtToken(token);

            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwtUtil.getRoleFromJwtToken(token));

            final UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username,
                                                            null,
                                                            Collections.singleton(authority)
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
            filterChain.doFilter(request,
                                 response
            );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        final String path = request.getRequestURI();
        return path.contains("login") || path.contains("actuator") || path.contains("register");
    }
}
