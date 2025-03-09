package kr.co.polycube.backendtest.global.batch.reader;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.lotto.repository.LottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@StepScope
@Component
@RequiredArgsConstructor
public class LottoItemReader implements ItemReader<Lotto> {

    private final LottoRepository lottoRepository;

    private Iterator<Lotto> lottoIterator;

    @Override
    public Lotto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (lottoIterator == null) {
            List<Lotto> lottoList = lottoRepository.findAll();
            lottoIterator = lottoList.iterator();
        }
        return lottoIterator.hasNext() ? lottoIterator.next() : null;
    }
}
