package ru.hh.standalone.utils.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;
import ru.hh.standalone.utils.oauth.ok_social_entity.OkData;

/**
 * User: alexandrdeshkevich
 */
public class OkAuthUtils {
    private final String okRequestId = UniqueIdentifierUtils.generateRequestId(getClass(), "loginTest");
    private final String okCode = UniqueIdentifierUtils.generateOAuthCode(getClass(), "loginTest");
    private final String okToken = UniqueIdentifierUtils.generateOAuthToken(getClass(), "loginTest");


    private ObjectMapper jsonMapper = new ObjectMapper();
    private EmulatorClient emulatorClient;
    private String hhid_public_url = "http://hhid.mercury.pyn.ru/";
    private String http_emulator_host;




    public OkAuthUtils(String http_emulator) {
        this.http_emulator_host = http_emulator;
    }

    public void LoginOk(OkData okData, String sUrlRedirect) throws Exception {
        String okUserId = okData.getUser().getUid();
        emulatorClient = new EmulatorClient(this.http_emulator_host);
        //driver.addCookie("odnoklassniki.ru", UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, okRequestId, false);



        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.COOKIE, UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, okRequestId)
                .addResponseEntry(AttributeType.STATUS, null, Integer.toString(HttpStatus.SC_MOVED_TEMPORARILY))
                .addResponseEntry(AttributeType.HEADER, "Location",
                        OAuthUtils.buildOAuthRedirrectURL(sUrlRedirect, sUrlRedirect, "OK", false, okCode, "", "", hhid_public_url))
                .save();

        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.PARAMETER, "code", okCode)
                .addResponseEntry(AttributeType.STATUS, null, Integer.toString(HttpStatus.SC_OK))
                .addResponseEntry(AttributeType.BODY, null, OAuthUtils.buildAccessTokenResponse(okToken, okUserId))
                .save();
        emulatorClient.createSimpleRule()
                .addEQ(AttributeType.PARAMETER, "access_token", okToken)
                .addResponseEntry(AttributeType.STATUS, null, Integer.toString(HttpStatus.SC_OK))
                .addResponseEntry(AttributeType.BODY, null, jsonMapper.writeValueAsString(okData.getUser()))
                .save();

        //driver.openHomePage();
    }
}
