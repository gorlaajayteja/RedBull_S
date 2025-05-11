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
	public static void Exit_postions() throws InterruptedException {
	    WebDriver driver = WebDriverSingleton.getInstance();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

	    try {
	        driver.switchTo().defaultContent();

	        WebElement postions_Tab = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[@id=\"b/s_underlying|position/Orders_List|scrip_99926000_1\"]")));
	        postions_Tab.click();

	        WebElement Postion_Window = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[@id=\"b/s_underlying|positions_tab|scrip_99926000_1\"]")));
	        Postion_Window.click();

	        WebElement Exit_postions = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                By.xpath("//*[@id=\"appContainer\"]/main/div/div[2]/div/div[2]/div/div[3]/div/div[5]/div[1]/div[2]/div[2]/div/div[1]/button")));
	        Exit_postions.click();

	        System.err.println("Positions exited completed");

	    } catch (Exception e) {
	        System.err.println("Exception occurred while exiting positions: " + e.getMessage());
//	        repeat();  // Call your retry or recovery logic
	    }
	}

	// Your repeat method (stub example)
	public static void repeat() {
	    System.err.println("Retry logic triggered...");
	    // Implement your recovery or retry logic here
	}

}
