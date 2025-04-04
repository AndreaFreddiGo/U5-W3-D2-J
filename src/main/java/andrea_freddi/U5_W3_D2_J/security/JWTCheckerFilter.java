package andrea_freddi.U5_W3_D2_J.security;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.exceptions.UnauthorizedException;
import andrea_freddi.U5_W3_D2_J.services.DipendentiService;
import andrea_freddi.U5_W3_D2_J.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component // Non dimenticare @Component altrimenti questa classe non verrà utilizzata nella catena dei filtri
public class JWTCheckerFilter extends OncePerRequestFilter {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private JWT jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        String token = authHeader.substring(7);

        jwt.verifyToken(token);

        String dipendenteId = jwt.getIdFromToken(token);
        Dipendente dipendenteAttuale = this.dipendentiService.findById(UUID.fromString(dipendenteId));

        Authentication authentication = new UsernamePasswordAuthenticationToken(dipendenteAttuale, null, dipendenteAttuale.getAuthorities());
        // Il terzo parametro serve per poter utilizzare i vari @PreAuthorize perchè così il SecurityContext saprà quali sono i ruoli dell'utente
        // che sta effettuando la richiesta
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    // Voglio disabilitare il filtro per tutte le richieste al controller Auth, quindi tutte le richieste che avranno come URL /auth/** non dovranno
    // avere il controllo del token
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
