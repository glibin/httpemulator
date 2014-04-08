package ru.hh.standalone.networks;


import org.apache.http.HttpStatus;
import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;
import ru.hh.standalone.utils.oauth.OAuthUtils;
import ru.hh.standalone.utils.oauth.fb_social_entity.*;


import java.util.Random;
import java.util.UUID;

/**
 * Created by marefyev on 4/7/14.
 */
public class FaceBook {
    private String error = "ОШИБКА СОЗДАНИЯ ПОЛЬЗОВАТЕЛЯ: ";

    private String openthisPage;
    private String testHost; //stand address
    private String site;
    private String fbUserId; //VK id
    private String http_emulator_host; //http emulator address
    private String hhid_public_url;
    private EmulatorClient emulatorClient;
    private FbData fbData;
    private String codes[] = null;


    private String fbRequestId;
    private String fbCode;
    private String fbToken;

    private String cookieName = "OAUTH-REQUEST-ID";

    private long ruleID1;
    private long ruleID2;
    private long ruleID3;

    private boolean userCreated;

    public FaceBook(String site, String testHostNameOnly, String http_emulator) {
        this.site = site;
        this.testHost = this.site + "." + testHostNameOnly + ".pyn.ru";

        this.fbUserId = "1" + getNumber(9);
        this.http_emulator_host = http_emulator;
        this.openthisPage = "applicant/negotiations";
        this.hhid_public_url = "http://hhid." + testHostNameOnly + ".pyn.ru/";
        this.userCreated = false;
        setIDs();
        fbData = new FbData(this.fbUserId);  // создаем данные пользователя вконтаке (sUid - id пользователя), vkPersonal - данные о языках
    }

    public FaceBook(String site, String testHostNameOnly, String http_emulator, String first_name, String lastName, String codes[]) {
        this(site, testHostNameOnly, http_emulator);
        this.fbData.setFirstName(first_name).setLastName(lastName);
        this.codes = codes;
    }

    //for test purposes only
    public static void main(String args[]) throws Exception {
        FaceBook face = new FaceBook("hh.ru", "mercury", "http://jenkins.pyn.ru:18880");
        face.loginFb();
    }

    private void setIDs() {
        this.fbRequestId = "FBREQUESTID" + UUID.randomUUID() + "FBREQUESTID";
        this.fbCode = "FBCODE" + UUID.randomUUID() + "FBCODE";
        this.fbToken = "FBTOKEN" + UUID.randomUUID() + "FBTOKEN";
    }

    private static String getNumber(int length) {
        Random randomGenerator = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < length; i++) {
            buf.append(Integer.toString(randomGenerator.nextInt(10)));
        }

        return buf.toString();
    }


    public void loginFb() throws Exception {

        emulatorClient = new EmulatorClient(this.http_emulator_host);


        try {
            this.ruleID1 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.COOKIE, this.cookieName, fbRequestId)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_MOVED_TEMPORARILY): codes[0])
                    .addResponseEntry(AttributeType.HEADER, "Location", OAuthUtils.buildOAuthRedirrectURL(this.testHost, this.openthisPage, "FB", false, fbCode, "", "", this.hhid_public_url))
                    .save();

            this.ruleID2 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.PARAMETER, "code", fbCode)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[1])
                    .addResponseEntry(AttributeType.BODY, null, OAuthUtils.fbAccessToken(fbToken))
                    .save();


            this.ruleID3 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.PARAMETER, "access_token", fbToken)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[2])
                    .addResponseEntry(AttributeType.BODY, null, this.fbData.toString())
                    .save();

            setIfTheUserCreated(true);
        } catch (IllegalStateException e) {
            error += e.getMessage();//Борода с ошибками если 409 - конфликт правил, одинаковые имена параметра и токены
        }

        emulatorClient.stopClient();

    }

    public String getFbRequestId() {
        if (getIfTheUserCreated()) {
            return this.fbRequestId;
        }
        else{
            return error;
        }
    }

    public String getFBID() {
        if (getIfTheUserCreated()) {
            return this.fbUserId;
        }
        else{
            return error;
        }
    }

    public String getRuleIDs() {
        if (getIfTheUserCreated()) {
            return Long.toString(this.ruleID1) + ", " + Long.toString(this.ruleID2) + ", " + Long.toString(this.ruleID3);
        }
        else{
            return error;
        }
    }

    private boolean getIfTheUserCreated() {
        return userCreated;
    }

    private void setIfTheUserCreated(boolean _created) {
        this.userCreated = _created;
    }

}
