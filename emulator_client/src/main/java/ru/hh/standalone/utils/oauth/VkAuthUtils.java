package ru.hh.standalone.utils.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hh.standalone.utils.oauth.vk_social_entity.*;
import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;


/**
 * User: dsobol
 */
public class VkAuthUtils{
  private final String vkRequestId = UniqueIdentifierUtils.generateRequestId(getClass(), "loginTest");
  private final String vkCode = UniqueIdentifierUtils.generateOAuthCode(getClass(), "loginTest");
  private final String vkToken = UniqueIdentifierUtils.generateOAuthToken(getClass(), "loginTest");


  private ObjectMapper jsonMapper = new ObjectMapper();
  private EmulatorClient emulatorClient;
  private String http_emulator_host;
    private String hhid_public_url = "http://hhid.mercury.pyn.ru/";




  public VkAuthUtils(String http_emulator) {
        this.http_emulator_host = http_emulator;
  }

  public void LoginVk(VkData vkData, String sUrlRedirect) throws Exception {
    String vkUserId = vkData.getResponse().getUser().get(0).getUid();
    emulatorClient = new EmulatorClient(this.http_emulator_host);
    //driver.addCookie("oauth.vk.com", UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, vkRequestId, true);


    emulatorClient.createSimpleRule()
        .addEQ(AttributeType.COOKIE, UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, vkRequestId)
        .addResponseEntry(AttributeType.STATUS, null, "302")
        .addResponseEntry(AttributeType.HEADER, "Location",
            OAuthUtils.buildOAuthRedirrectURL(sUrlRedirect, sUrlRedirect, "VK", false, vkCode, "", "", this.hhid_public_url))
        .save();

    emulatorClient.createSimpleRule()
        .addEQ(AttributeType.PARAMETER, "code", vkCode)
        .addResponseEntry(AttributeType.STATUS, null, "200")
        .addResponseEntry(AttributeType.BODY, null, OAuthUtils.buildAccessTokenResponse(vkToken, vkUserId))
        .save();

    emulatorClient.createSimpleRule()
        .addEQ(AttributeType.PARAMETER, "access_token", vkToken)
        .addResponseEntry(AttributeType.STATUS, null, "200")
        .addResponseEntry(AttributeType.BODY, null, jsonMapper.writeValueAsString(vkData))
        .save();


  }
}
