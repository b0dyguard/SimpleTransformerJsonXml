package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesTake {

    public static int port = 9000;
    public static int intervalMinutes = 1;
    public static String targetDirectory = "C:/temp/export";
    public static Properties prop = new Properties();
    public static InputStream in = null;

    private static void getProps() {
        try {
            in = Main.class.getClassLoader().getResourceAsStream("application.conf");
            if (in == null) {
                System.out.println("\"application.conf\" not found in the project directory.");
                throw new FileNotFoundException();
            }
        } catch (IOException e) {
            System.err.println("Error reading configuration file." + e.getMessage());
        }

        try {
            prop.load(in);
        } catch (IOException e) {
            System.err.println("Error reading configuration file." + e.getMessage());
        }
    }


    public static int getIntervalMinutes() {

        getProps();

        String intervalMinutesValue = prop.getProperty("intervalMinutes");

        if (intervalMinutesValue != null && !intervalMinutesValue.isEmpty()) {
            boolean intervalMinutesValueExam = intervalMinutesValue.matches("\\d+");
            if (intervalMinutesValueExam) {
                intervalMinutes = Integer.parseInt(intervalMinutesValue);
            } else {
                System.out.println("Error. The value of \"intervalMinutes\" in the configuration file \"application.conf\" is not a valid integer. Using default value: " + intervalMinutes);
            }
        } else {
            System.out.println("Error in the value of \"intervalMinutes\" in the configuration file \"application.conf\". Using default value: " + intervalMinutes);
        }
        return intervalMinutes;
    }

    public static String getTargetDirectory() {

        getProps();

        String targetDirectoryValue = prop.getProperty("targetDirectory");

        if (targetDirectoryValue != null && !targetDirectoryValue.isEmpty()) {
            targetDirectory = targetDirectoryValue;
        } else {
            System.out.println("Error in the value of \"targetDirectory\" in the configuration file \"application.conf\". Using default value: " + targetDirectory);
        }
        return targetDirectory;
    }

    public static int getPort() {

        getProps();

        String portValue = prop.getProperty("port");

            if (portValue != null && !portValue.isEmpty()) {
                boolean portExam = portValue.matches("\\d+");
                if (portExam) {
                    port = Integer.parseInt(portValue);
                } else  {
                    System.out.println("Error. The value of \"port\" in the configuration file \"application.conf\" is not a valid integer. The default port is used: " + port);
                }
            } else {
                System.out.println("Error in the value of \"port\" in the configuration file \"application.conf\". The default port is used: " + port);
            }
        return port;
        }
    }
