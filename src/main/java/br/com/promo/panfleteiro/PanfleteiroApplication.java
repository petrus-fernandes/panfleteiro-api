package br.com.promo.panfleteiro;

import br.com.promo.panfleteiro.job.DeactivateAllProprietiesWithExpiratedDateJob;
import org.quartz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class PanfleteiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanfleteiroApplication.class, args);
	}

	@Bean
	public JobDetail validateExpirationDateDetail() {
		return JobBuilder.newJob(DeactivateAllProprietiesWithExpiratedDateJob.class)
				.withIdentity("adJob", "system")
				.storeDurably()
				.build();
	}

	@Bean
	public Trigger validateExpirationDateTrigger() {
		return TriggerBuilder.newTrigger()
				.forJob(validateExpirationDateDetail())
				.withIdentity("adTrigger", "system")
				.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(3, 0))
				.build();
	}

}
