package com.epam.voucherifyintegrator.configuration;

import com.epam.voucherifyintegrator.job.UpdateLoyaltyProgramJob;
import com.epam.voucherifyintegrator.service.VoucherifyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({VoucherifyConfiguration.class})
public class ApplicationConfiguration {
    @Bean
    UpdateLoyaltyProgramJob updatePromotionJob(VoucherifyService voucherifyService) {
        return new UpdateLoyaltyProgramJob(voucherifyService);
    }
}
