package pl.lotto.infrastructure.resultannouncer.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.lotto.domain.resultchecker.PlayerResultNotFoundException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Log4j2
public class ResultAnnouncerControllerErrorHandler {
    @ExceptionHandler(PlayerResultNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResultAnnouncerErrorResponse> handlePlayerResultNotFoundException(PlayerResultNotFoundException exception){
        String message = exception.getMessage();
        log.error(message);
        return ResponseEntity.status(NOT_FOUND).body(new ResultAnnouncerErrorResponse(message, NOT_FOUND));
    }
}
