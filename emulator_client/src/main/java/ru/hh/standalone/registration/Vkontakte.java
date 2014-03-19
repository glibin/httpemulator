package ru.hh.standalone.registration;



import ru.hh.standalone.utils.oauth.vk_social_entity.*;
import ru.hh.standalone.utils.oauth.*;

import java.util.Random;


/**
 * User: dsobol
 */
public class Vkontakte {


  private VkData vkData;

  private String testHost;
  private VkPersonal vkPersonal;
  private String vkUserId;
  private String http_emulator_host;
  private String sEmailForUserVk;





  public Vkontakte(String testHost, VkPersonal vkPersonal, String vkUserId_2, String sEmailForUserVk,
                   String http_emulator) {
    this.testHost = testHost;
    this.vkPersonal = vkPersonal;
    this.vkUserId = vkUserId_2;
    this.sEmailForUserVk = sEmailForUserVk;
    this.http_emulator_host = http_emulator;
  }

   public static void main(String args[]) throws Exception {
    String vkUserId_2 = "1" + getNumber(10);
    Vkontakte vkontakt = new Vkontakte("http://hh.ru.mercury.pyn.ru", new VkPersonal(), vkUserId_2,
            vkUserId_2 + "@vkontakte.com", "http://jenkins.pyn.ru:18880");

    vkontakt.authorizationVkUser(vkontakt.vkUserId, vkontakt.testHost);  // авторизуемся через вконтакт (проставляем куку, формируем данные VK юзера и эмулуриуем авторизацию цепочкой запросов)

    System.out.println("Exiting");
  }

  public static String getNumber(int length) {
        Random randomGenerator = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < length; i++) {
            buf.append(Integer.toString(randomGenerator.nextInt(10)));
        }

        return buf.toString();
  }

  private void authorizationVkUser(String sUid, String sHost) throws Exception {
    VkAuthUtils Vk;
    String sUrlRedirect = sHost.contains("http://") ? sHost.replaceAll("http://", "") : sHost.contains("https://") ? sHost.replaceAll("https://", "") : sHost;
    vkData = new VkData(sUid, vkPersonal);  // создаем данные пользователя вконтаке (sUid - id пользователя), vkPersonal - данные о языках
    Vk = new VkAuthUtils(this.http_emulator_host);
    Vk.LoginVk(vkData, sUrlRedirect);  // выполняем цепочку запросов - результат авторизация через вконтакт
  }






}
