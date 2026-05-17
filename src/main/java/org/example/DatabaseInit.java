package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInit {

    private static final String JDBC_URL = "jdbc:h2:mem:usersdb;DB_CLOSE_DELAY=-1";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, "sa", "")) {
            Statement stmt = conn.createStatement();

            String createTableSql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "name VARCHAR(255),\n" +
                    "age TINYINT,\n" +
                    "actual_work VARCHAR(255),\n" +
                    "previous_work VARCHAR(255),\n" +
                    "current_status_active BOOLEAN\n" +
                    ")";
            stmt.execute(createTableSql);

            String insertSql = "INSERT INTO users (name, age, actual_work, previous_work, current_status_active) VALUES\n" +
                    "('Тимур', 30, 'Online Tours', 'УлГУ, Белочка', true),\n" +
                    "('Артём', 25, 'Ультра', 'УлГПУ, Улёт', true),\n" +
                    "('Алексей', 45, 'Автозавод', 'КЭИ, Автозавод', false),\n" +
                    "('Максим', 40, 'ВСРФ', 'АМТ, Лидер', true),\n" +
                    "('Никита', 28, 'Озон-склад', 'УИ ГА, Яндекс-доставка', false)";
            stmt.execute(insertSql);

            System.out.println("Database initialization complete");
        } catch (Exception e) {
            System.err.println("Error while working with database: " + e.getMessage());
        }
    }
}
