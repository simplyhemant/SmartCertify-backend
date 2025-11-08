package SmartCertify_backend.SmartCertify_backend.config;

import SmartCertify_backend.SmartCertify_backend.exception.TokenValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // remove "Bearer " prefix

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = claims.getSubject(); // preferred over custom key "email"
                String authorities = (String) claims.get("authorities");

                if (email == null || authorities == null) {
                    throw new TokenValidationException("Invalid token: missing claims");
                }

                List<GrantedAuthority> auths =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                throw new TokenValidationException("Token has expired. Please log in again.");
            } catch (UnsupportedJwtException e) {
                throw new TokenValidationException("Unsupported JWT token.");
            } catch (MalformedJwtException e) {
                throw new TokenValidationException("Malformed JWT token.");
            } catch (SignatureException e) {
                throw new TokenValidationException("Invalid JWT signature.");
            } catch (IllegalArgumentException e) {
                throw new TokenValidationException("Empty or null JWT token.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
