package ru.hh.standalone.server;

import ru.hh.standalone.networks.Vkontakte;

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


//        Enumeration<String> parameterNames = request.getParameterNames();
//        while(parameterNames.hasMoreElements()) {
//            String name = parameterNames.nextElement();
//
//            System.out.println(name +":" +request.getParameter(name));
//        }


        String social = request.getParameter("social");
        String site = request.getParameter("site");
        String serverName = request.getParameter("serverName");

        if (social != null && site != null && serverName != null) {
            String emulURL = request.getParameter("emulURL");
            emulURL = emulURL.equals("") ? "http://jenkins.pyn.ru:18880" : emulURL;

            response.setContentType("application/json");
            request.setCharacterEncoding("UTF-8");

            StringBuilder responseJSON = new StringBuilder();

            String errorMessage = "Something went terribly wrong";

            //TODO ENUM
            if (social.equals("VK")) {


                Vkontakte vkontakt = new Vkontakte(site, serverName, emulURL);

                try {
                    vkontakt.loginVk();

                    String step1 = "\"step1\":\"<li>Меняем хосты на тестовом стенде и на машине где будет запускаться браузер " +
                            "так чтобы <b class='blue'>vkontakte.ru</b>, <b class='blue'>api.vkontakte.ru</b>, <b class='blue'>vk.com</b>,"+
                            " <b class='blue'>oauth.vk.com</b> резолвились " +
                            "на машину где поднят http_emulator</li>\"";

                    String step2 = "\"step2\":\"<li>Преходим в браузере на <b class='blue'>https://oauth.vk.com</b> (страница будет пустая) "+
                            " и проставляем куку. key:'<b class='blue'>OAUTH-REQUEST-ID</b>',"+
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


            }
            String send = responseJSON.toString();
            if (send.equals("")) {
                send = "{\"STATUS\":500,\"error\":\""+errorMessage+"\"}";
            }
            response.getWriter().println(send);
        } else {
            String send = "{\"STATUS\":500,\"error\":\"Required params are missing!\"}";
            response.getWriter().println(send);
        }

    }
}
