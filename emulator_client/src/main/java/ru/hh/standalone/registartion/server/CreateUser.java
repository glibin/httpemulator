package ru.hh.standalone.registartion.server;

import ru.hh.standalone.registration.Vkontakte;

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
        // TODO Auto-generated method stub
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        response.getWriter().println("<h2>");
        response.getWriter().println("Username: " + userName);
        response.getWriter().println("</h2><h2>");
        response.getWriter().println("Inputed password: " + password);
        response.getWriter().println("</h2>");
        response.getWriter().println("");
        Vkontakte vkontakt = new Vkontakte("hh.ru", "mercury", "http://jenkins.pyn.ru:18880");
        try {
            vkontakt.loginVk();
        } catch (Exception e) {

        }
        response.getWriter().println(vkontakt.getVkRequestId());
        response.getWriter().println("");
        response.getWriter().println(vkontakt.getRuleIDs());
    }
}
