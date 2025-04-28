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
}