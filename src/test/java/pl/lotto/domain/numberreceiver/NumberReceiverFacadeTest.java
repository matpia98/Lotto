package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {
    TicketRepository ticketRepository = new InMemoryTicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();
//    NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(
//            hashGenerator,
//            clock,
//            ticketRepository
//    );
    @Test
    public void should_return_correct_response_when_user_gave_six_numbers_in_range() {
        // given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);
        HashGenerable hashGenerator = new HashGeneratorTestImpl("123");
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto expectedTicket = TicketDto.builder()
                .drawDate(nextDrawDate)
                .ticketId(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .build();

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(expectedTicket, List.of("success"));
        assertThat(response.ticketDto()).isEqualTo(expectedResponse.ticketDto());
        assertThat(response.messages()).isEqualTo(expectedResponse.messages());
    }

    @Test
    public void it_should_return_failed_message_when_user_input_six_numbers_but_one_number_is_out_of_range() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 100);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, List.of(
                "YOU SHOULD GIVE NUMBERS FROM 1 TO 99"
        ));
        assertThat(response.ticketDto()).isEqualTo(expectedResponse.ticketDto());
        assertThat(response.messages()).isEqualTo(expectedResponse.messages());
    }

    @Test
    public void should_return_failed_messages_when_user_input_six_numbers_but_one_number_is_out_of_range_and_is_negative() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, -4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, List.of(
                "YOU SHOULD GIVE NUMBERS FROM 1 TO 99"
        ));
        assertThat(response.ticketDto()).isEqualTo(expectedResponse.ticketDto());
        assertThat(response.messages()).isEqualTo(expectedResponse.messages());
    }

    @Test
    public void should_return_failed_message_when_user_input_less_than_six_numbers() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, List.of(
                "YOU MUST GIVE SIX NUMBERS"
        ));
        assertThat(response.ticketDto()).isEqualTo(expectedResponse.ticketDto());
        assertThat(response.messages()).isEqualTo(expectedResponse.messages());
    }

    @Test
    public void should_return_failed_message_when_user_input_more_than_six_numbers() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, List.of(
                "YOU MUST GIVE SIX NUMBERS"
        ));
        assertThat(response.ticketDto()).isEqualTo(expectedResponse.ticketDto());
        assertThat(response.messages()).isEqualTo(expectedResponse.messages());
    }

    @Test
    public void should_return_correct_hash() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        String hash = response.ticketDto().ticketId();
        assertThat(hash).hasSize(36);
        assertThat(hash).isNotNull();
    }

    @Test
    public void should_return_correct_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2024, 5, 14, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, fixedClock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime testedDrawDate = response.ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 5, 18, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_correct_draw_date_by_retrieve() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock fixedClock = Clock.fixed(LocalDateTime.of( 2024, 5, 14, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, fixedClock, ticketRepository);

        // when
        LocalDateTime drawDate = numberReceiverFacade.retrieveNextDrawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 5, 18, 12, 0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2024, 5, 11, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, fixedClock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = response.ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 5, 18, 12, 0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2024, 5, 11, 18, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/Warsaw"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, fixedClock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = response.ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 5, 18, 12, 0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Instant fixedInstant = LocalDateTime.of(2024, 5, 16, 12, 0, 0).toInstant(ZoneOffset.UTC);
        AdjustableClock clock = new AdjustableClock(fixedInstant, ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        NumberReceiverResponseDto draw1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto draw2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto draw3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto draw4 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        LocalDateTime drawDate = draw1.ticketDto().drawDate();
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        // then
        assertThat(allTicketsByDate).containsOnly(draw1.ticketDto(), draw2.ticketDto());
    }

    @Test
    public void should_return_empty_list_if_there_are_no_tickets() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2024, 5, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, fixedClock, ticketRepository);

        // when
        List<TicketDto> allTicketsByNextDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(LocalDateTime.now(clock));

        // then
        assertThat(allTicketsByNextDrawDate).isEmpty();


    }

    @Test
    public void should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 5, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = NumberReceiverFacadeConfiguration.createConfigurationForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = response.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByNextDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusDays(7L));

        // then
        assertThat(allTicketsByNextDrawDate).isEmpty();
    }
}