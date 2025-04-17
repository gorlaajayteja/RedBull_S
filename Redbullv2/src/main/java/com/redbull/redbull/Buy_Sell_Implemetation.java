package com.redbull.redbull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Buy_Sell_Implemetation {

    private static final Logger logger = LoggerFactory.getLogger(Buy_Sell_Implemetation.class);

    public static void buyCE() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");

        handleToggleState(driver, "B", "CALL", "MKT.");
    }

    public static void buyPE() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");

        handleToggleState(driver, "S", "PUT", "MKT.");
    }

    private static void handleToggleState(WebDriver driver, String expectedB1, String expectedB2, String expectedB3) throws InterruptedException {
        // --- B1 Toggle Check (B or S) ---
        try {
            WebElement checkbox = driver.findElement(By.id("b/s_underlying|transactiontype_toggle|S"));
            boolean isSell = checkbox.isSelected();
            boolean shouldBeSell = expectedB1.equalsIgnoreCase("S");

            if (isSell != shouldBeSell) {
                WebElement label = driver.findElement(By.xpath("//label[@for='b/s_underlying|transactiontype_toggle|S']"));
                label.click();
                logger.info("✅ Toggled B1 (Buy/Sell) to '{}'", expectedB1);
                Thread.sleep(500);
            } else {
                logger.info("✅ B1 is already in expected state '{}'", expectedB1);
            }
        } catch (Exception e) {
            logger.error("🚫 Unable to locate or toggle B1 checkbox", e);
        }

        // --- B2: CALL/PUT Toggle ---
        try {
            WebElement b2Toggle = driver.findElement(By.id("b/s_underlying|optiontype_toggle|PE"));
            String currentB2 = b2Toggle.getText().trim();

            if (!currentB2.equalsIgnoreCase(expectedB2)) {
                b2Toggle.click();
                Thread.sleep(500);
                logger.info("✅ Toggled B2 (CALL/PUT) to '{}'", expectedB2);
            } else {
                logger.info("✅ B2 is already in expected state '{}'", expectedB2);
            }
        } catch (Exception e) {
            logger.error("🚫 Unable to locate or toggle B2 button", e);
        }

        // --- B3: MKT./LIMIT Toggle ---
        try {
            String toggleId = String.format("b/s_underlying|ordertype_toggle|%s", expectedB3.toUpperCase().replace(".", ""));
            WebElement b3Toggle = driver.findElement(By.id(toggleId));
            String currentB3 = b3Toggle.getText().trim();

            if (!currentB3.equalsIgnoreCase(expectedB3)) {
                b3Toggle.click();
                Thread.sleep(500);
                logger.info("✅ Toggled B3 (MKT./LIMIT) to '{}'", expectedB3);
            } else {
                logger.info("✅ B3 is already in expected state '{}'", expectedB3);
            }
        } catch (Exception e) {
            logger.error("🚫 Unable to locate or toggle B3 button", e);
        }
    }
}
