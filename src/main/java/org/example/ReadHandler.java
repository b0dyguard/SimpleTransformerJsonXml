package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReadHandler implements HttpHandler {
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!"GET".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            String query = exchange.getRequestURI().getRawQuery();
            Map<String, String> params = parseQueryParams(query);

            String name = params.get("name");
            String ageStr = params.get("age");
            String actualWork = params.get("actual_work");

            if (name.isEmpty() || ageStr.isEmpty() || actualWork.isEmpty()) {
                String badRequestXml = "<response><error>Missing required parameters: name, age, or actual_work</error></response>";
                sendXmlResponse(exchange, 400, badRequestXml);
                return;
            }

            int age = Integer.parseInt(ageStr);

            User user = findUserInDb(name, age, actualWork);

            if (user != null) {
                String xmlResponse = xmlMapper.writeValueAsString(user);
                sendXmlResponse(exchange, 200, xmlResponse);
            } else {
                String notFoundXml = "<response><error>No such user</error></response>";
                sendXmlResponse(exchange, 404, notFoundXml);
            }
        } catch (NumberFormatException e) {
            String badRequestXml = "<response><error>Parameter 'age' must be a number</error></response>";
            sendXmlResponse(exchange, 400, badRequestXml);
        } catch (Exception e) {
            System.err.println("Error searching for user: " + e.getMessage());
            e.printStackTrace();
            String errorXml = "<response><error>Server error: " + e.getMessage() + "</error></response>";
            sendXmlResponse(exchange, 500, errorXml);
        }
    }

    private User findUserInDb(String name, int age, String actualWork) throws Exception {
        String sql = "SELECT * FROM users WHERE name = ? AND age = ? AND actual_work = ?";

        try (Connection conn = DatabaseInit.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, actualWork);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.name = rs.getString("name");
                    user.age = rs.getInt("age");
                    user.actualWork = rs.getString("actual_work");
                    user.currentStatusActive = rs.getBoolean("current_status_active");

                    String previousWorksStr =  rs.getString("previous_works");
                    if (previousWorksStr != null && !previousWorksStr.isEmpty()) {
                        user.previousWorks = Arrays.asList(previousWorksStr.split(", "));
                    }

                    return user;
                }
            }
        }
        return null;
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> result = new HashMap<>();
        if(query == null ||  query.isEmpty()) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                String key = URLDecoder.decode(entry[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(entry[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }

    private void sendXmlResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
