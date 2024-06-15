package pl.lotto.infrastructure.numberreceiver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.infrastructure.numberreceiver.controller.dto.InputNumbersRequestDto;

import java.util.HashSet;
import java.util.Set;


@RestController
@Log4j2
@AllArgsConstructor
public class NumberReceiverRestController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<NumberReceiverResponseDto> inputNumbers(@RequestBody @Valid InputNumbersRequestDto requestDto) {
        Set<Integer> integers = new HashSet<>(requestDto.inputNumbers());
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(integers);
        return ResponseEntity.ok(numberReceiverResponseDto);
    }

    @GetMapping("/info/{ticketId}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable String ticketId) {
        TicketDto ticketDto = numberReceiverFacade.retrieveTicketByTicketId(ticketId);
        return ResponseEntity.ok(ticketDto);
    }
}
