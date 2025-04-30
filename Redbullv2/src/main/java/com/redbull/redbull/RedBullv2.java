package com.redbull.redbull;

import java.time.Duration;
import java.util.NoSuchElementException;
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

@SpringBootApplication
public class RedBullv2 {
    public static void main(String[] args) {
        SpringApplication.run(RedBullv2.class, args);
        System.out.println(InstructionMassages.greeting);
        System.out.println(InstructionMassages.Redbull_Activated);

        Scanner sc = new Scanner(System.in);
        System.out.println("1. ADX_Fisher\n2. DMI, MACD, Fisher");
        int chooseStrgy = sc.nextInt();
        int chooseTrade;

        // Validate user input
        while (true) {
            System.out.println("Please Choose the Trading:\n1. Real Trade\n2. Paper Trade\n3. Personal or Testing");
            if (sc.hasNextInt()) {
                chooseTrade = sc.nextInt();
                if (chooseTrade >= 1 && chooseTrade <= 3) {
                    break;
                }
            }
            System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            sc.nextLine(); // Clear invalid input
        }

        try {
            switch (chooseTrade) {
                case 1:
                    Angel_login_Process.login();
                    StrategyRunner.runADX_FABStrategy();
                    break;

                case 2:
                    Angel_login_Process.Papertrade_login();
//                    ChartReader_Frame2_Shift.validateXPaths();
                    if (chooseStrgy == 1) {
                        StrategyRunner.runADX_FABStrategy();
                    } else if (chooseStrgy == 2) {
                    	
                        StrategyRunner.DMI_MACD_FI();
                    } else {
                        System.out.println("Invalid choice. Please select a valid strategy.");
                    }
                    
                    
                case 3:
                    Angel_login_Process.login();
                    WebDriver driver = WebDriverSingleton.getInstance();
                    final Logger logger = LoggerFactory.getLogger(Buy_Sell_Implemetation.class);
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                    driver.get("https://www.angelone.in/trade/watchlist/chart");

                    // Wait for ATM button to be clickable
                    WebElement ATM_button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='b/s_underlying|strike_selection_toggle|true']")));
                    ATM_button.click();
                   
                    
                    
                    
                    // Extract values with explicit waits
                    String atmValue = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[1]/span[2]"))).getText();
                    String strikePrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[1]"))).getText();
                    String contractType = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[2]/span[2]"))).getText();
                    String price = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'b/s_underlying|strikePrice_select|ATM|scrip_')]/div/div[3]/div/div/span/span[1]"))).getText();

                    logger.info(String.format("Strike: %s, ATM: %s, CE: %s, Price: %s", strikePrice, atmValue, contractType, price));
                    ATM_button.click();
                    
                    break;

                default:
                    System.out.println("Unexpected error in trade selection.");
            }
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close(); // Close scanner to prevent resource leaks
        }
    }
}