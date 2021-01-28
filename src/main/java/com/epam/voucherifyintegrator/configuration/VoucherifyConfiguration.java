package com.epam.voucherifyintegrator.configuration;

import com.epam.voucherifyintegrator.client.CovidApiClient;
import com.epam.voucherifyintegrator.client.MyVoucherifyClient;
import com.epam.voucherifyintegrator.service.CovidService;
import com.epam.voucherifyintegrator.service.VoucherifyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CovidConfiguration.class)
public class VoucherifyConfiguration {
    @Value("${voucherify.secret.key}")
    private String secretKey;
    @Value("${voucherify.application.id}")
    private String applicationId;

    @Bean
    MyVoucherifyClient myVoucherifyClient() {
        return new MyVoucherifyClient(secretKey, applicationId);
    }

    @Bean
    VoucherifyService voucherifyService(MyVoucherifyClient myVoucherifyClient, CovidService covidService) {
        return new VoucherifyService(myVoucherifyClient, covidService);
    }
}
