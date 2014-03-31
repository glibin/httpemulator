package ru.hh.standalone.utils.oauth.ok_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by marefyev on 3/31/14.
 */
public class OkLocation {

    @JsonProperty
    private String countryCode;
    @JsonProperty
    private String country;
    @JsonProperty
    private String city;

    public OkLocation() {
        this.countryCode = "RU";
        this.country = "RUSSIAN_FEDERATION";
        this.city = "Москва";
    }

    public String getСountryCode() {
        return countryCode;
    }

    public void setСountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getСountry() {
        return country;
    }

    public void setСountry(String country) {
        this.country = country;
    }

    public String getСity() {
        return city;
    }

    public void setСity(String city) {
        this.city = city;
    }
}
