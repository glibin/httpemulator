package ru.hh.standalone.server;

import ru.hh.standalone.networks.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marefyev on 4/1/14.
 */
public class CreateUser extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String social = request.getParameter("social");
        String site = request.getParameter("site");
        String serverName = request.getParameter("serverName");
        String extended = request.getParameter("extended");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String rule1Response = request.getParameter("rule1Response");
        String rule2Response = request.getParameter("rule2Response");
        String rule3Response = request.getParameter("rule3Response");
        String emulURL = request.getParameter("emulURL");


        if (social != null && site != null && serverName != null) {

            emulURL = emulURL.equals("") ? "http://jenkins.pyn.ru:18880" : emulURL;

            response.setContentType("application/json");
            request.setCharacterEncoding("UTF-8");

            StringBuilder responseJSON = new StringBuilder();

            String errorMessage = "Something went terribly wrong";

            //TODO ENUM
            if (social.equals("VK")) {

                Vkontakte vkontakt;

                if (extended.equals("yes")) {
                    vkontakt = new Vkontakte(site, serverName, emulURL, firstname.equals("")? "ВВедиИмяУрод":firstname, lastname.equals("")? "ФАМИЛИЯ" : lastname,
                            new String[]{rule1Response, rule2Response, rule3Response});
                } else {
                    vkontakt = new Vkontakte(site, serverName, emulURL);
                }


                try {
                    vkontakt.loginVk();

                    String step1 = "\"step1\":\"<li>Меняем хосты на тестовом стенде и на машине где будет запускаться браузер " +
                            "так чтобы <b class='blue'>vkontakte.ru</b>, <b class='blue'>api.vkontakte.ru</b>, <b class='blue'>vk.com</b>," +
                            " <b class='blue'>oauth.vk.com</b> резолвились " +
                            "на машину где поднят http_emulator</li>\"";

                    String step2 = "\"step2\":\"<li>Преходим в браузере на <b class='blue'>https://oauth.vk.com</b> (страница будет пустая) " +
                            " и проставляем куку. key:'<b class='blue'>OAUTH-REQUEST-ID</b>'," +
                            " value:'<b class='blue'>" +
                            vkontakt.getVkRequestId() + "</b>', ставим галочку <b class='blue'>secure</b>. (удобно использовать Edit This Cookie)</li>\"";

                    String step3 = "\"step3\":\"<li>Переходим на сайт <b class='blue'>" + site + "</b> стенда <b class='blue'>" + serverName +
                            "</b>, жмем кнопку <b class='blue'>Вконтакте</b>. (для другихъ сайтов не будет работать)." +
                            " Логин и пароль вводить не надо, аутентификация происходит по куке. </li>\"";

                    String step4 = "\"step4\":\"<li>TTL у созданных правил 10 часов</li>\"";
                    String step5 = "\"step5\":\"<li>VK ID созданного пользователя: <b class='blue'>" + vkontakt.getVKID() + "</b></li>\"";
                    String ruleIDs = "\"ruleIDs\":\"" + vkontakt.getRuleIDs() + "\"";

                    String status = "\"STATUS\":200";

                    responseJSON.append("{").append(step1).append(",").append(step2).append(",").append(step3).append(",")
                            .append(step4).append(",").append(step5).append(",").append(ruleIDs).append(",").append(status).append("}");
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = e.getMessage();
                }


            } else if (social.equals("OK")) {

                Odnoglazniki odnoglaz;

                if (extended.equals("yes")) {
                    odnoglaz = new Odnoglazniki(site, serverName, emulURL, firstname.equals("")? "ВВедиИмяУрод":firstname, lastname.equals("")? "ФАМИЛИЯ" : lastname,
                            new String[]{rule1Response, rule2Response, rule3Response});
                } else {
                    odnoglaz = new Odnoglazniki(site, serverName, emulURL);
                }


                try {
                    odnoglaz.loginOk();

                    String step1 = "\"step1\":\"<li>Меняем хосты на тестовом стенде и на машине где будет запускаться браузер " +
                            "так чтобы <b class='blue'>api.odnoklassniki.ru</b>, <b class='blue'>odnoklassniki.ru</b>, <b class='blue'>www.odnoklassniki.ru</b>" +
                            " резолвились " +
                            "на машину где поднят http_emulator</li>\"";

                    String step2 = "\"step2\":\"<li>Преходим в браузере на <b class='blue'>http://odnoklassniki.ru</b> (страница будет пустая) " +
                            " и проставляем куку. key:'<b class='blue'>OAUTH-REQUEST-ID</b>'," +
                            " value:'<b class='blue'>" +
                            odnoglaz.getOkRequestId() + "</b>'. (удобно использовать Edit This Cookie)</li>\"";

                    String step3 = "\"step3\":\"<li>Переходим на сайт <b class='blue'>" + site + "</b> стенда <b class='blue'>" + serverName +
                            "</b>, жмем кнопку <b class='blue'>Одноклассники</b>. (для другихъ сайтов не будет работать)." +
                            " Логин и пароль вводить не надо, аутентификация происходит по куке. </li>\"";

                    String step4 = "\"step4\":\"<li>TTL у созданных правил 10 часов</li>\"";
                    String step5 = "\"step5\":\"<li>OK ID созданного пользователя: <b class='blue'>" + odnoglaz.getOKID() + "</b></li>\"";
                    String ruleIDs = "\"ruleIDs\":\"" + odnoglaz.getRuleIDs() + "\"";

                    String status = "\"STATUS\":200";

                    responseJSON.append("{").append(step1).append(",").append(step2).append(",").append(step3).append(",")
                            .append(step4).append(",").append(step5).append(",").append(ruleIDs).append(",").append(status).append("}");
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = e.getMessage();
                }
            }else if (social.equals("FB")) {

                FaceBook face;

                if (extended.equals("yes")) {
                    face = new FaceBook(site, serverName, emulURL, firstname.equals("")? "ВВедиИмяУрод":firstname, lastname.equals("")? "ФАМИЛИЯ" : lastname,
                            new String[]{rule1Response, rule2Response, rule3Response});
                } else {
                    face = new FaceBook(site, serverName, emulURL);
                }


                try {
                    face.loginFb();

                    String step1 = "\"step1\":\"<li>Меняем хосты на тестовом стенде и на машине где будет запускаться браузер " +
                            "так чтобы <b class='blue'>facebook.com</b>, <b class='blue'>www.facebook.com</b>, <b class='blue'> www.facebook.com</b>" +
                            " резолвились " +
                            "на машину где поднят http_emulator</li>\"";

                    String step2 = "\"step2\":\"<li>Преходим в браузере на <b class='blue'>https://www.facebook.com</b> (страница будет пустая) " +
                            " и проставляем куку. key:'<b class='blue'>OAUTH-REQUEST-ID</b>'," +
                            " value:'<b class='blue'>" +
                            face.getFbRequestId() + "</b>', ставим галочку <b class='blue'>secure</b>. (удобно использовать Edit This Cookie)</li>\"";

                    String step3 = "\"step3\":\"<li>Переходим на сайт <b class='blue'>" + site + "</b> стенда <b class='blue'>" + serverName +
                            "</b>, жмем кнопку <b class='blue'>Фейсбук</b>. (для другихъ сайтов не будет работать)." +
                            " Логин и пароль вводить не надо, аутентификация происходит по куке. </li>\"";

                    String step4 = "\"step4\":\"<li>TTL у созданных правил 10 часов</li>\"";
                    String step5 = "\"step5\":\"<li>FB ID созданного пользователя: <b class='blue'>" + face.getFBID() + "</b></li>\"";
                    String ruleIDs = "\"ruleIDs\":\"" + face.getRuleIDs() + "\"";

                    String status = "\"STATUS\":200";

                    responseJSON.append("{").append(step1).append(",").append(step2).append(",").append(step3).append(",")
                            .append(step4).append(",").append(step5).append(",").append(ruleIDs).append(",").append(status).append("}");
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMessage = e.getMessage();
                }


            }

            String send = responseJSON.toString();
            if (send.equals("")) {
                send = "{\"STATUS\":500,\"error\":\"" + errorMessage + "\"}";
            }
            response.getWriter().println(send);
        } else {
            String send = "{\"STATUS\":500,\"error\":\"Required params are missing!\"}";
            response.getWriter().println(send);
        }

    }
}
