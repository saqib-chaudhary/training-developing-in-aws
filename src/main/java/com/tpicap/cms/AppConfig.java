package com.tpicap.cms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import software.amazon.awssdk.regions.Region;

public class AppConfig {

    private Map<String, String> appProperties;

    public AppConfig() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            // load a properties file from class path, inside static method
            prop.load(input);

            Map<String, String> map = new HashMap<>();

            for (String propertyName : prop.stringPropertyNames()) {
                String envValue = System.getProperty(propertyName);
                if (envValue == null || envValue.trim().length() == 0)
                    envValue = System.getenv(propertyName);
                if (envValue == null || envValue.trim().length() == 0)
                    envValue = prop.getProperty(propertyName);
                map.put(propertyName, envValue);
            }

            appProperties = Map.copyOf(map);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public String getAwsProfile() {
        return appProperties.get("APP_AWS_PROFILE");
    }

    public String getS3BucketName() {
        return appProperties.get("APP_S3_BUCKET");
    }

    public String getDynamoDBTable() {
        return appProperties.get("APP_DYNAMO_DB_TABLE");
    }

    public Region getAwsRegion() {
        return Region.of(appProperties.get("APP_AWS_REGION"));
    }

}
