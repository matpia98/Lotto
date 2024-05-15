package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {

    static TicketDto mapFromTicket(Ticket ticket) {
        return TicketDto.builder()
                .drawDate(ticket.drawDate())
                .ticketId(ticket.ticketId())
                .numbersFromUser(ticket.numbersFromUser())
                .build();
    }
}
