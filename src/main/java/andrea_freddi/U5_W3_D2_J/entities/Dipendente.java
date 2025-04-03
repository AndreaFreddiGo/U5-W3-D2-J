package andrea_freddi.U5_W3_D2_J.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// creo la classe Dipendente e gestisco Getter e Setter e costruttore vuoto con lombok
// invece il costruttore lo gestisco io così come il toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dipendenti")
public class Dipendente {
    @Id
    @Setter(AccessLevel.NONE) // non voglio che venga settato dall'esterno
    @GeneratedValue
    @Column(name = "dipendente_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "immagine_profilo")
    private String immagineProfilo;

    public Dipendente(String cognome, String email, String immagineProfilo, String nome, String password, String username) {
        this.cognome = cognome;
        this.email = email;
        this.immagineProfilo = immagineProfilo;
        this.nome = nome;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Dipendente{" +
                "cognome='" + cognome + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", immagineProfilo='" + immagineProfilo + '\'' +
                '}';
    }
}
