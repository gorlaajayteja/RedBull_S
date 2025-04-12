package com.redbull.redbull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChartReader_Frame2_Shift {
	
	private static final Logger log = LoggerFactory.getLogger(ChartReader_Frame2_Shift.class);

    public static Map<String, Double> frameToframe() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
     
        // Switching to the first iframe where chart is expected to be located
        WebElement iframe1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
        driver.switchTo().frame(iframe1);
        System.out.println("Loading Frame 1............");

        // Switching to the second nested iframe where financial details are displayed
        WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
        driver.switchTo().frame(iframe2);
        System.out.println("Loading Frame 2............");
        
        
        driver.switchTo().defaultContent();
        
       
        
        // Prepare a map to hold the data
        Map<String, Double> indicators = new HashMap<>();
        
        // Collecting the data
        indicators.put("SuperTrend", getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[1]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div"));
        System.out.println("SuperTrend--> Loaded");
        
        indicators.put("RSI", getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[3]/div[2]/div/div[2]/div/div[2]/div[2]/div[2]/div/div[1]/div"));
        System.out.println("RSI --> Loaded");
        
        indicators.put("ADX", getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[5]/div[2]/div/div[1]/div/div[2]/div[2]/div[2]/div/div/div"));
        System.out.println("ADX Loaded");
        
        indicators.put("FA", getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[7]/div[2]/div/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/div"));
        System.out.println("Fisher A --> Loaded");
        
        indicators.put("FB",getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[7]/div[2]/div/div[1]/div/div[2]/div[2]/div[2]/div/div[2]/div"));
        System.out.println("FB --> Loaded");
        
        indicators.put("ORBH",getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[7]/div[2]/div/div[1]/div/div[2]/div[2]/div[2]/div/div[2]/div"));
        System.out.println("ORBH --> Loaded");
        
        indicators.put("ORBL",getIndicatorValue(wait, "/html/body/div[3]/div[3]/div[2]/div[1]/div[2]/div[2]/div[7]/div[2]/div/div[1]/div/div[2]/div[2]/div[2]/div/div[2]/div"));
        System.out.println("ORBL --> Loaded");
        
        return indicators;
    }

    private static double getIndicatorValue(WebDriverWait wait, String xpath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String numberString = element.getText().replace('−', '-'); // Replace non-standard negative sign
        return Double.parseDouble(numberString);
    }



}
