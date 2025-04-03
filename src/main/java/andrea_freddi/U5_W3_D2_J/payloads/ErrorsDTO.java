package andrea_freddi.U5_W3_D2_J.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp) {
}
