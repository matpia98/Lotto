package pl.lotto.domain.numberreceiver.dto;

import java.util.List;

public record NumberReceiverResponseDto(
        TicketDto ticketDto,
        List<String> messages
) {
}
