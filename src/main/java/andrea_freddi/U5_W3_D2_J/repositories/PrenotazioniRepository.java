package andrea_freddi.U5_W3_D2_J.repositories;

import andrea_freddi.U5_W3_D2_J.entities.Dipendente;
import andrea_freddi.U5_W3_D2_J.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    // Creo un metodo per trovare una prenotazione in base a id dipendente e data del viaggio
    Optional<Prenotazione> findByDipendenteAndViaggioData(Dipendente dipendente, LocalDate data);
}
