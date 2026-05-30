package kg.attractor.airline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFlightTimeException extends RuntimeException {
    public InvalidFlightTimeException(String message) {
        super(message);
    }
}