package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class Server {

    public static void start() throws IOException {

        int port = Connection.getPort();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/convert", new ConvertHandler());
        server.createContext("/create", new CreateHandler());
        server.createContext("/read", new ReadHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port " + port);

    }

    private static class Connection {

        public static int port = 9000;

        public static int getPort() {

            Properties prop = new Properties();

            try (InputStream in = Main.class.getClassLoader().getResourceAsStream("application.conf")) {
                if (in == null) {
                    System.out.println("Can't find \"application.conf\". The default port is used: " + port);
                } else {
                    prop.load(in);

                    String portValue = prop.getProperty("port");
                    if (portValue != null) {
                        port = Integer.parseInt(portValue);
                    } else {
                        System.out.println("Error in the configuration file \"application.conf\". The default port is used: " + port);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading configuration: " + e.getMessage());
            }
            return port;
        }
    }
}