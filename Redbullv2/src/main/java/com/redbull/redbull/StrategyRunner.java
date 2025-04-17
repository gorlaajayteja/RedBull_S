package com.redbull.redbull;

import java.util.Map;
import java.util.logging.Logger;

public class StrategyRunner {

    private static final Logger logger = Logger.getLogger(StrategyRunner.class.getName());

    public static void runADX_FABStrategy() throws InterruptedException {

        Map<String, Double> values;
        double adx = -1.0;

        // Step 1 & 2: Keep calling frameToframe() until ADX > 20
        while (adx <= 20) {
            values = ChartReader_Frame2_Shift.frameToframe(); // Fetch the latest values
            adx = values.getOrDefault("ADX", -1.0);

            logger.info("Current ADX: " + adx);

            if (adx <= 20) {
                logger.info("Waiting for ADX to cross above 20...");
                Thread.sleep(2000); // Wait 2 seconds before rechecking
            } else {
                double fa = values.getOrDefault("FA", -1.0);
                double fb = values.getOrDefault("FB", -1.0);

                logger.info("ADX is now above 20 ✅");
                logger.info("FA: " + fa);
                logger.info("FB: " + fb);

                // Step 4 & 5: Compare FA and FB, and call respective methods
                if (fa > fb) {
                    logger.info("📈 Buy CE Signal (FA > FB)");
                    Buy_Sell_Implemetation.buyCE();
                } else if (fa < fb) {
                    logger.info("📉 Buy PE Signal (FA < FB)");
                    Buy_Sell_Implemetation.buyPE();
                } else {
                    logger.info("⚖️ No Clear Signal (FA == FB)");
                }

                break; // Stop loop after signal
            }
        }
    }
}
