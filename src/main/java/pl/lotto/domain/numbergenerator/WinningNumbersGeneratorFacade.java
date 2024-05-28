package pl.lotto.domain.numbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable randomGenerable;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacadeConfigurationProperties properties;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        SixRandomNumbersDto dto = randomGenerable.generateSixRandomNumbers(
                properties.count(), properties.lowerBand(), properties.upperBand()
        );
        Set<Integer> winningNumbers = dto.numbers();
        winningNumberValidator.validate(winningNumbers);
        WinningNumbers winningNumbersDocument = WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build();
        WinningNumbers saved = winningNumbersRepository.save(winningNumbersDocument);
        return WinningNumbersDto.builder()
                .winningNumbers(saved.winningNumbers())
                .date(saved.date())
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime date) {
        WinningNumbers numbersByDate = winningNumbersRepository.findFirstByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not Found"));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .date(numbersByDate.date())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }
}
