package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numberreceiver.dto.InputNumberResultDto;

import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser){
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            return InputNumberResultDto.builder()
                    .message("success")
                    .build();
        }
        return InputNumberResultDto.builder()
                .message("failed")
                .build();
    }



}
