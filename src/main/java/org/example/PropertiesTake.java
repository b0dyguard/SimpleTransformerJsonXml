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

    public static InputStream getProps() throws IOException {
        in = Main.class.getClassLoader().getResourceAsStream("application.conf");

        if  (in == null) {
            System.out.println("\"application.conf\" not found in the project directory.");
            throw new FileNotFoundException();
        }
        return in;
    }

    public static void propLoad() throws IOException {
        try {
            prop.load(getProps());
        } catch (IOException e) {
            System.err.println("Error reading configuration file." + e.getMessage());
        }
    }

    public static int getIntervalMinutes() throws IOException {

        in = getProps();
        propLoad();

        String intervalMinutesValue = prop.getProperty("intervalMinutes");
        boolean intervalMinutesValueExam = !intervalMinutesValue.matches(".*[^0-9.*]");

        if (intervalMinutesValue != null && !intervalMinutesValue.isEmpty() && intervalMinutesValueExam) {
            intervalMinutes = Integer.parseInt(intervalMinutesValue);
        } else {
            System.out.println("Error in the value of \"intervalMinutes\" in the configuration file \"application.conf\". Using default value: " + intervalMinutes);
        }
        return intervalMinutes;
    }

    public static String getTargetDirectory() throws IOException {

        in = getProps();
        propLoad();

        String targetDirectoryValue = prop.getProperty("targetDirectory");

        if (targetDirectoryValue != null && !targetDirectoryValue.isEmpty()) {
            targetDirectory = targetDirectoryValue;
        } else {
            System.out.println("Error in the value of \"targetDirectory\" in the configuration file \"application.conf\". Using default value: " + targetDirectory);
        }
        return targetDirectory;
    }

    public static int getPort() throws IOException {
        in = getProps();
        propLoad();

        String portValue = prop.getProperty("port");
        boolean portExam = !portValue.matches(".*[^0-9.*]");

        if (portValue != null && !portValue.isEmpty() && portExam) {
            port = Integer.parseInt(portValue);
        } else {
            System.out.println("Error in the configuration file \"application.conf\". The default port is used: " + port);
        }
        return port;
    }
}
