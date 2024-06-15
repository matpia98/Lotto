package pl.lotto.infrastructure.numberreceiver.error;

import org.springframework.http.HttpStatus;

public record GetTicketErrorResponse(
        String message,
        HttpStatus status) {
}
