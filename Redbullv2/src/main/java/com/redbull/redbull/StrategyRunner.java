package com.redbull.redbull;

import java.util.Map;
import java.util.logging.Logger;

public class StrategyRunner {

    private static final Logger logger = Logger.getLogger(StrategyRunner.class.getName());

    public static void runADX_FABStrategy() throws InterruptedException {
        Map<String, Double> values;
        double adx = 0;
        double fa, fb;

        // Step 1: Wait until ADX > 20
        while (true) {
            values = ChartReader_Frame2_Shift.frameToframe();

            if (isInvalid(values, "ADX")) {
                logger.warning("❌ Failed to fetch ADX value. Retrying in 5 seconds...");
                Thread.sleep(5000);
                continue;
            }

            adx = values.get("ADX");
            logger.info("Current ADX: " + adx);

            if (adx > 20) {
                logger.info("ADX is now above 20 ✅");
                break;
            } else {
                logger.info("Waiting for ADX to cross above 20...");
                Thread.sleep(2000);
            }
        }

        // Step 2: Wait for a clear signal (FA ≠ FB)
        while (true) {
            values = ChartReader_Frame2_Shift.frameToframe();

            if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                logger.warning("❌ Failed to fetch FA/FB values. Retrying in 5 seconds...");
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

    // Step 3: Monitor for reverse signal to exit
    private static void monitorExit(String positionType) throws InterruptedException {
        boolean isPositionOpen = true;

        while (isPositionOpen) {
            Thread.sleep(2000);
            Map<String, Double> values = ChartReader_Frame2_Shift.frameToframe();

            if (isInvalid(values, "FA") || isInvalid(values, "FB")) {
                logger.warning("❌ Error fetching FA/FB during exit check. Retrying in 5 seconds...");
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
