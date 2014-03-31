package ru.hh.standalone.utils.oauth.ok_social_entity;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateMidnight;


import java.text.SimpleDateFormat;

/**
 * Created by marefyev on 3/31/14.
 */
public class OkUser {
    private static long lastTimeStamp = System.currentTimeMillis();
    @JsonProperty
    private String uid; // уникальный id
    @JsonProperty
    private String birthday;  // дата роджения (строка формата dd-MM-yyyy)
    @JsonProperty
    private int age;
    @JsonProperty
    private String first_name;
    @JsonProperty
    private String last_name;
    @JsonProperty
    private String name;
    @JsonProperty
    private String locale;
    @JsonProperty
    private String gender;
    @JsonProperty
    private Boolean has_email;
    @JsonProperty
    private OkLocation location;
    @JsonProperty
    private String online;
    @JsonProperty
    private String photo_id;
    @JsonProperty
    private String pic_1;
    @JsonProperty
    private String pic_2;
    @JsonProperty
    private String email;


    public OkUser(String uid) {
        this.uid = uid;
        this.birthday = new SimpleDateFormat("dd-MM-yyyy").format(new DateMidnight(1990, 2, 10).toDate());
        this.age = 24;
        this.first_name = "Fn" + getUnique().toLowerCase();
        this.last_name = "Ln" + getUnique().toLowerCase();
        this.name = getUnique().toLowerCase();
        this.locale = "ru";
        this.gender = "male";
        this.has_email = Boolean.TRUE;
        this.location = new OkLocation();
        this.online = "web";
        this.photo_id = "891584623";
        this.pic_1 = "http://i500.mycdn.me/getImage?photoId=891584623&photoType=4&viewToken=rvpZfi6YG671x0TB6ncxjw";
        this.pic_2 = "http://usd1.mycdn.me/getImage?photoId=891584623&photoType=2&viewToken=rvpZfi6YG671x0TB6ncxjw";
        this.email = uid + "@odnoklassniki.ru";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge (int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPic_1() {
        return pic_1;
    }

    public void setPic_1(String pic_1) {
        this.pic_1 = pic_1;
    }

    public String getPic_2() {
        return pic_2;
    }

    public void setPic_2(String pic_2) {
        this.pic_2 = pic_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHas_email() {
        return has_email;
    }

    public void setHas_email(Boolean has_email) {
        this.has_email = has_email;
    }

    public OkLocation getLocation() {
        return location;
    }

    public void setLocation(OkLocation location) {
        this.location = location;
    }
}
