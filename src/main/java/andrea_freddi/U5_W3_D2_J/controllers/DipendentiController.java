package andrea_freddi.U5_W3_D2_J.controllers;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.payloads.DipendentePayload;
import andrea_freddi.U5_W3_D2_J.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;

/*

1. GET http://localhost:3001/dipendenti
2. POST http://localhost:3001/dipendenti (+ req.body) --> 201
3. GET http://localhost:3001/dipendenti/{dipendenteId}
4. PUT http://localhost:3001/dipendenti/{dipendenteId} (+ req.body)
5. DELETE http://localhost:3001/dipendenti/{dipendenteId} --> 204

*/

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {
    @Autowired
    private DipendentiService dipendentiService;

    // 1. GET http://localhost:3001/dipendenti
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    // <-- Solo gli ADMIN possono visualizzare la lista dei dipendenti in questa app
    public Page<Dipendente> findAll(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        // Inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.dipendentiService.getDipendenti(page, size, sortBy);
    }

    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente) {
        return currentAuthenticatedDipendente;
    }

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente,
                                    @RequestBody @Validated DipendentePayload body,
                                    BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.dipendentiService.findByIdAndUpdate(currentAuthenticatedDipendente.getId(), body);
    }

    @DeleteMapping("/me")
    public void deleteProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente) {
        this.dipendentiService.findByIdAndDelete(currentAuthenticatedDipendente.getId());
    }

    // 3. GET http://localhost:3001/dipendenti/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findById(@PathVariable UUID id) {
        return this.dipendentiService.findById(id);
    }

    // 4. PUT http://localhost:3001/dipendenti/{id} (+ req.body)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Dipendente findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated DipendentePayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.dipendentiService.findByIdAndUpdate(id, body);
    }

    // 5. DELETE http://localhost:3001/dipendenti/{id} --> 204
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.dipendentiService.findByIdAndDelete(id);
    }

    // 6. PATCH http://localhost:3001/dipendenti/{id}/immagineProfilo (+ req.body)
    @PatchMapping("/{id}/immagineProfilo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateImmagineProfilo(@PathVariable UUID id, @RequestParam("immagineProfilo") MultipartFile file) {
        this.dipendentiService.uploadImmagineProfilo(id, file);
    }
}
