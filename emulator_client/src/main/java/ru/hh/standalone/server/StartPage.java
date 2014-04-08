package ru.hh.standalone.server;


import org.apache.commons.io.IOUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;


/**
 * Created by marefyev on 3/31/14.
 */
public class StartPage extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);//TODO JSON
        request.setCharacterEncoding("UTF-8");

        String everything;
//        try(FileInputStream inputStream = new FileInputStream(new File(this.getClass().getResource("/face.html").toURI()))) {
//            everything = IOUtils.toString(inputStream, "UTF-8");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//            System.out.println(this.getClass().getResource("/face.html"));

        InputStream is = getClass().getResourceAsStream("/face.html");
        InputStreamReader fis = new InputStreamReader(is, "UTF-8");

        BufferedReader reader = new BufferedReader(fis);
        StringBuilder builder = new StringBuilder();

        String line = reader.readLine();
        while (line != null) {

            builder.append(line).append("\n");
            line = reader.readLine();


        }
        everything = builder.toString();

        response.getWriter().println(everything);
    }

}
