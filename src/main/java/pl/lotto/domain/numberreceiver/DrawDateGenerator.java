package pl.lotto.domain.numberreceiver;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

class DrawDateGenerator {

    private final Clock clock;

    DrawDateGenerator(Clock clock) {
        this.clock = clock;
    }

    LocalDateTime getNextDrawDate() {
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        if (isSaturdayAndBeforeTwelve(currentDateTime)) {
            return LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.of(12, 0, 0));
        }
        LocalDateTime drawDate = currentDateTime.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        return LocalDateTime.of(drawDate.toLocalDate(), LocalTime.of(12,0,0));
    }

    private boolean isSaturdayAndBeforeTwelve(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                && currentDateTime.toLocalTime().isBefore(LocalTime.of(12, 0, 0));
    }
}
