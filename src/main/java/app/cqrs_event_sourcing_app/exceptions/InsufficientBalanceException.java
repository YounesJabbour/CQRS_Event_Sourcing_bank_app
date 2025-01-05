package app.cqrs_event_sourcing_app.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException{
    private String message;
    public InsufficientBalanceException(String message){
        super(message);
    }
}
