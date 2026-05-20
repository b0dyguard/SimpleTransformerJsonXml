package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class Server {

    public static void start() throws IOException {

        int port = PropertiesTake.getPort();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/convert", new ConvertHandler());
        server.createContext("/create", new CreateHandler());
        server.createContext("/read", new ReadHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port " + port);

    }
}