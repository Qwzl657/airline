package kg.attractor.airline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketAlreadyBookedException extends RuntimeException {
    public TicketAlreadyBookedException(String message) {
        super(message);
    }
}