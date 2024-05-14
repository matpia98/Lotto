package pl.lotto.domain.numberreceiver;

class TicketMapper {

    static TicketDto mapFromTicket(Ticket ticket) {
        return TicketDto.builder()
                .drawDate(ticket.drawDate())
                .ticketId(ticket.ticketId())
                .numbersFromUser(ticket.numbersFromUser())
                .build();
    }
}
