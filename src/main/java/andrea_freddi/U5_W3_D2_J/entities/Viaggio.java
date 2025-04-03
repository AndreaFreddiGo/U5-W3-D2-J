package andrea_freddi.U5_W3_D2_J.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

// creo la classe Viaggio e gestisco Getter e Setter e costruttore vuoto con lombok
// invece il costruttore lo gestisco io così come il toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "viaggi")
public class Viaggio {
    @Id
    @Setter(AccessLevel.NONE) // non voglio che venga settato dall'esterno
    @GeneratedValue
    @Column(name = "viaggio_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String destinazione;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Stato stato;

    public Viaggio(LocalDate data, String destinazione, Stato stato) {
        this.data = data;
        this.destinazione = destinazione;
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "Viaggio{" +
                "data=" + data +
                ", id=" + id +
                ", destinazione='" + destinazione + '\'' +
                ", stato=" + stato +
                '}';
    }
}
