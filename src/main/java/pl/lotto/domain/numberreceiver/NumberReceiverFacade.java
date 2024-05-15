package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser){
        List<ValidationResult> validationResultList = numberValidator.validate(numbersFromUser);
        if (!validationResultList.isEmpty()) {
            List<String> errorResultMessages = numberValidator.createErrorResultMessages();
            return new NumberReceiverResponseDto(null, errorResultMessages);
        }
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        String hash = hashGenerator.getHash();
        Ticket savedTicket = Ticket.builder()
                .ticketId(hash)
                .drawDate(nextDrawDate)
                .numbersFromUser(numbersFromUser)
                .build();
        ticketRepository.save(savedTicket);

        TicketDto ticketDto = TicketDto.builder()
                .ticketId(savedTicket.ticketId())
                .drawDate(savedTicket.drawDate())
                .numbersFromUser(savedTicket.numbersFromUser())
                .build();

        return new NumberReceiverResponseDto(ticketDto, List.of("success"));
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = ticketRepository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .collect(Collectors.toList());
    }


    List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = retrieveNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().equals(date))
                .map(ticket -> TicketDto.builder()
                        .drawDate(ticket.drawDate())
                        .ticketId(ticket.ticketId())
                        .numbersFromUser(ticket.numbersFromUser())
                        .build())
                .collect(Collectors.toList());
    }
}
