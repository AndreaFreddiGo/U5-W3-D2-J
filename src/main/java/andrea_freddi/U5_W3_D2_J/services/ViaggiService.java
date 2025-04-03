package andrea_freddi.U5_W3_D2_J.services;

import andrea_freddi.U5_W3_D2_J.entities.Stato;
import andrea_freddi.U5_W3_D2_J.entities.Viaggio;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.exceptions.NotFoundException;
import andrea_freddi.U5_W3_D2_J.payloads.ViaggioPayload;
import andrea_freddi.U5_W3_D2_J.repositories.ViaggiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class ViaggiService {
    @Autowired
    private ViaggiRepository viaggiRepository;


    // Creo un metodo per salvare un nuovo viaggio
    public Viaggio save(ViaggioPayload body) {
        // Controllo se esiste già un viaggio programmato per la stessa data e la stessa destinazione
        viaggiRepository.findByDataAndDestinazione(body.data(), body.destinazione()).ifPresent(
                v -> {
                    throw new BadRequestException("Viaggio già programmato per la data " + body.data() + " e destinazione " + body.destinazione());
                } // Se trovo un viaggio con quella data e destinazione allora lancio un'eccezione
        );
        // Se non trovo un viaggio con quella data e destinazione allora creo un nuovo viaggio
        // Faccio un controllo anche su Stato per essere certo che non venga inserita una opzione non valida
        Stato statoEnum;
        Set<Stato> statiAmmessi = Set.of(Stato.IN_PROGRAMMA, Stato.COMPLETATO);
        try {
            statoEnum = Stato.valueOf(body.stato().toUpperCase());
            if (!statiAmmessi.contains(statoEnum)) {
                throw new BadRequestException("Stato non valido. Valori ammessi: " + statiAmmessi);
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Stato non valido. Valori ammessi: " + statiAmmessi);
        }
        if (!body.stato().toString().equals("IN_PROGRAMMA") && !body.stato().toString().equals("COMPLETATO")) {
            throw new BadRequestException("Stato non valido");
        }
        Viaggio nuovoViaggio = new Viaggio(body.data(), body.destinazione(), statoEnum);
        return viaggiRepository.save(nuovoViaggio);
    }

    // Creo un metodo per trovare tutti i viaggi ma con la paginazione
    public Page<Viaggio> getViaggi(int page, int size, String sortBy) {
        if (size > 100) size = 100; // Limito la size max a 100 così da client non possono inserire numeri troppo grandi
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggiRepository.findAll(pageable);
    }

    // Creo un metodo per trovare un viaggio tramite id
    public Viaggio findById(UUID id) {
        return this.viaggiRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id) // Se non trovo il viaggio con quell'id allora lancio un'eccezione
        );
    }

    // Creo un metodo per eliminare un viaggio tramite id
    public void findByIdAndDelete(UUID id) {
        Viaggio trovato = this.findById(id); // Trovo il viaggio tramite id
        this.viaggiRepository.delete(trovato); // Elimino il viaggio
    }

    // Creo un metodo per aggiornare un viaggio tramite id
    public Viaggio findByIdAndUpdate(UUID id, ViaggioPayload body) {
        Viaggio trovato = this.findById(id); // Trovo il viaggio tramite id
        // Controllo se esiste già un viaggio programmato per la stessa data e la stessa destinazione
        viaggiRepository.findByDataAndDestinazione(body.data(), body.destinazione()).ifPresent(
                v -> {
                    throw new BadRequestException("Viaggio già programmato per la data " + body.data() + " e destinazione " + body.destinazione());
                } // Se trovo un viaggio con quella data e destinazione allora lancio un'eccezione
        );
        // Se non trovo un viaggio con quella data e destinazione allora aggiorno il viaggio
        // Ma prima, faccio anche qui un controllo anche su Stato per essere certo che non venga inserita una opzione non valida
        Stato statoEnum;
        Set<Stato> statiAmmessi = Set.of(Stato.IN_PROGRAMMA, Stato.COMPLETATO);
        try {
            statoEnum = Stato.valueOf(body.stato().toUpperCase());
            if (!statiAmmessi.contains(statoEnum)) {
                throw new BadRequestException("Stato non valido. Valori ammessi: " + statiAmmessi);
            }
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Stato non valido. Valori ammessi: " + statiAmmessi);
        }
        if (!body.stato().toString().equals("IN_PROGRAMMA") && !body.stato().toString().equals("COMPLETATO")) {
            throw new BadRequestException("Stato non valido");
        }
        trovato.setData(body.data());
        trovato.setDestinazione(body.destinazione());
        trovato.setStato(statoEnum);
        return this.viaggiRepository.save(trovato); // Salvo il viaggio aggiornato
    }

    // Creo un metodo per aggiornare lo stato del viaggio se la data dello stesso è ormai passata
    // Non mi serve passare un parametro diverso perché impone di default stato "COMPLETATO" nel
    // caso in cui dovesse verificare che la data è passata
    public void updateStato(UUID id) {
        Viaggio trovato = this.findById(id); // Trovo il viaggio tramite id
        Stato nuovoStato = Stato.valueOf("COMPLETATO");
        if (trovato.getData().isAfter(java.time.LocalDate.now())) {
            throw new BadRequestException("Non puoi aggiornare lo stato di un viaggio che non è ancora partito");
        }
        trovato.setStato(nuovoStato);
        this.viaggiRepository.save(trovato);
    }
}
