package com.redbull.redbull;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChartReader_Frame2_Shift {

    static WebDriver driver = WebDriverSingleton.getInstance();
    static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    private static final Logger logger = Logger.getLogger(ChartReader_Frame2_Shift.class.getName());

    private static final String CHART_URL = "https://www.angelone.in/trade/watchlist/chart";

    public static Map<String, Double> frameToframe() throws InterruptedException {
        System.out.println("Navigating to chart URL: " + CHART_URL);
        driver.get(CHART_URL);
        FrameSwitcher.switchToFinancialChartFrame();
        System.out.println("Collecting indicator values from chart...");
        Map<String, Double> indicatorValues = collectIndicatorValues();
        driver.switchTo().defaultContent();
        return indicatorValues;
    }

    public static Map<String, Double> collectIndicatorValues() {
        Map<String, Double> indicators = new HashMap<>();

        indicators.put("MACDG", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_MACDGreen));
        indicators.put("MACDR", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_MACDRed));

        indicators.put("PDMI", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_PDMI));
        indicators.put("NDMI", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_NDMI));
        indicators.put("ADX", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_ADX));

        indicators.put("FA", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_FisherGreen));
        indicators.put("FB", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_FisherRed));

        return indicators;
    }

    public static double getValueByXPath(String xpath) {
        WebElement element = null;
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println("Waiting for element with XPath: " + xpath);
                    element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                    if (element.isDisplayed()) {
                        String value = element.getText();
                        System.out.println("Element found. Value: " + value);
                        return parseValue(value);
                    }
                } catch (WebDriverException e) {
                    System.out.println("Failed to locate element using XPath: " + xpath);
                    FrameSwitcher.switchToFinancialChartFrame();
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException ie) {
            logger.severe("Thread interrupted while waiting for XPath value.");
            Thread.currentThread().interrupt();
            return -0;
        }
    }
    
    public static List<String> getIndicaters(){
    	List<String> indicators = Arrays.asList(
                "Fisher",
                "Trigger",
                "ADX"
//                "+DI",
//                "-DI"
            );
    	return indicators;
    }

    public static Map<String, Double> getValueByTitle(List<String> titles) {
        Map<String, Double> indicatorResValues = new HashMap<>();
        String text = null;

        for (String title : titles) {
            String selector = String.format("div.valueValue-l31H9iuA[title='%s']", title);
            int maxRetries = 100;
            int retryDelayMillis = 10000;

            for (int attempt = 0; attempt < maxRetries; attempt++) {
                try {
                    System.out.println("🧠 RedBull is diving into charts to fetch: " + title + " (Attempt: " + (attempt + 1) + ")");
                    FrameSwitcher.switchToFinancialChartFrame();
                    WebElement element = driver.findElement(By.cssSelector(selector));

                    if (element.isDisplayed()) {
                        text = element.getText().trim();
                        System.out.println("✅ Found [" + title + "]: " + text + " — locked and loaded!");
                        indicatorResValues.put(title, parseValue(text));
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("🔄 Still searching for [" + title + "]... RedBull isn't giving up! 🧐");
                }

                try {
                    if (text == null) {
                        System.out.println("😬 Oops! Something went off-track... No worries boss, RedBull is reloading the chart!");
                        driver.get("https://www.angelone.in/trade/watchlist/chart");
                        Thread.sleep(retryDelayMillis);
                    } else {
                        break;
                    }
                } catch (InterruptedException ie) {
                    logger.severe("🚨 Thread interrupted while trying to be patient. Let's stay calm and carry on!");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        return indicatorResValues;
    }


    public static Double getValueByStyle(String rgbColor) {
        String selector = String.format("div.valueValue-l31H9iuA.apply-common-tooltip[style*='color: %s']", rgbColor);
        int maxRetries = 10;
        int retryDelayMillis = 500;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                System.out.println("[Attempt " + (attempt + 1) + "] Looking for style color: " + rgbColor);
                WebElement element = driver.findElement(By.cssSelector(selector));
                if (element.isDisplayed()) {
                    String value = element.getText().trim();
                    System.out.println("Found element with color '" + rgbColor + "': " + value);
                    return Double.parseDouble(value);
                }
            } catch (Exception e) {
                System.out.println("Value for color " + rgbColor + " not found, retrying...");
                FrameSwitcher.switchToFinancialChartFrame();
            }
            try {
                System.out.println("Sleeping for " + retryDelayMillis + "ms before retry...");
                Thread.sleep(retryDelayMillis);
            } catch (InterruptedException ie) {
                logger.severe("Thread interrupted during sleep.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.severe("Final failure: Could not find element with style color: " + rgbColor);
        return -0.0;
    }

    public static double parseValue(String text) {
        if (text.isEmpty()) return -0;
        try {
            return Double.parseDouble(text.replace("−", "-").trim());
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse value: " + text);
            return -0;
        }
    }
}

class FrameSwitcher {
    private static final WebDriver driver = WebDriverSingleton.getInstance();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

    public static void switchToMainChartFrame() {
//        System.out.println("Switching to main chart iframe (scrip chart)...");
        driver.switchTo().defaultContent();
        WebElement iframe1 = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
        driver.switchTo().frame(iframe1);
//        System.out.println("Switched to main chart iframe.");
    }

    public static void switchToFinancialChartFrame() {
        switchToMainChartFrame();
//        System.out.println("Switching to financial chart iframe...");
        WebElement iframe2 = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
        driver.switchTo().frame(iframe2);
//        System.out.println("Switched to financial chart iframe.");
    }
}
