package com.redbull.redbull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChartReader_Frame2_Shift {
	
	private static final Logger log = LoggerFactory.getLogger(ChartReader_Frame2_Shift.class);

    public static Map<String, Double> frameToframe() throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        System.out.println("Please update the changes in Char maualy");
        Thread.sleep(15000);
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
     
        // Switching to the first iframe where chart is expected to be located
        WebElement iframe1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
        driver.switchTo().frame(iframe1);
        log.info("Scirpt Chat Loaded");

        // Switching to the second nested iframe where financial details are displayed
        WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
        driver.switchTo().frame(iframe2);
        log.info("frame2 financial fram activated");
        

        
//        driver.switchTo().defaultContent();
//        log.info("Defult fram activated");
        
        
        // Prepare a map to hold the data
        Map<String, Double> indicators = new HashMap<>();
        
        // Collecting the data
        
        indicators.put("ADX", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_ADX));
        log.info("ADX Value: ");
        
        indicators.put("FisgerGreen", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_FisherGreen));
        log.info("Fisher Green: ");
        
        indicators.put("FisherRed", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_FisherRed));
        log.info("Fisher Red: ");
        
        indicators.put("RSI", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_RSI));
        log.info("RSI: ");
        
        indicators.put("ORBH", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_ORBHigh));
        log.info("ORBH: ");
        
        indicators.put("ORBL", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_ORBlow));
        log.info("ORBL: ");
        
        indicators.put("MACD", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_MACDmiddel));
        log.info("MACD: ");
        
        indicators.put("MACDG", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_MACDGreen));
        log.info("ORBL: ");
        
        indicators.put("MACDR", getIndicatorValue(wait, Angel_Urls_and_Xpaths.Xpath_MACDRed));
        log.info("ORBL: ");
        
        
        return indicators;
    }

    private static double getIndicatorValue(WebDriverWait wait, String xpath) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String numberString = element.getText().replace('−', '-'); // Replace non-standard negative sign
        return Double.parseDouble(numberString);
    }



}
