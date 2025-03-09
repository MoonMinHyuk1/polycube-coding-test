package kr.co.polycube.backendtest.domain.lotto.service;

import kr.co.polycube.backendtest.domain.lotto.dto.response.LottoResponseDto;
import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.lotto.repository.LottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final LottoRepository lottoRepository;

    @Transactional
    public LottoResponseDto saveLotto() {
        List<Integer> numbers = generateLottoNumbers();

        Lotto lotto = Lotto.builder()
                .numbers(numbers)
                .build();
        lottoRepository.save(lotto);

        return LottoResponseDto.from(lotto);
    }

    private List<Integer> generateLottoNumbers() {
        Set<Integer> numbers = new HashSet<>();
        Random random = new Random();

        while (numbers.size() < 6) {
            numbers.add(random.nextInt(45) + 1);
        }

        return numbers.stream()
                .sorted()
                .collect(Collectors.toList());
    }
}
