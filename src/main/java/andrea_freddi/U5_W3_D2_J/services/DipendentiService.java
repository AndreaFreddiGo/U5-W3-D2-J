package andrea_freddi.U5_W3_D2_J.services;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.exceptions.BadRequestException;
import andrea_freddi.U5_W3_D2_J.exceptions.NotFoundException;
import andrea_freddi.U5_W3_D2_J.payloads.DipendentePayload;
import andrea_freddi.U5_W3_D2_J.repositories.DipendentiRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class DipendentiService {
    // Inietto il Cloudinary per caricare le immagini
    @Autowired
    Cloudinary cloudinaryUploader;
    @Autowired
    private DipendentiRepository dipendentiRepository;

    // Creo un metodo per salvare un nuovo dipendente
    public Dipendente save(DipendentePayload body) {
        // Controllo se il dipendente (cioè l'email) esiste già
        dipendentiRepository.findByEmail(body.email()).ifPresent(
                d -> {
                    throw new RuntimeException("Email " + body.email() + " già in uso");
                }
        );
        // Se non trovo nessun dipendente con quell'email, creo un nuovo dipendente
        Dipendente nuovoDipendente = new Dipendente(body.cognome(), body.email(),
                "https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome(),
                body.nome(), body.password(), body.username());
        return dipendentiRepository.save(nuovoDipendente);
    }

    // Creo un metodo per trovare tutti i dipendenti
    public Page<Dipendente> getDipendenti(int page, int size, String sortBy) {
        if (size > 100) size = 100; // Limito la dimensione massima a 100
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }

    // Creo un metodo per trovare un dipendente per id
    public Dipendente findById(UUID id) {
        return this.dipendentiRepository.findById(id).orElseThrow(
                () -> new NotFoundException(id) // Se non trovo il dipendente, lancio un'eccezione
        );
    }

    // Creo un metodo per trovare un dipendente per email
    public Dipendente findByEmail(String email) {
        return this.dipendentiRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("Il dipendente con la email " + email + " non è stato trovato!") // Se non trovo il dipendente, lancio un'eccezione
        );
    }

    // Creo un metodo per eliminare un dipendente per id
    public void findByIdAndDelete(UUID id) {
        Dipendente trovato = this.findById(id); // Trovo il dipendente
        this.dipendentiRepository.delete(trovato); // Elimino il dipendente
    }

    // Creo un metodo per aggiornare un dipendente
    public Dipendente findByIdAndUpdate(UUID id, DipendentePayload body) {
        Dipendente trovato = this.findById(id); // Trovo il dipendente tramite l'id
        if (!trovato.getEmail().equals(body.email())) { // Se l'email è diversa da quella del dipendente trovato
            // ma l'email è già in uso da un altro dipendente allora lancio un'eccezione
            this.dipendentiRepository.findByEmail(body.email()).ifPresent(
                    d -> {
                        throw new RuntimeException("Email " + body.email() + " già in uso");
                    }
            );
        }
        trovato.setNome(body.nome());
        trovato.setCognome(body.cognome());
        trovato.setUsername(body.username());
        trovato.setPassword(body.password());
        return this.dipendentiRepository.save(trovato); // Salvo il dipendente aggiornato
    }

    // Creo un metodo per caricare l'immagine profilo per il dipendente
    public Dipendente uploadImmagineProfilo(UUID id, MultipartFile file) {
        // Cerco il dipendente tramite l'id
        Dipendente dipendenteTrovato = this.findById(id);
        // Chiamo l'uploader di Cloudinary per caricare l'immagine e mi restituirà l'url dell'immagine
        // che io potrò salvare nel dipendente
        String url = null;
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati errori nel caricamento dell'immagine profilo!");
        }
        // Se l'upload va a buon fine, salvo l'url dell'immagine nel dipendente
        dipendenteTrovato.setImmagineProfilo(url);
        // Salvo il dipendente aggiornato
        return this.dipendentiRepository.save(dipendenteTrovato);
    }
}

