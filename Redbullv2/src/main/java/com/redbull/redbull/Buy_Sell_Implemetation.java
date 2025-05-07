package com.redbull.redbull;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Buy_Sell_Implemetation {

    private static final Logger logger = LoggerFactory.getLogger(Buy_Sell_Implemetation.class);

    public static void buyCE(int lotSize) throws InterruptedException {
        executeBuy("CALL", lotSize);
    }

    public static void buyPE(int lotSize) throws InterruptedException {
        executeBuy("PUT", lotSize);
    }

    private static void executeBuy(String optionType, int lotSize) throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            driver.get("https://www.angelone.in/trade/watchlist/chart");
            driver.switchTo().defaultContent();

            // Toggle Option Type
            WebElement optionTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("[id^='b/s_underlying|optiontype_toggle']")));
            WebElement orderTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@id, 'ordertype_toggle')]")));

            String optionTypeText = optionTypeToggle.getText().trim();
            String orderTypeText = orderTypeToggle.getText().trim();

            logger.info("✅ Found Option Type Toggle | Text: {}", optionTypeText);
            if (!optionTypeText.equalsIgnoreCase(optionType)) {
                optionTypeToggle.click();
                logger.info("🔁 Changed Option Type to {}", optionType);
            }

            logger.info("✅ Found Order Type Toggle | Text: {}", orderTypeText);
            if (!orderTypeText.equalsIgnoreCase("LIMIT")) {
                orderTypeToggle.click();
                logger.info("🔁 Changed Order Type to MKT.");
            }

            // Select ATM strike
            WebElement ATM_button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='b/s_underlying|strike_selection_toggle|true']")));
            ATM_button.click();

            // Extract values
            String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@id,'strikePrice_select|ATM|scrip_')]/div/div[1]/span[2]"))).getText();
            String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@id,'strikePrice_select|ATM|scrip_')]/div/div[2]/span[1]"))).getText();
            String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@id,'strikePrice_select|ATM|scrip_')]/div/div[2]/span[2]"))).getText();
            String price = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@id,'strikePrice_select|ATM|scrip_')]/div/div[3]/div/div/span/span[1]"))).getText();

            logger.info(String.format("Strike: %s, ATM: %s, %s: %s", strikePrice, atmValue, contractType, price));
            ATM_button.click();
            
            System.err.println("Check lots size");
            Thread.sleep(5000);
            for(int i=1;i<lotSize;i++) {
            WebElement triangleIcon = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.cssSelector("span.icon-triangle-round.text-skin-neutralCaption.cursor-pointer")));
            	
            	triangleIcon.click();
            	System.out.println("lot Size --------------------------------: "+i);
            }

            // Step 1: Click the "BUY @" button using partial text match (more stable)
            WebElement buyButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'BUY @')]")));
            logger.info("🟢 BUY Button Found: {}", buyButton.getText());
            buyButton.click();

            // Step 2: Wait for "CONFIRM ORDER" button and click
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'CONFIRM ORDER')]")));
            logger.info("🟦 Confirm Order Button Found: {}", confirmButton.getText());
            confirmButton.click();

        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("❌ Element not found or not clickable. Reason: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected error during buy{}: {}", optionType, e.getMessage());
        }
    }
}
