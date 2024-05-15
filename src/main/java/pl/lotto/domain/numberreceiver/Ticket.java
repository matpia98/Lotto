package pl.lotto.domain.numberreceiver;


import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

//database entity
@Builder
record Ticket(String ticketId, LocalDateTime drawDate, Set<Integer> numbersFromUser) {
}
