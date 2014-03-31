package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;


/**
 * Created by marefyev on 3/31/14.
 */

public class VkData {
  @JsonProperty
  private VkResponse response;

  public VkData(String uid) {
    this.response = new VkResponse(uid);
  }

  public VkData(String uid, VkPersonal vkPersonal) {
    VkUser vkUser = new VkUser(uid);
    vkUser.setPersonal(vkPersonal);
    this.response = new VkResponse(vkUser, new VkCountry(), new VkCity());
  }

  public void setResponse(VkResponse response) {
    this.response = response;
  }

  public VkResponse getResponse() {
    return this.response;
  }

  public String getFirstNameUser() {
    return this.response.getUser().get(0).getFirst_name();
  }

  public String getLastNameUser() {
    return this.response.getUser().get(0).getLast_name();
  }

  public String getFullNameUser() {
    return this.response.getUser().get(0).getFullName();
  }

  public String getUniversityNameUser() {
    return this.response.getUser().get(0).getUniversities().get(0).getName();
  }

  public String getFacultyUniversityNameUser() {
    return this.response.getUser().get(0).getUniversities().get(0).getFaculty_name();
  }

  public String getCityLocalityNameUser() {
    return this.response.getCities().get(0).getName();
  }

  public String getBirthDayUser() {
    return this.response.getUser().get(0).getBdate();
  }

  public String getSexUser() {
    return this.response.getUser().get(0).getSex().equals("1") ? "Женский" : "Мужской";
  }

  public ArrayList<String> getLanguagesUser() {
    return this.response.getUser().get(0).getPersonal().getLangs();
  }
}
