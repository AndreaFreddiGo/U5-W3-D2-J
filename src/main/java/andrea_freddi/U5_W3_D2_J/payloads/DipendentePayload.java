package andrea_freddi.U5_W3_D2_J.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record DipendentePayload(
        @NotEmpty(message = "Lo username è obbligatorio")
        @Size(min = 2, max = 20, message = "Lo username deve essere compreso tra 2 e 20 caratteri!")
        String username,
        @NotEmpty(message = "Il nome proprio è obbligatorio")
        @Size(min = 2, max = 20, message = "Il nome deve essere compreso tra 2 e 20 caratteri!")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 20, message = "Il cognome deve essere compreso tra 2 e 20 caratteri!")
        String cognome,
        @NotEmpty(message = "L'indirizzo email è obbligatorio")
        @Email(message = "L'email inserita non è un'email valida")
        String email,
        @NotEmpty(message = "La password è un campo obbligatorio!")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri!")
        String password
) {
}
