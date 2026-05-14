package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ConvertHandler implements HttpHandler {
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            User user = jsonMapper.readValue(exchange.getRequestBody(), User.class);

            if ("Виталий".equalsIgnoreCase(user.name)) {
                String errorXml = "<response><error>This user does not exist.</error></response>";
                sendResponse(exchange, 404, errorXml);
                return;
            }

            String xmlResponse = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            sendResponse(exchange, 200, xmlResponse);

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Server error: " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String content) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/xml;charset=UTF-8");
        byte[] bytes = content.getBytes("UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}