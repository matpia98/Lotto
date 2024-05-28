package pl.lotto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pl.lotto.domain.AdjustableClock;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
@Profile("integration")
public class IntegrationConfiguration {

    @Bean
    @Primary
    Clock adjustableClock() {
        return AdjustableClock.ofLocalDateAndLocalTime(
                LocalDate.of(2024, 5, 27),
                LocalTime.of(10, 0, 0),
                ZoneId.systemDefault());
    }
}
