package api.giybat.uz.config;

import api.giybat.uz.dto.JwtDTO;
import api.giybat.uz.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher=new AntPathMatcher();

        return Arrays.stream(SpringSecurityConfig.AUTH_WHITELIST)
                .anyMatch(p->pathMatcher.match(p,request.getRequestURI()));
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
final String authorizationHeader = request.getHeader("Authorization");
if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("message","Token not found");
    return;
}
String token = authorizationHeader.substring(7);
        JwtDTO jwtDTO = null;
        try {
jwtDTO= JwtUtil.decodeJwt(token);

        }catch (RuntimeException e) {
   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
   response.setHeader("message","Token invalid");
    return;
        }
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(jwtDTO.getUsername());
        UsernamePasswordAuthenticationToken authentication=
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
