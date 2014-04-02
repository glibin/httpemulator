package ru.hh.standalone.registartion.server;


import org.apache.commons.io.IOUtils;
import ru.hh.standalone.registration.Vkontakte;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;


/**
 * Created by marefyev on 3/31/14.
 */
public class StartPage extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);//TODO JSON
        request.setCharacterEncoding("UTF-8");

        String everything;
        try(FileInputStream inputStream = new FileInputStream("src/main/java/ru/hh/standalone/webapp/face.html")) {
            everything = IOUtils.toString(inputStream, "UTF-8");
        }

        //System.out.println(everything);

        response.getWriter().println(everything);
    }

}
