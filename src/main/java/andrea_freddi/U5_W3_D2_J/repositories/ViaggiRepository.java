package andrea_freddi.U5_W3_D2_J.repositories;

import andrea_freddi.U5_W3_D2_J.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ViaggiRepository extends JpaRepository<Viaggio, UUID> {
    // Creo un metodo per trovare un viaggio in base alla data e alla destinazione
    Optional<Viaggio> findByDataAndDestinazione(LocalDate data, String destinazione);
}
