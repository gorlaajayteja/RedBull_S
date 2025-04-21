package com.redbull.redbull;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Positions {
	
    public static void Exit_postions () throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();

        WebElement postions_Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|position/Orders_List|scrip_99926000_1\"]")));
        postions_Tab.click();
        
        WebElement Postion_Window = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|positions_tab|scrip_99926000_1\"]")));
        Postion_Window.click();
        
        WebElement Exit_postions = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|position_exit|scrip_79197_2\"]")));
        Exit_postions.click();
    }
}
