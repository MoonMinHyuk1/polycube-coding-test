package kr.co.polycube.backendtest.domain.lotto.dto.response;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import lombok.Builder;

import java.util.List;

@Builder
public record LottoResponseDto(
        List<Integer> numbers
) {

    public static LottoResponseDto from(Lotto lotto) {
        List<Integer> numbers = List.of(
                lotto.getNumber1(), lotto.getNumber2(), lotto.getNumber3(),
                lotto.getNumber4(), lotto.getNumber5(), lotto.getNumber6()
        );
        return LottoResponseDto.builder()
                .numbers(numbers)
                .build();
    }
}
