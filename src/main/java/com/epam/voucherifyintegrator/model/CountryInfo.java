package com.epam.voucherifyintegrator.model;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryInfo {
    @JsonProperty("_id")
    private final Integer id;
    @JsonProperty("iso2")
    private final String iso2;
    @JsonProperty("iso3")
    private final String iso3;
    @JsonProperty("lat")
    private final Float lat;
    @JsonProperty("long")
    private final Float _long;
    @JsonProperty("flag")
    private final String flag;

    @JsonCreator
    public CountryInfo(
            @JsonProperty("_id") Integer id,
            @JsonProperty("iso2") String iso2,
            @JsonProperty("iso3") String iso3,
            @JsonProperty("lat") Float lat,
            @JsonProperty("long") Float _long,
            @JsonProperty("flag") String flag) {
        this.id = id;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.lat = lat;
        this._long = _long;
        this.flag = flag;
    }

    public Integer getId() {
        return id;
    }

    public String getIso2() {
        return iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public Float getLat() {
        return lat;
    }

    public Float get_long() {
        return _long;
    }

    public String getFlag() {
        return flag;
    }
}
