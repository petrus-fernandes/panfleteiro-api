package br.com.promo.panfleteiro.job;

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

    AdService adService;

    FlyerService flyerService;

    FlyerSectionService flyerSectionService;

    private static final Logger logger = LoggerFactory.getLogger(DeactivateAllProprietiesWithExpiratedDateJob.class);
    public DeactivateAllProprietiesWithExpiratedDateJob(AdService adService, FlyerService flyerService, FlyerSectionService flyerSectionService) {
        this.adService = adService;
        this.flyerService = flyerService;
        this.flyerSectionService = flyerSectionService;
    }
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Initiate deactivation of Ads, FlyerSections and Flyers expirated.");
        adService.getActiveAds().stream().filter(ad -> ad.getFlyerSection() != null && isExpirated(ad.getFlyerSection().getExpirationDate())).forEach(ad -> {
                ad.setActive(false);
                ad.getFlyerSection().setActive(false);
                adService.saveAd(ad);
                flyerSectionService.save(ad.getFlyerSection());
                logger.info("Ad: " + ad.getId() + " expirated.");
                logger.info("FlyerSection: " + ad.getFlyerSection().getId() + " expirated.");
        });
        flyerService.getActiveFlyers().stream().filter(flyer -> isExpirated(flyer.getExpirationDate())).forEach(flyer -> {
            flyer.setActive(false);
            flyerService.save(flyer);
            logger.info("Flyer: " + flyer.getId() + " expirated.");
        });
        logger.info("Deactivation of Ads, FlyerSections and Flyers expirated completed.");
    }

    public boolean isExpirated(Date expirationDate) {
        return expirationDate.before(new Date());
    }
}
