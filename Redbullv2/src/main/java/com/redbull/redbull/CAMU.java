package com.redbull.redbull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CAMU {

    public static void Student_List() throws InterruptedException { 
        WebDriver driver = WebDriverSingleton.getInstance(); // Browser Instance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Navigate to the login page
        driver.get("https://teacher.camu.in/");
        WebElement userName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"uname\"]")));
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pwd\"]")));
        WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"loginForm\"]/div[1]/button")));

        // Input credentials
        userName.sendKeys("Sateeshkumar.naga@woxsen.edu.in");
        password.sendKeys("Woxsen@1234");
        login.click(); // Click the login button

        Thread.sleep(10000); // Wait for login to complete

        // List of roll numbers as Strings
        List<String> roll_no = Arrays.asList(
            "67a52d63047045dd6e5345cb",
            "6777aa9cca720e36033d5e9a",
            "67a52d63b72c98a9b49f4604",
            "676a7f3e523a38e238a0ed28",
            "678e455afb902293061d954c",
            "67b06cc5435a591b8b116891",
            "676e29c92dedfc4b7cf8a750",
            "67761d75538f22114eeb36d7",
            "67761d752fa3fe9c81183b25",
            "67624c4d2f372c112336c1b1",
            "67a52d6436e101af5a7bce1e",
            "676f7edda293935c8152ce8d",
            "67a529fbb72c98a9b49f45e1",
            "6762b2422f372c112337d920",
            "677ae4ca26cff884571e3d54",
            "6764f656218e5e58518d0d4a",
            "67a51f5436e101af5a7bcd98",
            "678e45591e2a9cbf8409f23e",
            "67a5265db72c98a9b49f45bd",
            "676e29c995d37bd64081e5af",
            "67b827fc5cc84774caad366e",
            "67723e06e5013d360376d5c0",
            "67a5265c36e101af5a7bcdf2",
            "6787607f9f6cf1eae361ff98",
            "67723e05e5013d360376d5b3",
            "67a5265cb72c98a9b49f45ab",
            "677cb15300148d0d5c6495a8",
            "6789e3deaabe5df4f5910a3d",
            "677cadf1a42ade5cb4513e48",
            "67888ed53876c7ced6215c40",
            "67875d1efae35881ead57a0b",
            "67a51f54b72c98a9b49f454c",
            "67b5b2cd228cbdc3d174264b",
            "67875d1d994a323976efa9a7",
            "67875d1df9887a82ccf07ee0",
            "6780a5d360c8e1b164468eef",
            "67a9880087c574b5b87a4724",
            "677aa1fc1e7c03f394d485c5",
            "67bbf5e68fa3e8cdcee58cf5",
            "67a50dede2404887b47edb6b",
            "67875d1926f4243bd5a99c95",
            "67a50dedfc70a6bb6e957ab9",
            "67a50ded047045dd6e53431a",
            "676cd84d31703bc95c27751a"
        );

        // Loop through each roll number and perform operations
        for (String roll : roll_no) {
            // Navigate to student list page
            driver.get("https://teacher.camu.in/index#/student?view=Student&id=" + roll);

            WebElement roll_number = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AplnNum")));
            String Roll = roll_number.getAttribute("value");
            WebElement Adm_number = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"AdmNum\"]")));
            Adm_number.clear();
            Adm_number.sendKeys(Roll);

            WebElement finsh = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"saveCntnt\"]/div[2]/span[2]/button/span")));
            finsh.click();

            // Wait for the alert to be present and handle it
//            wait.until(ExpectedConditions.alertIsPresent());
//            Alert alert = driver.switchTo().alert();
//            System.out.println("Alert text: " + alert.getText()); // Print alert text
//
//            // Accept the alert (click "OK")
//            alert.accept();
//
//            // Log the process for the current roll
            System.out.println("Processing roll number: " + Roll);
            Thread.sleep(5000); // Example action delay

            // Add any additional logic required for processing each roll number
        }
    }
}