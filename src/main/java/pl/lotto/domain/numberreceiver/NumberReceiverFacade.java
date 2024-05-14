package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.InputNumberResultDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final NumberReceiverRepository repository;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser){
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now();
            Ticket savedTicket = repository.save(new Ticket(ticketId, drawDate, numbersFromUser));
            return InputNumberResultDto.builder()
                    .message("success")
                    .drawDate(savedTicket.drawDate())
                    .ticketId(savedTicket.ticketId())
                    .numbersFromUser(numbersFromUser)
                    .build();
        }
        return InputNumberResultDto.builder()
                .message("failed")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .collect(Collectors.toList());
    }



}
