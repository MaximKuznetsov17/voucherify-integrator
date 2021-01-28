package com.epam.voucherifyintegrator.client;

import com.epam.voucherifyintegrator.service.CovidService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class CovidApiClientTest {
    @Autowired
    private CovidApiClient covidApiClient;

    @Autowired
    private CovidService covidService;

    @Test
    public void covidRequestTest() {
        covidApiClient.getCountyCovidInfo("Italy", true);
    }

    @Test
    public void covidServiceTest() {
        Optional<Long> res1 = covidService.getTotalCasesByCountry("Russia");
        System.out.println(res1);
        Optional<Integer> res2 = covidService.getIncreaseOfIncidentsByCountry("Russia");
        System.out.println(res2);
        Optional<Integer> res3 = covidService.getIncreaseOfRecoveryByCountry("Russia");
        System.out.println(res3);
    }
}