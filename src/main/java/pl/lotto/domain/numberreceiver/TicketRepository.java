package pl.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;

interface TicketRepository {
    Ticket save(Ticket ticket);

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime date);
}
