package com.epam.voucherifyintegrator.controller;

import com.epam.voucherifyintegrator.job.UpdateLoyaltyProgramJob;
import com.epam.voucherifyintegrator.model.CustomResponse;
import com.epam.voucherifyintegrator.model.ResponseStatus;
import com.epam.voucherifyintegrator.service.CovidService;
import com.epam.voucherifyintegrator.service.VoucherifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoucherifyController {
    private final VoucherifyService voucherifyService;
    private final CovidService covidService;
    private final UpdateLoyaltyProgramJob updateLoyaltyProgramJob;

    public VoucherifyController(VoucherifyService voucherifyService, CovidService covidService,
                                UpdateLoyaltyProgramJob updateLoyaltyProgramJob) {
        this.voucherifyService = voucherifyService;
        this.covidService = covidService;
        this.updateLoyaltyProgramJob = updateLoyaltyProgramJob;
    }

    @GetMapping("/")
    public String start() {
        return "Server started up successfully!";
    }

    @GetMapping("/customer/points")
    @ResponseBody
    public CustomResponse getCustomerPoints(@RequestParam String id) {
        Integer res = voucherifyService.getPointsByCustomer(id);
        return new CustomResponse(ResponseStatus.SUCCESS, "", res);
    }

    @GetMapping("/covid/sickness/rate")
    @ResponseBody
    public CustomResponse getIncreaseOfIncidentsByCountry(@RequestParam String country) {
        Optional<Integer> res = covidService.getIncreaseOfIncidentsByCountry(country);
        return res.map(aDouble -> new CustomResponse(ResponseStatus.SUCCESS, "", aDouble))
                .orElseGet(() -> new CustomResponse(ResponseStatus.FAILED, "Can't get percent of increasing sickness rate", null));
    }

    @GetMapping("/covid/recovery/rate")
    @ResponseBody
    public CustomResponse getIncreaseOfRecoveryByCountry(@RequestParam String country) {
        Optional<Integer> res = covidService.getIncreaseOfRecoveryByCountry(country);
        return res.map(val -> new CustomResponse(ResponseStatus.SUCCESS, "", val))
                .orElseGet(() -> new CustomResponse(ResponseStatus.FAILED, "Can't get percent of increasing recovery rate", null));
    }

    @GetMapping("/covid/total/case")
    @ResponseBody
    public CustomResponse getTotalCasesByCountry(@RequestParam String country) {
        Optional<Long> res = covidService.getTotalCasesByCountry(country);
        return res.map(val -> new CustomResponse(ResponseStatus.SUCCESS, "", val))
                .orElseGet(() -> new CustomResponse(ResponseStatus.FAILED, "Can't get total cases count", null));
    }

    @GetMapping("/update/loyalty/program/job/start")
    @ResponseBody
    public String startUpdateLoyaltyProgramJob() {
        updateLoyaltyProgramJob.runJob();
        return ResponseStatus.SUCCESS.getStatus();
    }

    @GetMapping("/get/sum/earn/points")
    @ResponseBody
    public CustomResponse getSumOfPoints() {
        Integer res = voucherifyService.getSumOfPoints();
        return new CustomResponse(ResponseStatus.SUCCESS, "", res);
    }
}
