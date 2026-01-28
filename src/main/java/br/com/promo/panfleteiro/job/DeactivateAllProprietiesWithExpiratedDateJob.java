package br.com.promo.panfleteiro.job;

import br.com.promo.panfleteiro.v1.ad.AdMarketHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DeactivateAllProprietiesWithExpiratedDateJob implements Job {

    @Autowired
    private AdMarketHelper adMarketHelper;

    private static final Logger logger = LoggerFactory.getLogger(DeactivateAllProprietiesWithExpiratedDateJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Initiate deactivation of expirated Ads expirated.");
        adMarketHelper.deactivateEntitiesByExpiratedDate();
        logger.info("Deactivation of Ads completed.");
    }
}
