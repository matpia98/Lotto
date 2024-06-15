package pl.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(LocalDateTime drawDate,
                        String ticketId,
                        Set<Integer> numbersFromUser) implements Serializable {
}
