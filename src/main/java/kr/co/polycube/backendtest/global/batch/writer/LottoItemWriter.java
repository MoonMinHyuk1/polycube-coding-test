package kr.co.polycube.backendtest.global.batch.writer;

import kr.co.polycube.backendtest.domain.winner.entity.Winner;
import kr.co.polycube.backendtest.domain.winner.repository.WinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LottoItemWriter implements ItemWriter<Winner> {

    private final WinnerRepository winnerRepository;

    @Override
    public void write(Chunk<? extends Winner> chunk) throws Exception {
        List<? extends Winner> winners = chunk.getItems();
        winnerRepository.saveAll(winners);
    }
}
