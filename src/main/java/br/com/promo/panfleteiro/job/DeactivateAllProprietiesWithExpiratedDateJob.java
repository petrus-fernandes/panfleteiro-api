package br.com.promo.panfleteiro.job;

import br.com.promo.panfleteiro.orchestrator.FlyerOrchestrator;
import br.com.promo.panfleteiro.service.AdService;
import br.com.promo.panfleteiro.service.FlyerSectionService;
import br.com.promo.panfleteiro.service.FlyerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DeactivateAllProprietiesWithExpiratedDateJob implements Job {

    FlyerOrchestrator flyerOrchestrator;

    private static final Logger logger = LoggerFactory.getLogger(DeactivateAllProprietiesWithExpiratedDateJob.class);
    public DeactivateAllProprietiesWithExpiratedDateJob(FlyerOrchestrator flyerOrchestrator) {
        this.flyerOrchestrator = flyerOrchestrator;
    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Initiate deactivation of Ads, FlyerSections and Flyers expirated.");
        flyerOrchestrator.deactivateEntitiesByExpiratedDate();
        logger.info("Deactivation of Ads, FlyerSections and Flyers expirated completed.");
    }
}
