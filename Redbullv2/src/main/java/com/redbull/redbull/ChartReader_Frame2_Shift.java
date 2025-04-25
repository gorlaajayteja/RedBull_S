package com.redbull.redbull;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        log.info("Initializing WebDriver and navigating to the chart page.");
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        try {
            log.info("Switching to first iframe: scrip chart.");
            WebElement iframe1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
            driver.switchTo().frame(iframe1);

            log.info("Switching to second nested iframe: financial chart.");
            WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
            driver.switchTo().frame(iframe2);

            log.info("Collecting indicator values.");
            Map<String, Double> indicatorValues = collectIndicatorValues(driver);

            driver.switchTo().defaultContent();
            return indicatorValues;
        } catch (Exception e) {
            log.error("Error processing frames: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    public static Map<String, Double> collectIndicatorValues(WebDriver driver) {
        log.info("Starting indicator collection.");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Map<String, Double> indicators = new HashMap<>();
        List<String> indicatorKeys = Arrays.asList("ADX", "Plot", "Orb High", "Orb Low", "Histogram", "MACD", "Signal");

        while (true) {
            try {
                for (String key : indicatorKeys) {
                    indicators.put(key, getValueByTitle(wait, key));
                    log.info("Successfully retrieved value for {}", key);
                }
                break;
            } catch (Exception e) {
                log.warn("Some values could not be found. Requesting user input. Error: {}", e.getMessage());

                Scanner scanner = new Scanner(System.in);
                for (String key : indicatorKeys) {
                    System.out.print("Enter the XPath for " + key + " (or press Enter to keep the same): ");
                    String xpath = scanner.nextLine();
                    if (!xpath.isEmpty()) {
                        indicators.put(key, getValueByXpath(wait, xpath));
                        log.info("User provided XPath for {}: {}", key, xpath);
                    }
                }
                log.info("Restarting method with updated values.");
            }
        }

        return indicators;
    }

    private static Double getValueByXpath(WebDriverWait wait, String xpath) {
        try {
            log.info("Attempting to retrieve value for XPath: {}", xpath);
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            String text = element.getText().trim();
            log.info("Extracted text: {} for XPath: {}", text, xpath);

            return text.isEmpty() ? null : Double.parseDouble(text);
        } catch (Exception e) {
            log.error("Error retrieving value for XPath: {} - {}", xpath, e.getMessage(), e);
            return null;
        }
    }

    private static double getValueByTitle(WebDriverWait wait, String title) {
        try {
            log.info("Attempting to retrieve value for title: {}", title);
            String selector = String.format("div[title='%s']", title);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

            if (element.isDisplayed()) {
                String text = element.getText();
                log.info("Extracted text for {}: {}", title, text);
                return parseValue(text);
            } else {
                log.warn("Element for {} is not visible.", title);
                return -1.0;
            }
        } catch (Exception e) {
            log.error("Error retrieving value for title: {} - {}", title, e.getMessage(), e);
            return -1.0;
        }
    }

    private static double parseValue(String text) {
        if (text.isEmpty()) {
            log.warn("Parsed value is empty, returning default -1.0");
            return -1.0;
        }

        try {
            return Double.parseDouble(text.replace("−", "-").trim());
        } catch (NumberFormatException e) {
            log.error("Failed to parse numeric value: {} - {}", text, e.getMessage(), e);
            return -1.0;
        }
    }
}