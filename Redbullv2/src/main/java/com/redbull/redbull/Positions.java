package com.redbull.redbull;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Positions {
	private static final Logger logger = LoggerFactory.getLogger(StrategyRunner.class);
    public static void Exit_postions () throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
//        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();
        WebElement ATM_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strike_selection_toggle|true\"]")));
        ATM_button.click();
//     // Extract values
        String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[1]/span[2]"))).getText();
        String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[1]"))).getText();
        String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[2]"))).getText();
        String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[3]/div/div/span/span[1]"))).getText();

        logger.info(String.format("Strike: %s, ATM: %s, CE: %s, Price: %s", strikePrice, atmValue, contractType, price));

//        WebElement postions_Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|position/Orders_List|scrip_99926000_1\"]")));
//        postions_Tab.click();
//        
//        WebElement Postion_Window = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|positions_tab|scrip_99926000_1\"]")));
//        Postion_Window.click();
//        
//        WebElement Exit_postions = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|position_exit|scrip_79197_2\"]")));
//        Exit_postions.click();
    }
}
