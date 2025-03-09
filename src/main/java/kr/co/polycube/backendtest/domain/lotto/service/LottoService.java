package kr.co.polycube.backendtest.domain.lotto.service;

import kr.co.polycube.backendtest.domain.lotto.dto.response.LottoResponseDto;
import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.lotto.repository.LottoRepository;
import kr.co.polycube.backendtest.global.util.LottoNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final LottoRepository lottoRepository;

    @Transactional
    public LottoResponseDto saveLotto() {
        List<Integer> numbers = LottoNumberGenerator.generateLottoNumbers();

        Lotto lotto = Lotto.builder()
                .numbers(numbers)
                .build();
        lottoRepository.save(lotto);

        return LottoResponseDto.from(lotto);
    }
}
