package com.redbull.redbull;

import java.time.LocalTime;
import java.time.ZoneId;

public class timeZone {
	private static final LocalTime MARKET_OPEN = LocalTime.of(9, 15);
    private static final LocalTime MARKET_CLOSE = LocalTime.of(15, 15);
    private static final ZoneId INDIAN_ZONE = ZoneId.of("Asia/Kolkata");
    
    public static boolean isMarketOpen() {
        LocalTime now = LocalTime.now(INDIAN_ZONE);
        return !now.isBefore(MARKET_OPEN) && !now.isAfter(MARKET_CLOSE);
    }

    public static void sleepUntilMarketOpens() {
        long sleepTime = calculateTimeUntilMarketOpens();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private static long calculateTimeUntilMarketOpens() {
        LocalTime now = LocalTime.now(INDIAN_ZONE);
        long millisecondsUntilOpen = 0;
        if (now.isAfter(MARKET_CLOSE) || now.isBefore(MARKET_OPEN)) {
            LocalTime nextOpenTime = now.isAfter(MARKET_CLOSE) ? MARKET_OPEN.plusHours(24) : MARKET_OPEN;
            millisecondsUntilOpen = java.time.Duration.between(now, nextOpenTime).toMillis();
        }
        return millisecondsUntilOpen;
    }
}

