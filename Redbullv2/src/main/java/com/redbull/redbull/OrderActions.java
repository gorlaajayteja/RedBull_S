package com.redbull.redbull;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderActions {

    private static final WebDriver driver = WebDriverSingleton.getInstance();
    private static final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

    public static boolean placeOrder(String stock, int lotSize, String actionType) {
        driver.get("https://www.angelone.in/trade/watchlist/chart");
        driver.switchTo().defaultContent();

        try {
            System.out.println("⚙️ RedBull warming up — preparing order panel for " + stock);
            Thread.sleep(5000);

            WebElement iframe1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='scrip chart']")));
            driver.switchTo().frame(iframe1);
            System.out.println("🔍 RedBull locked into chart view...");
            Thread.sleep(5000);

            WebElement iframe2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@title='Financial Chart']")));
            driver.switchTo().frame(iframe2);
            System.out.println("📈 Zooming into the financial chart for precise action...");
            Thread.sleep(5000);

            WebElement actionButton;
            if (actionType.equalsIgnoreCase("BUY")) {
                actionButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.apply-common-tooltip.button-hw_3o_pb.buyButton-hw_3o_pb")));
                System.out.println("🟢 Buy signal confirmed — RedBull ready to click!");
            } else if (actionType.equalsIgnoreCase("SELL")) {
                actionButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.apply-common-tooltip.button-hw_3o_pb.sellButton-hw_3o_pb")));
                System.out.println("🔴 Sell pressure detected — RedBull lining up the shot!");
            } else {
                System.err.println("❌ Invalid actionType: " + actionType);
                return false;
            }

            Thread.sleep(2000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", actionButton);
            actionButton.click();
            System.out.println("🎯 Action button clicked — target locked.");

            driver.switchTo().defaultContent();

            WebElement intradayBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("productType1")));
            intradayBtn.click();
            System.out.println("🔄 Switched to Intraday mode — rapid execution on the way.");

            WebElement quantityInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("quantityOrderPad")));
            quantityInput.clear();
            quantityInput.sendKeys(String.valueOf(lotSize));
            System.out.println("✍️ Lot size entered: " + lotSize);

            WebElement placeOrderButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("payNow")));
            placeOrderButton.click();
            Thread.sleep(2000);

            System.out.println("✅ RedBull just fired the " + actionType.toUpperCase() + " order on " + stock + "! 🎯");
            System.out.println("🚀 Trade launched — now let's ride the momentum!");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Oops! RedBull encountered a hiccup while placing " + actionType.toUpperCase() + " for " + stock);
            e.printStackTrace();
            return false;
        }
    }
}
