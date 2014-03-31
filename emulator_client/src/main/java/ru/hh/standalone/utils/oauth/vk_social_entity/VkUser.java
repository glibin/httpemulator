package ru.hh.standalone.utils.oauth.vk_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateMidnight;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by marefyev on 3/31/14.
 */
public class VkUser {
    private static long lastTimeStamp = System.currentTimeMillis();

    @JsonProperty
    private String uid; // уникальный id
    @JsonProperty
    private String first_name;
    @JsonProperty
    private String last_name;
    @JsonProperty
    private String sex;  // 1 жен, 2 муж
    @JsonProperty
    private String bdate;  // дата роджения (строка формата dd.MM.yyyy)
    @JsonProperty
    private String city;   // здесь id такое же должно уйти и в VkCity в cid
    @JsonProperty
    private String country;  // здесь id такое же должно уйти и в VkCountry в cid
    @JsonProperty
    private VkPersonal personal;   // языки
    @JsonProperty
    private ArrayList<VkUniversity> universities = new ArrayList<VkUniversity>();  // учеба

    public VkUser(String uid) {
        this.uid = uid;
        this.first_name = "Fn" + getUnique().toLowerCase();
        this.last_name = "Ln" + getUnique().toLowerCase();
        this.sex = "Male";//TODO can be a problem
        this.bdate = new SimpleDateFormat("dd.MM.yyyy").format(new DateMidnight(1990, 2, 10).toDate());
        this.city = "1386";
        this.country = "1";
        this.personal = new VkPersonal();
        this.universities.add(new VkUniversity());
    }

    public synchronized static String getUnique() {
        long currentTimeStamp = System.currentTimeMillis();
        if (lastTimeStamp < currentTimeStamp) {
            lastTimeStamp = currentTimeStamp;
        } else {
            lastTimeStamp += 1;
        }
        final String sUnique = String.valueOf(lastTimeStamp) + String.valueOf(Math.round(Math.random() * 1000));
        char[] chars = sUnique.toCharArray();

        for (int i = 0; i < sUnique.length(); i++) {
            chars[i] += 'A' - '0';
        }

        return new String(chars);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public VkPersonal getPersonal() {
        return personal;
    }

    public void setPersonal(VkPersonal personal) {
        this.personal = personal;
    }

    public ArrayList<VkUniversity> getUniversities() {
        return this.universities;
    }

    public void setUniversities(VkUniversity uni) {
        this.universities.add(uni);
    }

    public String getFullName() {
        return getLast_name() + " " + getFirst_name();
    }
}

