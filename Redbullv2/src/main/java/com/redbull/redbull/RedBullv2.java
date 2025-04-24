package com.redbull.redbull;

import java.time.Duration;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.dockerjava.api.model.Driver;

@SpringBootApplication
public class RedBullv2 {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RedBullv2 .class, args);
		System.out.println(InstructionMassages.greeting);
		System.out.println(InstructionMassages.Redbull_Activated);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Choose the Trading\n"+"1. Real Trade\n"+"2. Paper Trade\n"+ "3. Personal or Testing");
		int Choose_trade = sc.nextInt();
		switch (Choose_trade) {
		case 1: {
			Angel_login_Process.login();
	        StrategyRunner.runADX_FABStrategy();
		}
		case 2: {

			Angel_login_Process.login();
			StrategyRunner.runADX_FABStrategy();
		}
		case 3: {
			Angel_login_Process.login();
			WebDriver driver = WebDriverSingleton.getInstance();
			final Logger logger = LoggerFactory.getLogger(Buy_Sell_Implemetation.class);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			driver.get("https://www.angelone.in/trade/watchlist/chart");
			 	WebElement ATM_button = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strike_selection_toggle|true\"]")));
		        ATM_button.click();
//		        WebElement listItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("b/s_underlying|strikePrice_select|ATM|scrip_79634_2")));
		        Thread.sleep(3);
		        // Extract values
		        String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strikePrice_select|ATM|scrip_79634_2\"]/div/div[1]/span[2]"))).getText();
		        String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strikePrice_select|ATM|scrip_79634_2\"]/div/div[2]/span[1]"))).getText();
		        String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strikePrice_select|ATM|scrip_79634_2\"]/div/div[2]/span[2]"))).getText();
		        String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"b/s_underlying|strikePrice_select|ATM|scrip_79634_2\"]/div/div[3]/div/div/span/span[1]"))).getText();
		        logger.info(String.format("Strike: %s, ATM: %s, CE: %s, Price: %s", strikePrice, atmValue, contractType, price));
		        ATM_button.click();
			
//			
			}
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + Choose_trade);
		}
		
		
//		CAMU.Student_List();
	}

}
