package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.numberreceiver.dto.InputNumberResultDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {

    NumberValidator validator = new NumberValidator();
    InMemoryNumberReceiverRepositoryTestImpl repository = new InMemoryNumberReceiverRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            validator,
            repository
            );
    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);

        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_range_of_1_to_99() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2000, 3, 4, 5, 6);

        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 3, 4, 5, 6);

        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_save_to_database_when_user_gave_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.now();

        // when
        List<TicketDto> ticketDtos = numberReceiverFacade.userNumbers(drawDate);

        // then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(result.drawDate())
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );
    }

}