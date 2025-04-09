package com.redbull.redbull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverSingleton {
    private static WebDriver instance;

    private WebDriverSingleton() {}

    public static WebDriver getInstance() {
        if (instance == null) {
            WebDriverManager.chromedriver().setup(); // Ensure the ChromeDriver is properly set up
            instance = new ChromeDriver(); // Create the WebDriver instance
            instance.manage().window().maximize(); // Configure the instance
            
        }
        return instance;
    }

    public static void closeInstance() {
        if (instance != null) {
            instance.quit();
            instance = null;
        }
    }
}
