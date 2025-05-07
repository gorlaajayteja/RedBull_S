package com.redbull.redbull;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class Xpath_Validator {

    public static void validateAllIndicators() {
        WebDriver driver = WebDriverSingleton.getInstance();
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        Scanner scanner = new Scanner(System.in);

        // ✅ Corrected: Only raw field names, not fully qualified with class name
        Map<String, String> indicatorToXpathField = new HashMap<>();
        indicatorToXpathField.put("MACD", "Xpath_MACDmiddel");
        indicatorToXpathField.put("MACDG", "Xpath_MACDGreen");
        indicatorToXpathField.put("MACDR", "Xpath_MACDRed");
        indicatorToXpathField.put("PDMI", "Xpath_PDMI");
        indicatorToXpathField.put("NDMI", "Xpath_NDMI");
        indicatorToXpathField.put("ADX", "Xpath_ADX");
        indicatorToXpathField.put("FA", "Xpath_FisherGreen");
        indicatorToXpathField.put("FB", "Xpath_FisherRed");

        Map<String, Double> indicators = new HashMap<>();

        for (Map.Entry<String, String> entry : indicatorToXpathField.entrySet()) {
            String indicatorName = entry.getKey();
            String xpathFieldName = entry.getValue();
            String xpathValue = getStaticFieldValue(Angel_Urls_and_Xpaths.class, xpathFieldName);

            Double value = null;
            while (value == null) {
                try {
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathValue)));
                    String text = element.getText().trim().replaceAll(",", "");
                    value = Double.parseDouble(text);
                    indicators.put(indicatorName, value);
                    System.out.println(indicatorName + " = " + value);

                    // If user had updated the XPath, keep the new one saved
                    setStaticFieldValue(Angel_Urls_and_Xpaths.class, xpathFieldName, xpathValue);

                } catch (Exception e) {
                    System.out.println("Failed to get value for " + indicatorName + ". Please enter a valid XPath:");
                    xpathValue = scanner.nextLine();
                }
            }
        }

        System.out.println("All indicators successfully validated:");
        indicators.forEach((k, v) -> System.out.println(k + ": " + v));

        scanner.close(); // Good practice
    }

    public static String getStaticFieldValue(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field: " + fieldName, e);
        }
    }

    public static void setStaticFieldValue(Class<?> clazz, String fieldName, String newValue) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, newValue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update field: " + fieldName, e);
        }
    }
}
