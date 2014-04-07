package ru.hh.standalone.networks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;
import ru.hh.standalone.utils.oauth.ok_social_entity.*;
import ru.hh.standalone.utils.oauth.*;

import java.util.Random;
import java.util.UUID;

/**
 * Created by marefyev on 3/31/14.
 */
public class Odnoglazniki {

    private String error = "ОШИБКА СОЗДАНИЯ ПОЛЬЗОВАТЕЛЯ: ";

    private long ruleID1;
    private long ruleID2;
    private long ruleID3;


    private String okRequestId;
    private String okCode;
    private String okToken;

    private OkData okData;

    private ObjectMapper jsonMapper = new ObjectMapper();
    private EmulatorClient emulatorClient;
    private String hhid_public_url;

    private String testHost;
    private String okUserId;
    private String http_emulator_host;
    private String site;

    private String openthisPage;
    private boolean userCreated;
    private String codes[] = null;

    public static void main(String args[]) throws Exception {

        Odnoglazniki odnoglazniki= new Odnoglazniki("hh.ru", "mercury", "http://jenkins.pyn.ru:18880");

        odnoglazniki.loginOk();
    }


    public Odnoglazniki(String site, String testHostNameOnly, String http_emulator) {
        this.site = site;
        this.testHost = this.site + "." + testHostNameOnly + ".pyn.ru";
        this.okUserId = "1" + getNumber(10);
        this.openthisPage = "applicant/negotiations";
        this.http_emulator_host = http_emulator;
        this.hhid_public_url = "http://hhid." + testHostNameOnly + ".pyn.ru/";
        this.userCreated = false;
        setIDs();
        okData = new OkData(this.okUserId);  // создаем данные пользователя OK (sUid - id пользователя)
    }

    public Odnoglazniki(String site, String testHostNameOnly, String http_emulator, String first_name, String lastName, String codes[]) {
        this(site, testHostNameOnly, http_emulator);
        this.okData.getUser().setFirst_name(first_name).setLast_name(lastName);
        this.codes = codes;
    }



    private void setIDs() {
        this.okRequestId = "OKREQUESTID" + UUID.randomUUID() + "OKREQUESTID";
        this.okCode = "OKCODE" + UUID.randomUUID() + "OKCODE";
        this.okToken = "OKTOKEN" + UUID.randomUUID() + "OKTOKEN";
    }

    public static String getNumber(int length) {
        Random randomGenerator = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < length; i++) {
            buf.append(Integer.toString(randomGenerator.nextInt(10)));
        }

        return buf.toString();
    }

    public void loginOk() throws Exception {

        emulatorClient = new EmulatorClient(this.http_emulator_host);

        try {

        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.COOKIE, "OAUTH-REQUEST-ID", okRequestId)
                .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_MOVED_TEMPORARILY): codes[0])
                .addResponseEntry(AttributeType.HEADER, "Location",
                        OAuthUtils.buildOAuthRedirrectURL(this.testHost, this.openthisPage, "OK", false, okCode, "", "", hhid_public_url))
                .save();

        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.PARAMETER, "code", okCode)
                .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[1])
                .addResponseEntry(AttributeType.BODY, null, OAuthUtils.buildAccessTokenResponse(okToken, this.okUserId))
                .save();
        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.PARAMETER, "access_token", okToken)
                .addResponseEntry(AttributeType.STATUS, null, codes ==null ? Integer.toString(HttpStatus.SC_OK): codes[2])
                .addResponseEntry(AttributeType.BODY, null, jsonMapper.writeValueAsString(this.okData.getUser()))
                .save();
            setIfTheUserCreated(true);
        } catch (IllegalStateException e) {
            error += e.getMessage();
        }

        emulatorClient.stopClient();

    }

    public String getOkRequestId() {
        if (getIfTheUserCreated()) {
            return this.okRequestId;
        }
        else{
            return error;
        }
    }

    public String getOKID() {
        if (getIfTheUserCreated()) {
            return this.okUserId;
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
