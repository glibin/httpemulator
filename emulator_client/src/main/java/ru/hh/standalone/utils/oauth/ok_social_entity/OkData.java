package ru.hh.standalone.utils.oauth.ok_social_entity;

/**
 * Created by marefyev on 3/31/14.
 */
public class OkData {

    private OkUser user;

    public  OkData(String uid) {
        user = new OkUser(uid);
    }

    public void setUser(OkUser user) {
        this.user = user;
    }

    public OkUser getUser() {
        return this.user;
    }
}
