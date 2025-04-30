package com.redbull.redbull;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Xpath_Validator {
	public static void chart_Xpath_Validation() {
		 WebDriver driver = WebDriverSingleton.getInstance();
	        driver.get("https://www.angelone.in/trade/watchlist/chart");
	        
	        // Use Java 8's java.time.Duration (make sure you are using the correct Selenium dependency)
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

	        // Switching to the first iframe where the chart is expected to be located
	        WebElement iframe1 = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
	        driver.switchTo().frame(iframe1);

	        // Switching to the second nested iframe where financial details are displayed
	        WebElement iframe2 = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
	        driver.switchTo().frame(iframe2);
	        
	        
	        
	}

}
