package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

interface TicketRepository extends MongoRepository<Ticket, String> {
//    Ticket save(Ticket ticket);

    List<Ticket> findAllByDrawDate(LocalDateTime drawDate);

}
