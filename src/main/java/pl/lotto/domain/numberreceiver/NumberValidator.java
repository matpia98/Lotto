package pl.lotto.domain.numberreceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {

    private static final int QUANTITY_OF_NUMBERS_FROM_USER = 6;
    private static final int MINIMAL_NUMBER_FROM_USER = 1;
    private static final int MAXIMAL_NUMBER_FROM_USER = 99;
    List<ValidationResult> errors = new ArrayList<>();


    List<ValidationResult> validate(Set<Integer> numbersFromUser) {
        if (!isNumberSizeEqualsSix(numbersFromUser)) {
            errors.add(new ValidationResult("YOU MUST GIVE SIX NUMBERS"));
        }
        if (!areAllNumbersInRange(numbersFromUser)) {
            errors.add(new ValidationResult("YOU SHOULD GIVE NUMBERS FROM 1 TO 99"));
        }
        return errors;
    }

    List<String> createErrorResultMessages() {
        return this.errors
                .stream()
                .map(ValidationResult::message)
                .collect(Collectors.toList());
    }

    private boolean isNumberSizeEqualsSix(Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == 6;
    }

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
            return numbersFromUser
                    .stream()
                    .allMatch(number -> number >= MINIMAL_NUMBER_FROM_USER && number <= MAXIMAL_NUMBER_FROM_USER);
    }
}
