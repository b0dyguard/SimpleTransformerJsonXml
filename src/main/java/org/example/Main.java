package org.example;

public class Main {

    static void main() throws Exception {

        DatabaseInit.start();

        CsvExporter.startBackgroundExport();

        Server.start();

    }
}