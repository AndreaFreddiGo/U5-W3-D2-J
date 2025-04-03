package andrea_freddi.U5_W3_D2_J.services;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.exceptions.UnauthorizedException;
import andrea_freddi.U5_W3_D2_J.payloads.LoginPayload;
import andrea_freddi.U5_W3_D2_J.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendentiService dipendentiService;
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(LoginPayload body) {
        Dipendente trovato = dipendentiService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), trovato.getPassword())) {
            String token = jwt.createToken(trovato);
            return token;
        } else {
            throw new UnauthorizedException("Credenziali non valide");
        }
    }
}
