package ru.hh.standalone.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by marefyev on 3/31/14.
 */
public class SimpleServer {

    public static void main(String args[]) throws Exception{

        String portSTR = args.length != 0 ? args[0] : null;

        Server server = new Server(portSTR != null && portSTR.length() > 3? Integer.parseInt(portSTR): 7171);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        server.setHandler(context);
        context.addServlet(new ServletHolder(new StartPage()),"/reguser");
        context.addServlet(new ServletHolder(new CreateUser()),"/reguser/post/createuser");


        server.start();
        server.join();
    }

}
