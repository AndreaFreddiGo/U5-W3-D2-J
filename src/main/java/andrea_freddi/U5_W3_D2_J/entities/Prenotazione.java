package andrea_freddi.U5_W3_D2_J.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

// creo la classe Prenotazione e gestisco Getter e Setter e costruttore vuoto con lombok
// invece il costruttore lo gestisco io così come il toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @Setter(AccessLevel.NONE) // non voglio che venga settato dall'esterno
    @GeneratedValue
    @Column(name = "prenotazione_id", nullable = false)
    private UUID id;
    @Column(name = "data_richiesta", nullable = false)
    private LocalDate dataRichiesta;
    private String note;

    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    public Prenotazione(LocalDate dataRichiesta, Dipendente dipendente, String note, Viaggio viaggio) {
        this.dataRichiesta = dataRichiesta;
        this.dipendente = dipendente;
        this.note = note;
        this.viaggio = viaggio;
    }

    @Override
    public String toString() {
        return "Prenotazione{" +
                "dataRichiesta=" + dataRichiesta +
                ", id=" + id +
                ", note='" + note + '\'' +
                ", dipendente=" + dipendente +
                ", viaggio=" + viaggio +
                '}';
    }
}
