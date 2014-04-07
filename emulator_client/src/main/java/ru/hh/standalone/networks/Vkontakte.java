package ru.hh.standalone.networks;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;
import ru.hh.standalone.utils.oauth.vk_social_entity.*;
import ru.hh.standalone.utils.oauth.*;

import java.util.Random;
import java.util.UUID;


/**
 * Created by marefyev on 3/31/14.
 */
public class Vkontakte {

    private String error = "ОШИБКА СОЗДАНИЯ ПОЛЬЗОВАТЕЛЯ: ";

    private String openthisPage;
    private String testHost; //stand address
    private String site;
    private VkPersonal vkPersonal;
    private String vkUserId; //VK id
    private String http_emulator_host; //http emulator address
    private String hhid_public_url;
    private ObjectMapper jsonMapper = new ObjectMapper();
    private EmulatorClient emulatorClient;
    private VkData vkData;
    private String codes[] = null;


    private String vkRequestId;
    private String vkCode;
    private String vkToken;

    private String cookieName = "OAUTH-REQUEST-ID";

    private long ruleID1;
    private long ruleID2;
    private long ruleID3;

    private boolean userCreated;

    public Vkontakte(String site, String testHostNameOnly, String http_emulator) {
        this.site = site;
        this.testHost = this.site + "." + testHostNameOnly + ".pyn.ru";

        this.vkPersonal = new VkPersonal();
        this.vkUserId = "1" + getNumber(10);
        this.http_emulator_host = http_emulator;
        this.openthisPage = "applicant/negotiations";
        this.hhid_public_url = "http://hhid." + testHostNameOnly + ".pyn.ru/";
        this.userCreated = false;
        setIDs();
        vkData = new VkData(this.vkUserId, this.vkPersonal);  // создаем данные пользователя вконтаке (sUid - id пользователя), vkPersonal - данные о языках
    }

    public Vkontakte(String site, String testHostNameOnly, String http_emulator, String first_name, String lastName, String codes[]) {
        this(site, testHostNameOnly, http_emulator);
        this.vkData.setFirstNameUser(first_name).setLastNameUser(lastName);
        this.codes = codes;
    }

    //for test purposes only
    public static void main(String args[]) throws Exception {
        Vkontakte vkontakt = new Vkontakte("hh.ru", "mercury", "http://jenkins.pyn.ru:18880");
        vkontakt.loginVk();
    }

    private void setIDs() {
        this.vkRequestId = "VKREQUESTID" + UUID.randomUUID() + "VKREQUESTID";
        this.vkCode = "VKCODE" + UUID.randomUUID() + "VKCODE";
        this.vkToken = "VKTOKEN" + UUID.randomUUID() + "VKTOKEN";
    }

    private static String getNumber(int length) {
        Random randomGenerator = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < length; i++) {
            buf.append(Integer.toString(randomGenerator.nextInt(10)));
        }

        return buf.toString();
    }


    public void loginVk() throws Exception {

        emulatorClient = new EmulatorClient(this.http_emulator_host);


        try {
            this.ruleID1 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.COOKIE, this.cookieName, vkRequestId)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_MOVED_TEMPORARILY): codes[0])
                    .addResponseEntry(AttributeType.HEADER, "Location", OAuthUtils.buildOAuthRedirrectURL(this.testHost, this.openthisPage, "VK", false, vkCode, "", "", this.hhid_public_url))
                    .save();

            this.ruleID2 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.PARAMETER, "code", vkCode)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[1])
                    .addResponseEntry(AttributeType.BODY, null, OAuthUtils.buildAccessTokenResponse(vkToken, this.vkUserId))
                    .save();

            this.ruleID3 = emulatorClient.createSimpleRule()
                    .addEQ(AttributeType.PARAMETER, "access_token", vkToken)
                    .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[2])
                    .addResponseEntry(AttributeType.BODY, null, jsonMapper.writeValueAsString(this.vkData))
                    .save();

            setIfTheUserCreated(true);
        } catch (IllegalStateException e) {
            error += e.getMessage();
        }

        emulatorClient.stopClient();

    }

    public String getVkRequestId() {
        if (getIfTheUserCreated()) {
            return this.vkRequestId;
        }
        else{
            return error;
        }
    }

    public String getVKID() {
        if (getIfTheUserCreated()) {
        return this.vkUserId;
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

}//end class
