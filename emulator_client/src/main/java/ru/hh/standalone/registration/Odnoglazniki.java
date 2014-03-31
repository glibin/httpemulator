package ru.hh.standalone.registration;

import ru.hh.standalone.utils.oauth.ok_social_entity.*;
import ru.hh.standalone.utils.oauth.*;

import java.util.Random;

/**
 * Created by marefyev on 3/31/14.
 */
public class Odnoglazniki {




    private OkData okData;



    private String testHost;
    private String okUserId_2;
    private String sEmailForUserOk;
    private String http_emulator_host = "http://jenkins.pyn.ru:18880";

    public static void main(String args[]) throws Exception {
        String okUserId_1 = "1" + getNumber(10);
        String sEmail_Ok1 = okUserId_1 + "@odnoklassniki.ru";

        Odnoglazniki odnoglazniki= new Odnoglazniki("http://hh.ru.mercury.pyn.ru", okUserId_1, sEmail_Ok1);

        odnoglazniki.authorizationOkUser(odnoglazniki.okUserId_2, odnoglazniki.sEmailForUserOk ,odnoglazniki.testHost);  // авторизуемся через вконтакт (проставляем куку, формируем данные VK юзера и эмулуриуем авторизацию цепочкой запросов)

        System.out.println("Exiting");
    }


    public Odnoglazniki(String testHost, String okUserId_2, String sEmailForUserOk) {
        this.testHost = testHost;
        this.okUserId_2 = okUserId_2;
        this.sEmailForUserOk = sEmailForUserOk;
    }

    private void authorizationOkUser(String sUid, String sEmail, String sHost) throws Exception {
        OkAuthUtils Ok;
        String sUrlRedirect = sHost.contains("http://") ? sHost.replaceAll("http://", "") : sHost.contains("https://") ? sHost.replaceAll("https://", "") : sHost;
        okData = new OkData(sUid);  // создаем данные пользователя OK (sUid - id пользователя)
        Ok = new OkAuthUtils(this.http_emulator_host);
        Ok.LoginOk(okData, sUrlRedirect);  // выполняем цепочку запросов - результат авторизация через вконтакт
    }



    public static String getNumber(int length) {
        Random randomGenerator = new Random();
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < length; i++) {
            buf.append(Integer.toString(randomGenerator.nextInt(10)));
        }

        return buf.toString();
    }


}
