package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by marefyev on 3/31/14.
 */
public class VkCountry {
  @JsonProperty
  private String cid;
  @JsonProperty
  private String name;

  public VkCountry() {
    this.cid = "1";
    this.name = "Россия";
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
