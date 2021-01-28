package com.epam.voucherifyintegrator.configuration;

import com.epam.voucherifyintegrator.client.CovidApiClient;
import com.epam.voucherifyintegrator.service.CovidService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:covid-api.properties")
public class CovidConfiguration {
    @Value("${covid.api.base.url}")
    private String baseUrl;
    @Value("${covid.api.connection.timeout}")
    private Integer connectionTimeout;

    @Bean
    CovidApiClient covidApiClient() {
        return new CovidApiClient(baseUrl, connectionTimeout);
    }

    @Bean
    CovidService covidService(CovidApiClient covidApiClient) {
        return new CovidService(covidApiClient);
    }
}
