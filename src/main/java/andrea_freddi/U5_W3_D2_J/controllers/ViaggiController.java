package andrea_freddi.U5_W3_D2_J.controllers;

import andrea_freddi.U5_W3_D2_J.entities.Viaggio;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.payloads.ViaggioPayload;
import andrea_freddi.U5_W3_D2_J.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

/*

1. GET http://localhost:3001/viaggi
2. POST http://localhost:3001/viaggi (+ req.body) --> 201
3. GET http://localhost:3001/viaggi/{id}
4. PUT http://localhost:3001/viaggi/{id} (+ req.body)
5. DELETE http://localhost:3001/viaggi/{id} --> 204

*/

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {
    @Autowired
    private ViaggiService viaggiService;

    // 1. GET http://localhost:3001/viaggi
    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy) {
        // Inserisco valori di default per far si che non ci siano errori se il client non ci invia uno dei query parameters
        return this.viaggiService.getViaggi(page, size, sortBy);
    }

    // 2. POST http://localhost:3001/viaggi (+ req.body) --> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public Viaggio save(@RequestBody @Validated ViaggioPayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.viaggiService.save(body);
    }

    // 3. GET http://localhost:3001/viaggi/{id}
    @GetMapping("/{id}")
    public Viaggio findById(@PathVariable UUID id) {
        return this.viaggiService.findById(id);
    }

    // 4. PUT http://localhost:3001/viaggi/{id} (+ req.body)
    @PutMapping("/{id}")
    public Viaggio findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ViaggioPayload body, BindingResult validationResult) {
        // inserisco anche BindingResult per poter visualizzare eventuali errori di validazione
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati i seguenti errori nel payload: " + message);
        }
        return this.viaggiService.findByIdAndUpdate(id, body);
    }

    // 5. DELETE http://localhost:3001/viaggi/{id} --> 204
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204
    public void findByIdAndDelete(@PathVariable UUID id) {
        this.viaggiService.findByIdAndDelete(id);
    }

    // 6. PATCH http://localhost:3001/viaggi/{id}
    @PatchMapping("/{id}")
    public void findByIdAndUpdateStatus(@PathVariable UUID id) {
        this.viaggiService.updateStato(id);
    }

}



