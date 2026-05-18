package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {

    private static final String JDBC_URL = "jdbc:h2:mem:usersdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, "sa", "")) {
            Statement stmt = conn.createStatement();

            String createTableSql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "age TINYINT, " +
                    "actual_work VARCHAR(255), " +
                    "previous_works VARCHAR(1000), " +
                    "current_status_active BOOLEAN" +
                    ")";
            stmt.execute(createTableSql);

            stmt.execute("TRUNCATE TABLE users");

            String insertSql = "INSERT INTO users (name, age, actual_work, previous_works, current_status_active) VALUES " +
                    "('Тимур', 22, 'Online Tours', 'Улгу, Белочка', true), " +
                    "('Артём', 22, 'Ультра', 'УлГПУ, Улёт', true), " +
                    "('Алексей', 23, 'Автозавод', 'КЭИ, Яндекс-доставка', false), " +
                    "('Никита', 21, 'УИ ГА', 'УИ ГА, Озон', true), " +
                    "('Елена', 24, 'Тинькофф', 'УлГТУ, Альфа-Банк', true), " +
                    "('Сергей', 25, 'Ростелеком', 'УлГУ, Билайн', false)";
            stmt.execute(insertSql);

            System.out.println("Database initialization complete");
        } catch (Exception e) {
            System.err.println("Error while working with database: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
}
