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
        // Questo è il metodo che verrà richiamato ad ogni richiesta (a parte quelle che poi stabiliremo non ne abbiano bisogno)
        // Questo filtro dovrà controllare che il token allegato alla richiesta sia valido. Il token lo troveremo nell'Authorization Header (se c'è)
        // Una delle caratteristiche dei filtri è quella di avere l'accesso a tutte le parti della richiesta e quindi anche agli headers.

        // Piano di battaglia:
        // 1. Verifichiamo se nella richiesta è presente l'Authorization Header, e se è ben formato ("Bearer josdjojosdj...") se non c'è oppure
        // se non ha il formato giusto --> 401
        String authHeader = request.getHeader("Authorization");
        // "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MzA3MTk2MTcsImV4cCI6MTczMTMyNDQxNywic3ViIjoiM2RlMDRlYmEtNDJjOC00YzE4LWFhNzUtNzY3MDAwZWVhYmMxIn0.HsVC06J2LXg1-lrWb5ZcenLfm0Wd6zEOCE9-FPTDQrQ"
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");

        // 2. Estraiamo il token dall'header
        String token = authHeader.substring(7);

        // 3. Verifichiamo se il token è stato manipolato (verifichiamo la signature) o se è scaduto (verifichiamo Expiration Date)
        jwt.verifyToken(token);

        // 4. Se tutto è OK, andiamo avanti (passiamo la richiesta al prossimo filtro o al controller)
        filterChain.doFilter(request, response); // Tramite .doFilter(req,res) richiamo il prossimo membro della catena (o un filtro o un controller)

        // 5. Se qualcosa non va con il token --> 401
    }

    // Voglio disabilitare il filtro per tutte le richieste al controller Auth, quindi tutte le richieste che avranno come URL /auth/** non dovranno
    // avere il controllo del token
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
