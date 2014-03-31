package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

/**
 * Created by marefyev on 3/31/14.
 */
public class VkResponse {
  @JsonProperty
  private ArrayList<VkUser> user = new ArrayList<VkUser>();
  @JsonProperty
  private ArrayList<VkCountry> countries = new ArrayList<VkCountry>();
  @JsonProperty
  private ArrayList<VkCity> cities = new ArrayList<VkCity>();

  public VkResponse(String uid) {
    this.user.add(new VkUser(uid));
    this.countries.add(new VkCountry());
    this.cities.add(new VkCity());
  }

  public VkResponse(VkUser user, VkCountry country, VkCity city) {
    this.user.add(user);
    this.countries.add(country);
    this.cities.add(city);
  }

  public ArrayList<VkUser> getUser() {
    return user;
  }

  public void setUser(VkUser user) {
    this.user.add(user);
  }

  public ArrayList<VkCountry> getCountries() {
    return countries;
  }

  public void setCountries(VkCountry countries) {
    this.countries.add(countries);
  }

  public ArrayList<VkCity> getCities() {
    return cities;
  }

  public void setCities(VkCity cities) {
    this.cities.add(cities);
  }

}
