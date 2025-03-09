package kr.co.polycube.backendtest.global.batch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LottoBatchScheduler {

    private final JobLauncher jobLauncher;

    private final Job lottoJob;

    public LottoBatchScheduler(JobLauncher jobLauncher, @Qualifier("lottoJob") Job lottoJob) {
        this.jobLauncher = jobLauncher;
        this.lottoJob = lottoJob;
    }

    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void runLottoJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(lottoJob, params);
        } catch (Exception e) {
            log.error("배치 실행 중 문제가 발생했습니다.");
        }
    }
}
