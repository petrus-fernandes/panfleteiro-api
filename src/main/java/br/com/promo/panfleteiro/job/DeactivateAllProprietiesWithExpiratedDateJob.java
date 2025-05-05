package br.com.promo.panfleteiro.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DeactivateAllProprietiesWithExpiratedDateJob implements Job {

//    @Autowired
//    FlyerOrchestrator flyerOrchestrator;

    private static final Logger logger = LoggerFactory.getLogger(DeactivateAllProprietiesWithExpiratedDateJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Initiate deactivation of Ads, FlyerSections and Flyers expirated.");
//        flyerOrchestrator.deactivateEntitiesByExpiratedDate();
        logger.info("Deactivation of Ads, FlyerSections and Flyers expirated completed.");
    }
}
