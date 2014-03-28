package ru.hh.standalone.utils.oauth.ok_social_entity;

/**
 * User: alexandrdeshkevich
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
