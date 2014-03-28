package ru.hh.standalone.utils.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.hh.standalone.utils.oauth.vk_social_entity.*;
import ru.hh.http.emulator.client.EmulatorClient;
import ru.hh.http.emulator.client.entity.AttributeType;

import java.util.Date;


/**
 * User: dsobol
 */
public class VkAuthUtils{
  private  String vkRequestId = UniqueIdentifierUtils.generateRequestId(getClass(), "loginTest");//final
  private  String vkCode = UniqueIdentifierUtils.generateOAuthCode(getClass(), "loginTest");
  private  String vkToken = UniqueIdentifierUtils.generateOAuthToken(getClass(), "loginTest");


  private ObjectMapper jsonMapper = new ObjectMapper();
  private EmulatorClient emulatorClient;
  private String http_emulator_host;
    private String hhid_public_url = "http://hhid.mercury.pyn.ru/";




  public VkAuthUtils(String http_emulator) {
        this.http_emulator_host = http_emulator;
  }

    public void addCookie(final String domain, final String name, final String value, final boolean isSecure) {
        WebDriver driver = new FirefoxDriver();
        driver.get((isSecure ? "https" : "http") + "://" + domain);

        driver.manage().addCookie(new Cookie(name, value, domain, "/", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000), isSecure));

        driver.get("http://mercury.pyn.ru");
    }

  public void LoginVk(VkData vkData, String sUrlRedirect) throws Exception {
    String vkUserId = vkData.getResponse().getUser().get(0).getUid();
    emulatorClient = new EmulatorClient(this.http_emulator_host);


    System.out.println("vkRequestId : " + vkRequestId);
    System.out.println("vkCode: " + vkCode);
    System.out.println("vkToken: " + vkToken);

      //vkRequestId = "ru.hh.standalone.utils.oauth.VkAuthUtils_loginTest_reqIde1451ebe-ce25-4d7f-803d-3c18994e1bbf";
      //vkCode = "ru.hh.standalone.utils.oauth.VkAuthUtils_loginTest_code0543ff00-2c6e-4c5e-a381-e6275fadc754";
      //vkToken = "ru.hh.standalone.utils.oauth.VkAuthUtils_loginTest_token467bf8ea-5853-4404-9ec9-d7fc0d9507ba";

    emulatorClient.createSimpleRule()
        .addEQ(AttributeType.COOKIE, UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, vkRequestId)
        .addResponseEntry(AttributeType.STATUS, null, "302")
        .addResponseEntry(AttributeType.HEADER, "Location", OAuthUtils.buildOAuthRedirrectURL(sUrlRedirect, sUrlRedirect, "VK", false, vkCode, "", "", this.hhid_public_url))
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

      //addCookie("oauth.vk.com", UniqueIdentifierUtils.OAUTH_REQUEST_ID_COOKIE, vkRequestId, true);

  }
}
