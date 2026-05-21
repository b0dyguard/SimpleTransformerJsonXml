package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.h2.engine.Database;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateHandler implements HttpHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            User user = mapper.readValue(is, User.class);

            String previousWorkString = "";

            if (user.getPrevious_works() != null) {
                previousWorkString = String.join(", ", user.getPrevious_works());
            }

            saveUserToDb(user, previousWorkString);

            String response = "<response>\n<status>success</status>\n<message>User successfully created</message>\n</response>";
            sendXmlResponse(exchange, 201, response);

        } catch (Exception e) {
            System.err.println("Error while creating user: " + e.getMessage());
            e.printStackTrace();
            String errorResponse = "<response>\n<error>Server error: " + e.getMessage() + "</error>\n</response>";
            sendXmlResponse(exchange, 500, errorResponse);
        }
    }

    private void saveUserToDb(User user, String previousWorkString) throws Exception {
        String sql = "INSERT INTO users (name, age, actual_work, previous_works, current_status_active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseInit.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getAge());
            pstmt.setString(3, user.getActual_work());
            pstmt.setString(4, previousWorkString);
            pstmt.setBoolean(5, user.isCurrent_status_active());

            pstmt.executeUpdate();
        }
    }

    private void sendXmlResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/xml; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
