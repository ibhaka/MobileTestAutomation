package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class DriverManager {

    private static AndroidDriver driver;
    private static final Properties props = new Properties();

    static {
        try {
            props.load(new FileInputStream("src/test/resources/config/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static AndroidDriver getDriver() {
        if (driver == null) {
            initDriver();
        }
        return driver;
    }

    private static void initDriver() {
        try {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName(props.getProperty("platform.name"));
            options.setDeviceName(props.getProperty("device.name"));
            options.setPlatformVersion(props.getProperty("platform.version"));
            options.setAutomationName(props.getProperty("automation.name"));
            options.setAppPackage(props.getProperty("app.package"));
            options.setAppActivity(props.getProperty("app.activity"));
            options.setNoReset(true);
            options.setAutoGrantPermissions(true);
            options.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
            options.setCapability("appium:adbExecTimeout", 60000);
            options.setCapability("appium:androidInstallTimeout", 90000);

            driver = new AndroidDriver(new URL(props.getProperty("appium.url")), options);
            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(
                            Long.parseLong(props.getProperty("implicit.wait", "10"))));

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AndroidDriver: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static Properties getProps() {
        return props;
    }
}
