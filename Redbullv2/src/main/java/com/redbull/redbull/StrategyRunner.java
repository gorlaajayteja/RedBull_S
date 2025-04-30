package com.redbull.redbull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class StrategyRunner {

	private static final Logger logger = LoggerFactory.getLogger(StrategyRunner.class);


    public static void runADX_FABStrategy() throws InterruptedException {
        Map<String, Double> values;
        double adx, fa, fb;
        

        // Define the market close time
        LocalTime marketCloseTime = LocalTime.of(15, 30); // Market closes at 3:30 PM

        // Continuous execution until the market closes
        while (LocalTime.now().isBefore(marketCloseTime)) {
            logger.info("Starting a new strategy cycle...");

            // Step 1: Wait until ADX > 20
            while (true) {
                values = ChartReader_Frame2_Shift.frameToframe();

                if (isInvalid(values, "ADX")) {
                    logger.warn("❌ Failed to fetch ADX value. Retrying in 10 seconds...");
                    Thread.sleep(10000);
                    continue;
                }

                adx = values.get("ADX");
                logger.info("Current ADX: " + adx);

                if (adx > 20) {
                    logger.info("ADX is now above 20 ✅");
                    break;
                } else {
                    logger.info("Waiting for ADX to cross above 20, Current ADX is: "+adx);
                    Thread.sleep(20000);
                }
            }

            // Step 2: Wait for a clear signal (FA ≠ FB)
            while (true) {
                values = ChartReader_Frame2_Shift.frameToframe();

                if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                    logger.warn("❌ Failed to fetch FA/FB values. Retrying in 5 seconds...");
                    Thread.sleep(5000);
                    continue;
                }

                fa = values.get("FA");
                fb = values.get("FB");

                if (fa > fb) {
                    logger.info("📈 Buy CE Signal (FA > FB)");
                    Buy_Sell_Implemetation.buyCE();
                    monitorExit("CE");
                    break;

                } else if (fa < fb) {
                    logger.info("📉 Buy PE Signal (FA < FB)");
                    Buy_Sell_Implemetation.buyPE();
                    monitorExit("PE");
                    break;

                } else {
                    logger.info("⚖️ No Clear Signal (FA == FB), waiting...");
                    Thread.sleep(5000);
                }
            }
        }

        logger.info("Market closed. Terminating strategy execution.");
    }

    // Step 3: Monitor for reverse signal to exit
    private static void monitorExit(String positionType) throws InterruptedException {
        boolean isPositionOpen = true;

        while (isPositionOpen) {
            Thread.sleep(10000);
            Map<String, Double> values = ChartReader_Frame2_Shift.frameToframe();

            if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                logger.warn("❌ Error fetching FA/FB during exit check. Retrying in 5 seconds...");
                Thread.sleep(5000);
                continue;
            }

            double fa = values.get("FA");
            double fb = values.get("FB");

            if ((positionType.equals("CE") && fa < fb) || (positionType.equals("PE") && fa > fb)) {
                logger.info("🔁 Exit Signal Detected! Exiting position...");
                Positions.Exit_postions();
                isPositionOpen = false;
            } else {
                logger.info("⏳ Holding " + positionType + " - FA: " + fa + ", FB: " + fb);
            }
        }
    }

    // Helper method to check if value is invalid (null or -1.0)
    private static boolean isInvalid(Map<String, Double> map, String key) {
        return map == null || !map.containsKey(key) || map.get(key) == 0;
    }
    public static void DMI_MACD_FI() throws InterruptedException, TimeoutException {
        Map<String, Double> values;
        
        boolean isTradeInitiated = false;
        String positionType = null;

        // Define market close time
        LocalTime marketCloseTime = LocalTime.of(23, 30); // Market closes at 11:30 PM

        WebDriver driver = WebDriverSingleton.getInstance();
        
        // **Validate All XPaths Before Strategy Execution**
//        validateXPaths(driver);

        while (LocalTime.now().isBefore(marketCloseTime)) {
            logger.info("Starting a new strategy cycle...");
            values = ChartReader_Frame2_Shift.frameToframe();

            if (!areIndicatorsValid(values)) {
                logger.warn("❌ One or more indicators are missing. Retrying in 10 seconds...");
                Thread.sleep(10000);
                continue;
            }

            executeTradeStrategy(values, isTradeInitiated, positionType);
        }

        logger.info("Market closed. Terminating execution.");
    }

    // **XPath Validation Before Execution**
    public static void validateXPaths(WebDriver driver) throws TimeoutException {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
// 10 seconds timeout

        Scanner scanner = new Scanner(System.in);

        Map<String, String> indicatorsToCheck = Map.of(
            "MACD", Angel_Urls_and_Xpaths.Xpath_MACDmiddel,
            "MACDG", Angel_Urls_and_Xpaths.Xpath_MACDGreen,
            "MACDR", Angel_Urls_and_Xpaths.Xpath_MACDRed,
            "PDMI", Angel_Urls_and_Xpaths.Xpath_PDMI,
            "NDMI", Angel_Urls_and_Xpaths.Xpath_NDMI,
            "ADX", Angel_Urls_and_Xpaths.Xpath_ADX,
            "FA", Angel_Urls_and_Xpaths.Xpath_FisherGreen,
            "FB", Angel_Urls_and_Xpaths.Xpath_FisherRed
        );

        for (Map.Entry<String, String> entry : indicatorsToCheck.entrySet()) {
            String key = entry.getKey();
            String xpath = entry.getValue();

            System.out.print("Do you want to validate " + key + "? (yes/no): ");
            String userInput = scanner.nextLine();
            if (!userInput.equals("yes")) continue;

            boolean isValid = checkXPath(wait, xpath);
            while (!isValid) {
                System.out.println("XPath failed for: " + key);
                System.out.print("Enter a new XPath for " + key + ": ");
                String newXPath = scanner.nextLine();
                isValid = checkXPath(wait, newXPath);

                if (isValid) {
                    indicatorsToCheck.put(key, newXPath);
                    System.out.println("XPath for " + key + " updated successfully!");
                }
            }
        }
        scanner.close();
    }

    // **Indicator Health Check**
    private static boolean areIndicatorsValid(Map<String, Double> values) {
        return values != null && values.entrySet().stream()
            .allMatch(entry -> entry.getValue() != null && entry.getValue() != -0);
    }

    // **Trade Execution Logic**
    private static void executeTradeStrategy(Map<String, Double> values, boolean isTradeInitiated, String positionType) throws InterruptedException {
        double ADX = values.get("ADX");
        double MACDG = values.get("MACDG");
        double MACDR = values.get("MACDR");
        double PDMI = values.get("PDMI");
        double NDMI = values.get("NDMI");
        double FA = values.get("FA");
        double FB = values.get("FB");

        // **Trending Market Check (ADX > 20)**
        if (ADX > 20) {
            logger.info("✅ Trending Market Detected (ADX > 20)");

            // **Bullish Condition Check**
            if (MACDG > MACDR && PDMI > NDMI && FA > FB) {
                logger.info("📈 Bullish Signal Detected");
                logger.info("MACDG: " + MACDG + ", MACDR: " + MACDR + ", PDMI: " + PDMI + ", NDMI: " + NDMI + ", FA: " + FA + ", FB: " + FB);

                if (!isTradeInitiated) {
                    Buy_Sell_Implemetation.buyCE(); // Execute Buy Call
                    positionType = "CE";
                    isTradeInitiated = true;
                }
            }
            // **Bearish Condition Check**
            else if (MACDG < MACDR && PDMI < NDMI && FA < FB) {
                logger.info("📉 Bearish Signal Detected");
                logger.info("MACDG: " + MACDG + ", MACDR: " + MACDR + ", PDMI: " + PDMI + ", NDMI: " + NDMI + ", FA: " + FA + ", FB: " + FB);

                if (!isTradeInitiated) {
                    Buy_Sell_Implemetation.buyPE(); // Execute Buy Put
                    positionType = "PE";
                    isTradeInitiated = true;
                }
            } else {
                logger.info("⚖️ No Clear Signal Detected. Monitoring...");
                Thread.sleep(30000);
            }
        } else {
            logger.info("Waiting for ADX to rise above 20 to identify a trending market...");
        }

        // **Trade Exit Monitoring**
        if (isTradeInitiated && positionType != null) {
            monitorExit(positionType); // Pass position type to monitorExit()
        }
    }

    // **XPath Check**
    private static boolean checkXPath(WebDriverWait wait, String xpath) throws TimeoutException {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

   
}