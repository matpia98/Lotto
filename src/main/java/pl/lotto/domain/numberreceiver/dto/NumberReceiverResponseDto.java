package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NumberReceiverResponseDto(
        TicketDto ticketDto,
        List<String> messages
) {
}
