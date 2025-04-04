package andrea_freddi.U5_W3_D2_J.security;

import andrea_freddi.U5_W3_D2_J.exceptions.UnauthorizedException;
import andrea_freddi.U5_W3_D2_J.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Non dimenticare @Component altrimenti questa classe non verrà utilizzata nella catena dei filtri
public class JWTCheckerFilter extends OncePerRequestFilter {

    @Autowired
    private JWT jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        String token = authHeader.substring(7);

        jwt.verifyToken(token);

        
        filterChain.doFilter(request, response);

    }

    // Voglio disabilitare il filtro per tutte le richieste al controller Auth, quindi tutte le richieste che avranno come URL /auth/** non dovranno
    // avere il controllo del token
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
