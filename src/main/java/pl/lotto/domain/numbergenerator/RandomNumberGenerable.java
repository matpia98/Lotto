package pl.lotto.domain.numbergenerator;

import java.util.Set;

public interface RandomNumberGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand);
}

