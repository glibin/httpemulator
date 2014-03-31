package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by marefyev on 3/31/14.
 */
public class VkUniversity {
  @JsonProperty
  private String id;
  @JsonProperty
  private String country;
  @JsonProperty
  private String city;
  @JsonProperty
  private String name;
  @JsonProperty
  private String faculty;
  @JsonProperty
  private String faculty_name;
  @JsonProperty
  private String chair;
  @JsonProperty
  private String chair_name;
  @JsonProperty
  private String graduation;
  @JsonProperty
  private String education_form;
  @JsonProperty
  private String education_status;

  public VkUniversity() {
    this.id = "239";
    this.country = "1";
    this.city = "1386";
    this.name = "БГУ";
    this.faculty = "959";
    this.faculty_name = "Факультет прикладных исскуств";
    this.chair = "18134";
    this.chair_name = "Изобразительное исскуство";
    this.graduation = "2010";
    this.education_form = "Дневное отделение";
    this.education_status = "Выпускник (специалист)";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFaculty() {
    return faculty;
  }

  public void setFaculty(String faculty) {
    this.faculty = faculty;
  }

  public String getFaculty_name() {
    return faculty_name;
  }

  public void setFaculty_name(String faculty_name) {
    this.faculty_name = faculty_name;
  }

  public String getChair() {
    return chair;
  }

  public void setChair(String chair) {
    this.chair = chair;
  }

  public String getChair_name() {
    return chair_name;
  }

  public void setChair_name(String chair_name) {
    this.chair_name = chair_name;
  }

  public String getGraduation() {
    return graduation;
  }

  public void setGraduation(String graduation) {
    this.graduation = graduation;
  }

  public String getEducation_form() {
    return education_form;
  }

  public void setEducation_form(String education_form) {
    this.education_form = education_form;
  }

  public String getEducation_status() {
    return education_status;
  }

  public void setEducation_status(String education_status) {
    this.education_status = education_status;
  }
}
