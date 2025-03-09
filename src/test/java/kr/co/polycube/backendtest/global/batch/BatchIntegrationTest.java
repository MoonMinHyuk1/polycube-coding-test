package kr.co.polycube.backendtest.global.batch;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.lotto.repository.LottoRepository;
import kr.co.polycube.backendtest.domain.winner.repository.WinnerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class BatchIntegrationTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    LottoRepository lottoRepository;
    @Autowired
    WinnerRepository winnerRepository;

    @Test
    @DisplayName("로또 배치 통합 테스트")
    public void lottoBatchTest() throws Exception {
        //given
        lottoRepository.save(Lotto.builder().numbers(List.of(1, 2, 3, 4, 5, 6)).build());
        lottoRepository.save(Lotto.builder().numbers(List.of(7, 8, 9, 10, 11, 12)).build());
        lottoRepository.save(Lotto.builder().numbers(List.of(13, 14, 15, 16, 17, 18)).build());

        //when
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(params);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(lottoRepository.findAll()).hasSize(3);
        assertThat(winnerRepository.findAll().size()).isLessThanOrEqualTo(3);
    }
}
