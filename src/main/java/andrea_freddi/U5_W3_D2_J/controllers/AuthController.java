package andrea_freddi.U5_W3_D2_J.controllers;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.payloads.DipendentePayload;
import andrea_freddi.U5_W3_D2_J.payloads.LoginPayload;
import andrea_freddi.U5_W3_D2_J.payloads.LoginResponseDTO;
import andrea_freddi.U5_W3_D2_J.services.AuthService;
import andrea_freddi.U5_W3_D2_J.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private DipendentiService dipendentiService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginPayload body) {
        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Dipendente save(@RequestBody @Validated DipendentePayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.dipendentiService.save(body);
    }
}
