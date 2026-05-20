package org.example;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CsvExporter {

    public static void startBackgroundExport() throws IOException {

        int intervalMinutes = PropertiesTake.getIntervalMinutes();
        String targetDirectory = PropertiesTake.getTargetDirectory();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        final String finalDir = targetDirectory;
        scheduler.scheduleAtFixedRate(() -> {
            exportData(finalDir);
        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    private static void exportData(String targetDirectory) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        String formattedDate = now.format(formatter);

        String fileName = formattedDate + "_actual_users.csv";

        File directory = new File(targetDirectory);
        String targetDirectoryCorrected = targetDirectory.replace('/', '\\');
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("[BG] Created new directory for backups: " + targetDirectoryCorrected);
            }
        }

        File outputFile = new File(directory, fileName);
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseInit.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        FileWriter writer = new FileWriter(outputFile, false)) {
            writer.append("id,name,age,actual_work,previous_works,current_status_active\n");
            while (rs.next()) {
                writer.append(rs.getString("id")).append(",");
                writer.append(rs.getString("name")).append(",");
                writer.append(rs.getString("actual_work")).append(",");

                String prevWorks = rs.getString("previous_works");
                if  (prevWorks != null) {
                    writer.append("\"").append(prevWorks).append("\"").append(",");
                } else {
                    writer.append(",");
                }

                writer.append(rs.getString("current_status_active")).append("\n");
            }

            System.out.println("[BG] Data successfully uploaded to file: " + outputFile.getAbsolutePath());

        }  catch (Exception e) {
            System.err.println("[BG] Error while uploading CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

