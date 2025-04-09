package com.redbull.redbull;

import java.time.Duration;
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

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

            // Wait for OTP input field
            WebElement OTP_entry_box = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(Angel_Urls_and_Xpaths.Xpath_OTP_entry))
            );

            // Ask user to enter OTP
            Scanner sc = new Scanner(System.in);
            System.out.println("PLEASE ENTER THE OTP:");
            String OTP = sc.nextLine();
            OTP_entry_box.sendKeys(OTP);
            log.info("Entered OTP");

            // Click on Proceed for OTP
            wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(Angel_Urls_and_Xpaths.Xpath_Clickon_Proceed_OTP))
            ).click();
            log.info("Clicked on Proceed (OTP)");

        } catch (TimeoutException e) {
            log.error("Timeout while waiting for an element. Please check your network or page response time.", e);
        } catch (NoSuchElementException e) {
            log.error("Element not found. Possibly due to network delay or incorrect XPath.", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred during login process.", e);
        }
    }
}
