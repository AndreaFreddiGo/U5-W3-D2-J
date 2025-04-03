package andrea_freddi.U5_W3_D2_J.controllers;

import andrea_freddi.U5_W3_D2_J.entities.Prenotazione;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.payloads.PrenotazionePayload;
import andrea_freddi.U5_W3_D2_J.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

/*

1. GET http://localhost:3001/prenotazioni
2. POST http://localhost:3001/prenotazioni (+ req.body) --> 201
3. GET http://localhost:3001/prenotazioni/{prenotazioneId}
4. PUT http://localhost:3001/prenotazioni/{prenotazioneId} (+ req.body)
5. DELETE http://localhost:3001/prenotazioni/{prenotazioneId} --> 204

*/

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    @Autowired
    PrenotazioniService prenotazioniService;

    // 1. GET http://localhost:3001/prenotazioni
    @GetMapping
    public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        // Inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.prenotazioniService.getPrenotazioni(page, size, sortBy);
    }

    // 2. POST http://localhost:3001/prenotazioni (+ req.body) --> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Prenotazione save(@RequestBody @Validated PrenotazionePayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.prenotazioniService.save(body);
    }

    // 3. GET http://localhost:3001/prenotazioni/{id}
    @GetMapping("/{id}")
    public Prenotazione findById(@PathVariable UUID id) {
        return this.prenotazioniService.findById(id);
    }

    // 4. PUT http://localhost:3001/prenotazioni/{id} (+ req.body)
    @PutMapping("/{id}")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated PrenotazionePayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.prenotazioniService.findByIdAndUpdate(id, body);
    }

    // 5. DELETE http://localhost:3001/prenotazioni/{id} --> 204
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.prenotazioniService.findByIdAndDelete(id);
    }
}
