package com.redbull.redbull;

import java.time.Duration;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Angel_login_Process {
    
    private static final Logger log = LoggerFactory.getLogger(Angel_login_Process.class);

    public static void login() {
        System.out.println(InstructionMassages.login_class_Activated);
        log.info("Instruction message: " + InstructionMassages.login_class_Activated);

        WebDriver driver = WebDriverSingleton.getInstance(); // Browser Instance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            driver.get(Angel_Urls_and_Xpaths.Url_loginpage);
            log.info("Opened login page URL");

            // Wait and enter mobile number
            WebElement Mobileno_entry_box = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_EnterMobileNumber))
            );
            Mobileno_entry_box.sendKeys(Default_User_info.Mobile_Number);
            log.info("Entered mobile number");

            // Click on Proceed
            wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_mobile))
            ).click();
            log.info("Clicked on Proceed (Mobile)");

            // Wait for pin input field
            WebElement OTP_entry_box = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_OTP_entry))
            );
            
         try (// Ask user to enter OTP
			Scanner sc = new Scanner(System.in)) {
				System.out.println("PLEASE ENTER THE OTP:");
				String OTP = sc.nextLine();
				OTP_entry_box.sendKeys(OTP);
				log.info("Entered OTP");


				// Click on Proceed for OTP
				wait.until(
				    ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_OTP))
				).click();
				log.info("Clicked on Proceed (OTP)");
         
				// Click on PIN
				WebElement PIn_entry_box = wait.until(
				        ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_Pin_Entry))
				    );
				PIn_entry_box.sendKeys(Default_User_info.Angel_Pin);
				log.info("Entered pin number");
				
				// Clink on proceed after enter the pin
				
				
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_PIN))).click();
				log.info("Clicked on Proceed (PIN)");
				
				// click on Gotit 
				
//            Thread.sleep(5000);
				
//          driver.get(Angel_Urls_and_Xpaths.Url_CharPage);
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Gotit_Button))).click();
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Option_Introduction_NotNow))).click();
				log.info("Login process completd");
				System.out.println("Please confirm the chart setupdone with below things\n"+"Time frame\n"+"Indicators\n"+ "Algo setup\n"+"COnfirm with any singel digit Number");
				String Chart_confirmation = sc.next();
				System.err.println("Thanks for the confirmarin with number: "+ Chart_confirmation);
			}
            
        } catch (TimeoutException e) {
            log.error("Timeout while waiting for an element. Please check your network or page response time.", e);
            
        } catch (NoSuchElementException e) {
            log.error("Element not found. Possibly due to network delay or incorrect XPath.", e);
            log.info("Automation faild Please complete the login process maunally");
        } catch (Exception e) {
            log.error("Unexpected error occurred during login process.", e);
            log.info("Automation faild Please complete the login process maunally");
        }
    }

    public static void Papertrade_login() {
        System.out.println(InstructionMassages.login_class_Activated);
        log.info("Instruction message: " + InstructionMassages.login_class_Activated);

        WebDriver driver = WebDriverSingleton.getInstance(); // Browser Instance
        driver.get(Angel_Urls_and_Xpaths.Url_loginpage);
        log.info("Opened login page URL");

        try (Scanner sc = new Scanner(System.in)) {
            System.err.println("Please complete the Login process manually..... and post any number for confirmation");
            
            int Login_confirmation = getUserConfirmation(sc, "Login");
            log.info("Login confirmation received: " + Login_confirmation);
            
            int Chart_Confirmation = getUserConfirmation(sc, "Default chart setting");
            log.info("Chart setting confirmation received: " + Chart_Confirmation);
            
            int Time_Confirmation = getUserConfirmation(sc, "Time frame");
            log.info("Time frame confirmation received: " + Time_Confirmation);
            
            int ATM_Confirmation = getUserConfirmation(sc, "ATM Button");
            log.info("ATM button confirmation received: " + ATM_Confirmation);
        }
    }

    private static int getUserConfirmation(Scanner sc, String context) {
        int confirmation = -1;
        while (confirmation < 0) {
            try {
                System.err.println("Please confirm the " + context + ":");
                confirmation = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("⚠️ Invalid input. Please enter a number.");
                sc.next(); // Clear invalid input
            }
        }
        return confirmation;
    }
}