package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface TicketRepository extends MongoRepository<Ticket, String> {
//    Ticket save(Ticket ticket);

    List<Ticket> findAllByDrawDate(LocalDateTime drawDate);

    Optional<Ticket> findByTicketId(String ticketId);
}
