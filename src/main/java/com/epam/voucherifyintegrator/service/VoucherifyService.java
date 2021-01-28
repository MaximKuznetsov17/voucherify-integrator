package com.epam.voucherifyintegrator.service;

import com.epam.voucherifyintegrator.client.CovidApiClient;
import com.epam.voucherifyintegrator.client.MyVoucherifyClient;
import io.voucherify.client.model.campaign.response.CampaignResponse;
import io.voucherify.client.model.customer.response.CustomerResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Objects;
import java.util.Optional;

public class VoucherifyService {
    private final MyVoucherifyClient myVoucherifyClient;
    private final CovidService covidService;

    Logger logger = LogManager.getLogger(VoucherifyService.class);

    private static final String COVID_CAMPAIGN = "COVID Loyalty program";
    private static final String DEFAULT_COUNTRY = "Russia";

    public VoucherifyService(MyVoucherifyClient myVoucherifyClient, CovidService covidService) {
        this.myVoucherifyClient = Objects.requireNonNull(myVoucherifyClient, "Can't get voucherifyClient");
        this.covidService = Objects.requireNonNull(covidService, "Can't get covidService");
    }

    public void updateCovidLoyaltyProgram() {
        try {
            Optional<CampaignResponse> campaign = myVoucherifyClient.getCampaignByName(COVID_CAMPAIGN);
            if (campaign.isPresent()) {
                updateLoyaltyProgram(campaign.get());
            } else {
                createLoyaltyProgram();
            }
        } catch (Exception ex) {
            logger.error("Can't update or create COVID Loyalty program", ex);
        }
    }

    private void updateLoyaltyProgram(CampaignResponse campaign) {
        try {
            Optional<Integer> percent = covidService.getIncreaseOfIncidentsByCountry(DEFAULT_COUNTRY);
            Double averageSum = myVoucherifyClient.getYesterdayAverageOrderSum();
            percent.ifPresent(val -> {
                Integer point = averageSum.intValue() * val / 100;
                myVoucherifyClient.updateLoyaltyProgramProperties(campaign, point);
            });
        } catch (Exception ex) {
            logger.error("Can't update COVID Loyalty program", ex);
        }
    }

    private void createLoyaltyProgram() {
        try {
            Optional<Integer> percent = covidService.getIncreaseOfIncidentsByCountry(DEFAULT_COUNTRY);
            percent.ifPresent(myVoucherifyClient::createLoyaltyProgram);
        } catch (Exception ex) {
            logger.error("Can't create COVID Loyalty program", ex);
        }
    }

    public Integer getSumOfPoints() {
        try {
            return myVoucherifyClient.getSumOfPoints();
        } catch (Exception ex) {
            logger.error("Can't get sum of points by COVID program", ex);
            return null;
        }
    }

    public Integer getPointsByCustomer(String sourceId) {
        try {
            Optional<CustomerResponse> customer = myVoucherifyClient.getCustomerBySourceId(sourceId);
            return customer.map(customerResponse ->
                    myVoucherifyClient
                            .getPointsByCustomer(customerResponse)
                            .orElse(0)
            ).orElse(0);
        } catch (Exception ex) {
            logger.error("Can't get points by customer with id ={}", sourceId, ex);
            return 0;
        }
    }
}
