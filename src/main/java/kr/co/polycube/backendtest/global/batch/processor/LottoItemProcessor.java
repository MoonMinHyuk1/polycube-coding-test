package kr.co.polycube.backendtest.global.batch.processor;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.winner.entity.Winner;
import kr.co.polycube.backendtest.global.util.LottoNumberGenerator;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@JobScope
@Component
public class LottoItemProcessor implements ItemProcessor<Lotto, Winner> {

    private final List<Integer> winningNumbers = LottoNumberGenerator.generateLottoNumbers();

    @Override
    public Winner process(Lotto item) throws Exception {
        List<Integer> lottoNumbers = item.makeNumbers();
        System.out.println("winningNumbers = " + winningNumbers);

        int count = 0;
        for (int number: winningNumbers) {
            if (lottoNumbers.contains(number)) {
                count++;
            }
        }
        Integer rank = switch (count) {
            case 6 -> 1;
            case 5 -> 2;
            case 4 -> 3;
            case 3 -> 4;
            case 2 -> 5;
            default -> null;
        };
        Winner winner = null;
        if (rank != null) {
            winner = Winner.builder()
                    .rank(rank)
                    .lotto(item)
                    .build();
        }

        return winner;
    }
}
