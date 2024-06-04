package pl.lotto.domain.numberreceiver;


import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

//database entity
@Builder
@Document(collection = "tickets")
record Ticket(
        String ticketId,
        LocalDateTime drawDate,
        Set<Integer> numbersFromUser) {
}
