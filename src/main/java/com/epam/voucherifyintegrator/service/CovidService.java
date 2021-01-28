package com.epam.voucherifyintegrator.service;

import com.epam.voucherifyintegrator.client.CovidApiClient;
import com.epam.voucherifyintegrator.job.UpdateLoyaltyProgramJob;
import com.epam.voucherifyintegrator.model.CovidData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

public class CovidService {
    private final CovidApiClient covidClient;

    Logger logger = LogManager.getLogger(CovidService.class);


    public CovidService(CovidApiClient covidClient) {
        this.covidClient = Objects.requireNonNull(covidClient, "Can't get covidClient");
    }

    public Optional<Long> getTotalCasesByCountry(String country) {
        try {
            Optional<CovidData> data = covidClient.getCountyCovidInfo(country, false);
            return data.map(CovidData::getCases);
        } catch (Exception ex) {
            logger.error("Can't get total cases by country = {}", country, ex);
            return Optional.empty();
        }
    }

    public Optional<Integer> getIncreaseOfIncidentsByCountry(String country) {
        try {
            Optional<CovidData> todayData = covidClient.getCountyCovidInfo(country, false);
            Optional<CovidData> yesterdayData = covidClient.getCountyCovidInfo(country, true);
            if (todayData.isPresent() && yesterdayData.isPresent()) {
                return getIncreasePercent(todayData.get().getTodayCases(), yesterdayData.get().getTodayCases());
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            logger.error("Can't get increase of incidents by country = {}", country, ex);
            return Optional.empty();
        }
    }

    public Optional<Integer> getIncreaseOfRecoveryByCountry(String country) {
        try {
            Optional<CovidData> todayData = covidClient.getCountyCovidInfo(country, false);
            Optional<CovidData> yesterdayData = covidClient.getCountyCovidInfo(country, true);
            if (todayData.isPresent() && yesterdayData.isPresent()) {
                return getIncreasePercent(todayData.get().getTodayRecovered(), yesterdayData.get().getTodayRecovered());
            } else {
                logger.warn("Some of COVID data is null");
                return Optional.empty();
            }
        } catch (Exception ex) {
            logger.error("Can't get increase of recovery by country = {}", country, ex);
            return Optional.empty();
        }
    }

    private Optional<Integer> getIncreasePercent(Long now, Long before) {
        if (now == null || now == 0 || before == null || before == 0) {
            return Optional.empty();
        }
        double value = (double) now / (double) before - 1;
        double scale = Math.pow(10, 2);
        return Optional.of((int) Math.ceil(value * scale));
    }
}
