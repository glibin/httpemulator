package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * User: dsobol
 */
public class VkCity {
  @JsonProperty
  private String cid;
  @JsonProperty
  private String name;

  public VkCity() {
    this.cid = "1386";
    this.name = "Москва";
  }

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
