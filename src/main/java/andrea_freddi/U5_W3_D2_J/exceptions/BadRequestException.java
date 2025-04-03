package andrea_freddi.U5_W3_D2_J.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String messaggio) {
        super(messaggio);
    }
}
