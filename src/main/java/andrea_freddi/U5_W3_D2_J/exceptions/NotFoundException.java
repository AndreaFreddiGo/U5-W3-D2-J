package andrea_freddi.U5_W3_D2_J.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String messaggio) {
        super(messaggio);
    }

    public NotFoundException(UUID id) {
        super(id + " non trovato!");
    }
}