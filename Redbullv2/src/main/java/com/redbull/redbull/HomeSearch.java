package com.redbull.redbull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomeSearch {
    

	public static void searchStock(String stock) {
	    WebDriver driver = WebDriverSingleton.getInstance();
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

	    driver.get("https://www.angelone.in/trade/watchlist/chart");

	    // Wait for the search input to be present and enter the stock name
	    WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"watchlist-search\"]")));
	    searchBox.sendKeys(stock);

	    // Wait for the stock item to appear and click it
	    WebElement stockItem = wait.until(ExpectedConditions.elementToBeClickable(By.id("watchlist-DEFAULT-0")));
	    stockItem.click();

	    System.out.println("RedBull analysing " + stock+" Techincal....");
	}
    

    public static String getThirdFriday() {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Start with the current month
        LocalDate targetMonth = today.withDayOfMonth(1);

        // Step 1: Find third Friday of current month
        LocalDate firstFriday = targetMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate thirdFriday = firstFriday.plusDays(14); // 3rd Friday = first + 14 days

        // Step 2: If today is after 3rd Friday, shift to next month
        if (today.isAfter(thirdFriday)) {
            targetMonth = targetMonth.plusMonths(1).withDayOfMonth(1);
            firstFriday = targetMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            thirdFriday = firstFriday.plusDays(14);
        }

        // Format date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        return thirdFriday.format(formatter).toUpperCase();
    }

    public static void monitorIndex(String positionType, String stock, String strike, double thresold) throws InterruptedException {
        boolean isPositionOpen = true;
        System.out.println("👁️ RedBull is now guarding your " + positionType + " position on " + stock + " like a hawk!");
        System.out.println("📡 Monitoring live signals... waiting for the perfect exit 💼");

        while (isPositionOpen) {
            Thread.sleep(10000);
            HomeSearch.searchStock(stock);

            Map<String, Double> indicators = ChartReader_Frame2_Shift.getValueByTitle(ChartReader_Frame2_Shift.getIndicaters());

            double fisher = indicators.get("Fisher");
            double trigger = indicators.get("Trigger");
            double gap = Math.abs(fisher - trigger);

            System.out.printf("📊 FA (Fisher): %.2f | FB (Trigger): %.2f | GAP: %.2f%n", fisher, trigger, gap);

            boolean exitCondition = 
                (positionType.equals("CE") && fisher < trigger && gap > thresold) ||
                (positionType.equals("PE") && fisher > trigger && gap > thresold); // corrected PE logic

            if (exitCondition) {
                System.out.println("🚪 RedBull says: Exit signal detected! Smooth move, trend reversal confirmed.");
                System.out.println("🔄 Switching gears — exiting " + positionType + " and getting ready for the next big wave!");
                HomeSearch.searchStock(strike);
                Positions.Exit_postions();
                isPositionOpen = false;
            } else {
                System.out.println("🛡️ Holding strong — " + positionType + " position looks stable. No exit signal yet.");
                System.out.printf("💡 FA: %.2f | FB: %.2f | Confidence remains high, RedBull is watching 👀%n", fisher, trigger);

                // Add motivational refreshers randomly
                if ((int)(Math.random() * 4) == 0) {
                    System.out.println("🧘‍♂️ Remember: Emotions off. Logic on. Let RedBull guide the discipline.");
                }
            }
        }
        System.out.println("🏁 Exit complete — mission accomplished. RedBull is ready for the next adventure! 💪");
    }

    
}
