package com.redbull.redbull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChartReader_Frame2_Shift {
    
    public static Map<String, Double> frameToframe() throws InterruptedException {
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

        // Collect indicator values 
        Map<String, Double> indicatorValues = collectIndicatorValues(driver);
        driver.switchTo().defaultContent();
        return indicatorValues;
    }
    
    public static Map<String, Double> collectIndicatorValues(WebDriver driver) {
       
        Map<String, Double> indicators = new HashMap<>();
//        indicators.put("MACD", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_MACDmiddel));
        indicators.put("MACDG", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_MACDGreen));
        indicators.put("MACDR", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_MACDRed));
        
        indicators.put("PDMI", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_PDMI));
        indicators.put("NDMI", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_NDMI));
        indicators.put("ADX", getValueByXPath( Angel_Urls_and_Xpaths.Xpath_ADX));
        
        // FA and FB using XPath
        indicators.put("FA", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_FisherGreen));
        indicators.put("FB", getValueByXPath(Angel_Urls_and_Xpaths.Xpath_FisherRed));
        
        return indicators;
    }
    
    public static double getValueByXPath(String xpath) {
    	WebDriver driver = WebDriverSingleton.getInstance();
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));

        WebElement element = null;
        try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
			    try {
			        element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			        if (element.isDisplayed()) {
			            String text = element.getText();
			            return parseValue(text);
			        }
			    } catch (NoSuchElementException e) {
			        System.out.println("❌ XPath failed: " + xpath);
			        System.out.print("Enter a new XPath for this element: ");
			        xpath = scanner.nextLine(); // Get new XPath from user
			    }
			}
		}
    }
    
    private static double parseValue(String text) {
        if (text.isEmpty()) {
            return -0; // Or handle accordingly
        }
        try {
            return Double.parseDouble(text.replace("−", "-").trim());
        } catch (NumberFormatException e) {
            return -0;
        }
    }
}