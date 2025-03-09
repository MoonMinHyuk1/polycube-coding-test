package kr.co.polycube.backendtest.domain.lotto.dto.response;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import lombok.Builder;

import java.util.List;

@Builder
public record LottoResponseDto(
        List<Integer> numbers
) {

    public static LottoResponseDto from(Lotto lotto) {
        return LottoResponseDto.builder()
                .numbers(lotto.makeNumbers())
                .build();
    }
}
