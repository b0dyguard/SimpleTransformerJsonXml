package org.example;

public class Main {

    static void main() throws Exception {

        DatabaseInit.initDatabase();

        CsvExporter.startBackgroundExport();

        Server.startServer();

    }
}