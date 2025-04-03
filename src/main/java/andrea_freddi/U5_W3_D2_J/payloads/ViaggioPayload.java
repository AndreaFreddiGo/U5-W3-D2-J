package andrea_freddi.U5_W3_D2_J.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ViaggioPayload(
        @NotEmpty(message = "La destinazione è obbligatoria")
        String destinazione,
        @NotNull(message = "La data è obbligatoria")
        LocalDate data,
        @NotNull(message = "Lo stato è obbligatorio")
        String stato
) {
}
