package org.example;

public class Main {

    static void main() throws Exception {

        DatabaseInit.initDatabase();

        Server.startServer();

    }
}