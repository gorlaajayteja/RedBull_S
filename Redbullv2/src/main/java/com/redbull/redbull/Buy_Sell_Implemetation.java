package com.redbull.redbull;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Buy_Sell_Implemetation {


    public static void buyCE(int lotSize) throws InterruptedException {
        executeBuy("CALL", lotSize);
    }

    public static void buyPE(int lotSize) throws InterruptedException {
        executeBuy("PUT", lotSize);
    }

    private static void executeBuy(String optionType, int lotSize) throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

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

            System.out.println("✅ Found Option Type Toggle | Text: {}"+ optionTypeText);
            if (!optionTypeText.equalsIgnoreCase(optionType)) {
                optionTypeToggle.click();
                System.out.println("🔁 Changed Option Type to {}"+ optionType);
            }

            System.out.println("✅ Found Order Type Toggle | Text: {}"+ orderTypeText);
            if (!orderTypeText.equalsIgnoreCase("MKT")) {
            		orderTypeToggle.click();
                System.out.println("🔁 Changed Order Type to MKT.");
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

            System.out.println(String.format("Strike: %s, ATM: %s, %s: %s", strikePrice, atmValue, contractType, price));
            ATM_button.click();
            
            for(int i=1;i<lotSize;i++) {
            WebElement triangleIcon = wait.until(ExpectedConditions.elementToBeClickable(
            	    By.cssSelector("span.icon-triangle-round.text-skin-neutralCaption.cursor-pointer")));
            	
            	triangleIcon.click();
            	System.out.println("lot Size --------------------------------: "+i);
            }

            // Step 1: Click the "BUY @" button using partial text match (more stable)
            WebElement buyButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'BUY @')]")));
            System.out.println("🟢 BUY Button Found: {}"+ buyButton.getText());
            Thread.sleep(2000);
            buyButton.click();

            // Step 2: Wait for "CONFIRM ORDER" button and click
            WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'CONFIRM ORDER')]")));
            
            confirmButton.click();
            Thread.sleep(2000);
            System.out.println("🟦 Confirm Order Button Found: {}" +confirmButton.getText());

        } catch (TimeoutException | NoSuchElementException e) {
        	System.out.println("❌ Element not found or not clickable. Reason: {}"+ e.getMessage());
        } catch (Exception e) {
        	System.out.println("❌ Unexpected error during buy{}: {}"+ optionType + e.getMessage());
        }
    }
}
