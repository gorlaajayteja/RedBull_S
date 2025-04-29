package com.redbull.redbull;

import java.time.LocalTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyRunner {

	private static final Logger logger = LoggerFactory.getLogger(StrategyRunner.class);


    public static void runADX_FABStrategy() throws InterruptedException {
        Map<String, Double> values;
        double adx, fa, fb;

        // Define the market close time
        LocalTime marketCloseTime = LocalTime.of(15, 30); // Market closes at 3:30 PM

        // Continuous execution until the market closes
        while (LocalTime.now().isBefore(marketCloseTime)) {
            logger.info("Starting a new strategy cycle...");

            // Step 1: Wait until ADX > 20
            while (true) {
                values = ChartReader_Frame2_Shift.frameToframe();

                if (isInvalid(values, "ADX")) {
                    logger.warn("❌ Failed to fetch ADX value. Retrying in 10 seconds...");
                    Thread.sleep(10000);
                    continue;
                }

                adx = values.get("ADX");
                logger.info("Current ADX: " + adx);

                if (adx > 20) {
                    logger.info("ADX is now above 20 ✅");
                    break;
                } else {
                    logger.info("Waiting for ADX to cross above 20...");
                    Thread.sleep(20000);
                }
            }

            // Step 2: Wait for a clear signal (FA ≠ FB)
            while (true) {
                values = ChartReader_Frame2_Shift.frameToframe();

                if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                    logger.warn("❌ Failed to fetch FA/FB values. Retrying in 5 seconds...");
                    Thread.sleep(5000);
                    continue;
                }

                fa = values.get("FA");
                fb = values.get("FB");

                if (fa > fb) {
                    logger.info("📈 Buy CE Signal (FA > FB)");
                    Buy_Sell_Implemetation.buyCE();
                    monitorExit("CE");
                    break;

                } else if (fa < fb) {
                    logger.info("📉 Buy PE Signal (FA < FB)");
                    Buy_Sell_Implemetation.buyPE();
                    monitorExit("PE");
                    break;

                } else {
                    logger.info("⚖️ No Clear Signal (FA == FB), waiting...");
                    Thread.sleep(5000);
                }
            }
        }

        logger.info("Market closed. Terminating strategy execution.");
    }

    // Step 3: Monitor for reverse signal to exit
    private static void monitorExit(String positionType) throws InterruptedException {
        boolean isPositionOpen = true;

        while (isPositionOpen) {
            Thread.sleep(10000);
            Map<String, Double> values = ChartReader_Frame2_Shift.frameToframe();

            if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                logger.warn("❌ Error fetching FA/FB during exit check. Retrying in 5 seconds...");
                Thread.sleep(5000);
                continue;
            }

            double fa = values.get("FA");
            double fb = values.get("FB");

            if ((positionType.equals("CE") && fa < fb) || (positionType.equals("PE") && fa > fb)) {
                logger.info("🔁 Exit Signal Detected! Exiting position...");
                Positions.Exit_postions();
                isPositionOpen = false;
            } else {
                logger.info("⏳ Holding " + positionType + " - FA: " + fa + ", FB: " + fb);
            }
        }
    }

    // Helper method to check if value is invalid (null or -1.0)
    private static boolean isInvalid(Map<String, Double> map, String key) {
        return map == null || !map.containsKey(key) || map.get(key) == 0;
    }
    public static void DMI_MACD_FI() throws InterruptedException {
        Map<String, Double> values;
        double ADX, MACD, MACDG, MACDR, PDMI, NDMI, FA, FB;
        boolean isTradeInitiated = false;
        String positionType = null; // Track whether the position is CE or PE

        // Define the market close time
        LocalTime marketCloseTime = LocalTime.of(15, 30); // Market closes at 3:30 PM

        // Continuous execution until the market closes
        while (LocalTime.now().isBefore(marketCloseTime)) {
            logger.info("Starting a new strategy cycle...");

            values = ChartReader_Frame2_Shift.frameToframe();

            // Validate required indicators
            if (isInvalid(values, "ADX") || isInvalid(values, "MACDG") ||
                isInvalid(values, "MACD") || isInvalid(values, "PDMI") ||
                isInvalid(values, "NDMI") || isInvalid(values, "FA") ||
                isInvalid(values, "MACDR") || isInvalid(values, "FB")) 
            {
                logger.warn("❌ Failed to fetch one or more indicator values. Retrying in 10 seconds...");
                Thread.sleep(10000);
                continue;
            }

            // Fetch the required values
            ADX = values.get("ADX");
            MACD = values.get("MACD");
            MACDG = values.get("MACDG");
            MACDR = values.get("MACDR");
            NDMI = values.get("NDMI");
            PDMI = values.get("PDMI");
            FA = values.get("FA");
            FB = values.get("FB");

            // Check if ADX indicates a trending market
            if (ADX > 20) {
                logger.info("✅ Trending Market Detected (ADX > 20)");

                // Check for bullish condition (MACD line > MACD signal, DMI+ > DMI-, FA > FB)
                if (MACDG > MACDR && PDMI > NDMI && FA > FB) {
                    logger.info("📈 Bullish Signal Detected");
                    if (!isTradeInitiated) {
                        Buy_Sell_Implemetation.buyCE(); // Execute Buy Call
                        positionType = "CE"; // Set position type to CE
                        isTradeInitiated = true;
                    }
                }
                // Check for bearish condition (MACD line < MACD signal, DMI+ < DMI-, FA < FB)
                else if (MACDG < MACDR && PDMI < NDMI && FA < FB) {
                    logger.info("📉 Bearish Signal Detected");
                    if (!isTradeInitiated) {
                        Buy_Sell_Implemetation.buyPE(); // Execute Buy Put
                        positionType = "PE"; // Set position type to PE
                        isTradeInitiated = true;
                    }
                } else {
                    logger.info("⚖️ No Clear Signal Detected. Monitoring...");
                }
            } else {
                logger.info("Waiting for ADX to rise above 20 to identify a trending market...");
            }

            // Monitor Exit Signal
            if (isTradeInitiated && positionType != null) {
                monitorExit(positionType); // Pass the position type to monitorExit
            }

            Thread.sleep(10000); // Pause before the next strategy cycle
        }

        logger.info("Market closed. Terminating DMI_MACD_FI strategy execution.");
       }
}