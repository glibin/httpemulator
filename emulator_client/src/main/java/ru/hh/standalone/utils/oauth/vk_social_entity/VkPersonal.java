package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marefyev on 3/31/14.
 */
public class VkPersonal {
  @JsonProperty
  private ArrayList<String> langs = new ArrayList<String>();

  public VkPersonal() {
    langs = new ArrayList<String>(Arrays.asList("Русский", "Английский"));
  }

  public VkPersonal(ArrayList<String> langs) {
    this.langs = langs;
  }

  public ArrayList<String> getLangs() {
    return langs;
  }

  public void setLangs(ArrayList<String> langs) {
    this.langs = langs;
  }

  public void addLangs(String lang) {
    this.langs.add(lang);
  }

}
