package com.redbull.redbull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartReader_Frame2_Shift {

    private static final Logger log = LoggerFactory.getLogger(ChartReader_Frame2_Shift.class);

    public static Map<String, Double> frameToframe() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        System.out.println("Please update the changes in Chart manually");
        Thread.sleep(15000); // Wait for manual update
        driver.get("https://www.angelone.in/trade/watchlist/chart");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time

        // Switching to the first iframe where the chart is expected to be located
        WebElement iframe1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
        driver.switchTo().frame(iframe1);
        log.info("Script chart loaded");

        // Switching to the second nested iframe where financial details are displayed
        WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
        driver.switchTo().frame(iframe2);
        log.info("Financial chart frame activated");

        // Collect indicator values
        Map<String, Double> indicatorValues = collectIndicatorValues(driver);

        // Output the collected values without additional debug information
//        indicatorValues.forEach((key, value) -> System.out.println(key + ": " + value));

        return indicatorValues;
    }

    public static Map<String, Double> collectIndicatorValues(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Increased wait time
        Map<String, Double> indicators = new HashMap<>();

        indicators.put("ADX", getValueByTitle(wait, "ADX"));
        indicators.put("RSI", getValueByTitle(wait, "Plot"));
        indicators.put("ORBH", getValueByTitle(wait, "Orb High"));
        indicators.put("ORBL", getValueByTitle(wait, "Orb Low"));
        indicators.put("MACD", getValueByTitle(wait, "Histogram"));
        indicators.put("MACDG", getValueByTitle(wait, "MACD"));
        indicators.put("MACDR", getValueByTitle(wait, "Signal"));

        // FA and FB using XPath
        indicators.put("FA", getValueByXPath(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[5]/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[1]/div"));
        indicators.put("FB", getValueByXPath(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[5]/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[2]/div"));

        return indicators;
    }

    private static double getValueByTitle(WebDriverWait wait, String title) {
        String selector = String.format("div[title='%s']", title);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

        // Only print the extracted text for the value (not raw HTML or style)
        if (element.isDisplayed()) {
            String text = element.getText();
            return parseValue(text);
        } else {
            return -1.0; // Or handle accordingly
        }
    }

    private static double getValueByXPath(WebDriverWait wait, String xpath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        // Only print the extracted text for the value (not raw HTML or style)
        if (element.isDisplayed()) {
            String text = element.getText();
            return parseValue(text);
        } else {
            return -1.0; // Or handle accordingly
        }
    }

    private static double parseValue(String text) {
        if (text.isEmpty()) {
            return -1.0; // Return -1.0 or handle the case where value is missing
        }

        try {
            return Double.parseDouble(text.replace("−", "-").trim()); // Handles special minus sign
        } catch (NumberFormatException e) {
            return -1.0; // If the value cannot be parsed, return -1.0
        }
    }
}
