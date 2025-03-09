package kr.co.polycube.backendtest.global.batch.config;

import kr.co.polycube.backendtest.domain.lotto.entity.Lotto;
import kr.co.polycube.backendtest.domain.lotto.repository.LottoRepository;
import kr.co.polycube.backendtest.domain.winner.entity.Winner;
import kr.co.polycube.backendtest.global.batch.processor.LottoItemProcessor;
import kr.co.polycube.backendtest.global.batch.reader.LottoItemReader;
import kr.co.polycube.backendtest.global.batch.writer.LottoItemWriter;
import kr.co.polycube.backendtest.global.util.LottoNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class LottoBatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final LottoItemReader lottoItemReader;
    private final LottoItemWriter lottoItemWriter;

    @Bean
    public Job lottoJob() {
        return new JobBuilder("lottoJob", jobRepository)
                .start(lottoStep())
                .build();
    }

    @Bean
    public Step lottoStep() {
        List<Integer> winningNumbers = LottoNumberGenerator.generateLottoNumbers();
        LottoItemProcessor lottoItemProcessor = new LottoItemProcessor(winningNumbers);
        return new StepBuilder("lottoStep", jobRepository)
                .<Lotto, Winner>chunk(10, transactionManager)
                .reader(lottoItemReader)
                .processor(lottoItemProcessor)
                .writer(lottoItemWriter)
                .build();
    }
}
