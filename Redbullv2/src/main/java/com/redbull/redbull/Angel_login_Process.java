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
        System.out.println("Instruction message: " + InstructionMassages.login_class_Activated);

        WebDriver driver = WebDriverSingleton.getInstance(); // Browser Instance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            driver.get(Angel_Urls_and_Xpaths.Url_loginpage);
            System.out.println("Opened login page URL");

            // Wait and enter mobile number
            WebElement Mobileno_entry_box = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_EnterMobileNumber))
            );
            Mobileno_entry_box.sendKeys("1234567890");
            System.out.println("Entered mobile number");

            // Click on Proceed
            wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_mobile))
            ).click();
            System.out.println("Clicked on Proceed (Mobile)");

            // Wait for pin input field
            WebElement OTP_entry_box = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_OTP_entry))
            );
            
         try (// Ask user to enter OTP
			Scanner sc = new Scanner(System.in)) {
				System.out.println("PLEASE ENTER THE OTP:");
				String OTP = sc.nextLine();
				OTP_entry_box.sendKeys(OTP);
				System.out.println("Entered OTP");


				// Click on Proceed for OTP
				wait.until(
				    ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_OTP))
				).click();
				System.out.println("Clicked on Proceed (OTP)");
				System.out.println();
         
				// Click on PIN
				WebElement PIn_entry_box = wait.until(
				        ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_Pin_Entry))
				    );
				PIn_entry_box.sendKeys("XXXX");
				System.out.println("Entered pin number");
				
				// Clink on proceed after enter the pin
				
				
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_PIN))).click();
				System.out.println("Clicked on Proceed (PIN)");
				
				// click on Gotit 
				
//            Thread.sleep(5000);
				
//          driver.get(Angel_Urls_and_Xpaths.Url_CharPage);
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Gotit_Button))).click();
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Option_Introduction_NotNow))).click();
				System.out.println("Login process completd");
				System.out.println("Please confirm the chart setupdone with below things\n"+"Time frame\n"+"Indicators\n"+ "Algo setup\n"+"COnfirm with any singel digit Number");
				String Chart_confirmation = sc.next();
				System.err.println("Thanks for the confirmarin with number: "+ Chart_confirmation);
			}
            
        } catch (TimeoutException e) {
            log.error("Timeout while waiting for an element. Please check your network or page response time.", e);
            
        } catch (NoSuchElementException e) {
            log.error("Element not found. Possibly due to network delay or incorrect XPath.", e);
            System.out.println("Automation faild Please complete the login process maunally");
        } catch (Exception e) {
            log.error("Unexpected error occurred during login process.", e);
            System.out.println("Automation faild Please complete the login process maunally");
        }
    }

    private static final Scanner scanner = new Scanner(System.in); // declare globally or reuse from RedBullv2

    public static void Papertrade_login() {
        System.out.println(InstructionMassages.login_class_Activated);
        WebDriver driver = WebDriverSingleton.getInstance(); // Browser Instance
        driver.get(Angel_Urls_and_Xpaths.Url_loginpage);
        System.out.println("Opened login page URL");

        System.err.println("Please complete the Login process manually..... and post any number for confirmation");

        int Login_confirmation = getUserConfirmation(scanner, "Login");
        System.out.println("Login confirmation received: " + Login_confirmation);

        int Chart_Confirmation = getUserConfirmation(scanner, "Default chart setting");
        System.out.println("Chart setting confirmation received: " + Chart_Confirmation);

        int Time_Confirmation = getUserConfirmation(scanner, "Time frame");
        System.out.println("Time frame confirmation received: " + Time_Confirmation);

        int ATM_Confirmation = getUserConfirmation(scanner, "ATM Button");
        System.out.println("ATM button confirmation received: " + ATM_Confirmation);
    }


    public static int getUserConfirmation(Scanner sc, String context) {
        int confirmation = 0;
        while (confirmation <= 0) {
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