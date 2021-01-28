package com.epam.voucherifyintegrator.job;

import com.epam.voucherifyintegrator.service.VoucherifyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Objects;

public class UpdateLoyaltyProgramJob {
    private final VoucherifyService voucherifyService;

    Logger logger = LogManager.getLogger(UpdateLoyaltyProgramJob.class);

    public UpdateLoyaltyProgramJob(VoucherifyService voucherifyService) {
        this.voucherifyService = Objects.requireNonNull(voucherifyService, "Can't get promotionService");
    }

    @Scheduled(cron = "${job.cron.update.promotion}")
    public void runJob() {
        try {
            voucherifyService.updateCovidLoyaltyProgram();
        } catch (Exception ex) {
            logger.error("Can't update promotions!", ex);
        }
    }
}
