package com.epam.voucherifyintegrator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidData {
    @JsonProperty("updated")
    private final BigDecimal updated;
    @JsonProperty("country")
    private final String country;
    @JsonProperty("countryInfo")
    private final CountryInfo countryInfo;
    @JsonProperty("cases")
    private final Long cases;
    @JsonProperty("todayCases")
    private final Long todayCases;
    @JsonProperty("deaths")
    private final Long deaths;
    @JsonProperty("todayDeaths")
    private final Long todayDeaths;
    @JsonProperty("todayRecovered")
    private final Long todayRecovered;
    @JsonProperty("recovered")
    private final Long recovered;
    @JsonProperty("active")
    private final Long active;
    @JsonProperty("critical")
    private final Long critical;
    @JsonProperty("casesPerOneMillion")
    private final Long casesPerOneMillion;
    @JsonProperty("deathsPerOneMillion")
    private final Long deathsPerOneMillion;
    @JsonProperty("tests")
    private final Long tests;
    @JsonProperty("testsPerOneMillion")
    private final Long testsPerOneMillion;

    @JsonCreator
    public CovidData(
            @JsonProperty("updated") BigDecimal updated,
            @JsonProperty("country") String country,
            @JsonProperty("countryInfo") CountryInfo countryInfo,
            @JsonProperty("cases") Long cases,
            @JsonProperty("todayCases") Long todayCases,
            @JsonProperty("deaths") Long deaths,
            @JsonProperty("todayDeaths") Long todayDeaths,
            @JsonProperty("todayRecovered") Long todayRecovered,
            @JsonProperty("recovered") Long recovered,
            @JsonProperty("active") Long active,
            @JsonProperty("critical") Long critical,
            @JsonProperty("casesPerOneMillion") Long casesPerOneMillion,
            @JsonProperty("deathsPerOneMillion") Long deathsPerOneMillion,
            @JsonProperty("tests") Long tests,
            @JsonProperty("testsPerOneMillion") Long testsPerOneMillion) {
        this.updated = updated;
        this.country = country;
        this.countryInfo = countryInfo;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.todayRecovered = todayRecovered;
        this.recovered = recovered;
        this.active = active;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
        this.deathsPerOneMillion = deathsPerOneMillion;
        this.tests = tests;
        this.testsPerOneMillion = testsPerOneMillion;
    }

    public BigDecimal getUpdated() {
        return updated;
    }

    public String getCountry() {
        return country;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public Long getCases() {
        return cases;
    }

    public Long getTodayCases() {
        return todayCases;
    }

    public Long getDeaths() {
        return deaths;
    }

    public Long getTodayDeaths() {
        return todayDeaths;
    }

    public Long getRecovered() {
        return recovered;
    }

    public Long getActive() {
        return active;
    }

    public Long getCritical() {
        return critical;
    }

    public Long getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public Long getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }

    public Long getTests() {
        return tests;
    }

    public Long getTestsPerOneMillion() {
        return testsPerOneMillion;
    }

    public Long getTodayRecovered() {
        return todayRecovered;
    }
}
