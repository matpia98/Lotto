package pl.lotto.domain.numberreceiver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverFacadeConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

//    @Bean
//    TicketRepository ticketRepository() {
//        return new TicketRepository() {
//            @Override
//            public Ticket save(Ticket ticket) {
//                return null;
//            }
//
//            @Override
//            public List<Ticket> findAllByDrawDate(LocalDateTime drawDate) {
//                return null;
//            }
//        };
//    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, drawDateGenerator, hashGenerator, ticketRepository);
    }
    static NumberReceiverFacade createConfigurationForTest(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, drawDateGenerator, hashGenerator, ticketRepository);
    }
}
