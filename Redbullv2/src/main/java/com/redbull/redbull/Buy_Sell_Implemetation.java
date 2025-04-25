package com.redbull.redbull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Buy_Sell_Implemetation {

    private static final Logger logger = LoggerFactory.getLogger(Buy_Sell_Implemetation.class);

    public static void buyCE() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();

        WebElement optionTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id^='b/s_underlying|optiontype_toggle']")));
        WebElement orderTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'ordertype_toggle')]")));
        String optionTypeText = optionTypeToggle.getText().trim();
        String orderTypeText = orderTypeToggle.getText().trim();
        logger.info("✅ Found Option Type Toggle | Text: {}", optionTypeText);
        logger.info("✅ Found Order Type Toggle | Text: {}", orderTypeText);
        if (!optionTypeText.equalsIgnoreCase("CALL")) {
            optionTypeToggle.click();
            logger.info("🔁 Changed Option Type to CALL");
        }

        if (!orderTypeText.equalsIgnoreCase("MKT.")) {
            orderTypeToggle.click();
            logger.info("🔁 Changed Order Type to MKT.");
        }
        
        WebElement ATM_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strike_selection_toggle|true\"]")));
        ATM_button.click();
//     // Extract values
        String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[1]/span[2]"))).getText();
        String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[1]"))).getText();
        String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[2]"))).getText();
        String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[3]/div/div/span/span[1]"))).getText();

        logger.info(String.format("Strike: %s, ATM: %s, CE: %s, Price: %s", strikePrice, atmValue, contractType, price));
        ATM_button.click();
   
//     // Step 1: Click the dynamic "BUY @" button
//        WebElement buyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[starts-with(@id, 'b/s_underlying|place_order|BUY_')]")));
//            
//        String buyText = buyButton.getText();
//        logger.info("🟢 BUY Button Found: {}", buyText);
////        buyButton.click();
//
//        // Step 2: Wait for "CONFIRM ORDER" button to appear
//        WebElement confirmButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'CONFIRM ORDER')]") ));
//       
//
//        // Optional: Verify it's enabled (sometimes appears disabled first)
//        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
//
//        // Step 3: Click "CONFIRM ORDER"
//        logger.info("🟦 Confirm Order Button Found: {}", confirmButton.getText());
////        confirmButton.click();
        
    }

    public static void buyPE() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();

        WebElement optionTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id^='b/s_underlying|optiontype_toggle']")));
        WebElement orderTypeToggle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id, 'ordertype_toggle')]")));      
        String optionTypeText = optionTypeToggle.getText().trim();
        String orderTypeText = orderTypeToggle.getText().trim();

        logger.info("✅ Found Option Type Toggle | Text: {}", optionTypeText);
        logger.info("✅ Found Order Type Toggle | Text: {}", orderTypeText);
        if (!optionTypeText.equalsIgnoreCase("PUT")) {
            optionTypeToggle.click();
            logger.info("🔁 Changed Option Type to PUT");
        }

        if (!orderTypeText.equalsIgnoreCase("MKT.")) {
            orderTypeToggle.click();
            logger.info("🔁 Changed Order Type to MKT.");
        }
        WebElement ATM_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strike_selection_toggle|true\"]")));
        ATM_button.click();
//     // Extract values
        String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[1]/span[2]"))).getText();
        String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[1]"))).getText();
        String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[2]"))).getText();
        String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[3]/div/div/span/span[1]"))).getText();

        logger.info(String.format("Strike: %s, ATM: %s, CE: %s, Price: %s", strikePrice, atmValue, contractType, price));
        ATM_button.click();

       
//        	   
//     // Step 1: Click the dynamic "BUY @" button
//        WebElement buyButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[starts-with(@id, 'b/s_underlying|place_order|BUY_')]")));
//            
//        String buyText = buyButton.getText();
//        logger.info("🟢 BUY Button Found: {}", buyText);
////*********Activate for real trade**************
////        buyButton.click();
//
//        // Step 2: Wait for "CONFIRM ORDER" button to appear
//        WebElement confirmButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'CONFIRM ORDER')]") ));
//       
//
//        // Optional: Verify it's enabled (sometimes appears disabled first)
//        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
//
//        // Step 3: Click "CONFIRM ORDER"
//        logger.info("🟦 Confirm Order Button Found: {}", confirmButton.getText());
////*********Activate for real trade**************
//        //        confirmButton.click();
        
        
    }
}
