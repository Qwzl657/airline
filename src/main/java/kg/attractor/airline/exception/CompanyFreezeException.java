package kg.attractor.airline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CompanyFreezeException extends RuntimeException {
    public CompanyFreezeException(String message) {
        super(message);
    }
}