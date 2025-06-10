package com.redbull.redbull;

public class Signals {

    public static void handleSignal(int lotSize, String stock, double adx, double green, double red, String signalType) throws InterruptedException {
//        System.err.println("📊 [Signal] handleSignal triggered for: " + stock + " with type: " + signalType + ", ADX: " + adx);

        boolean isBuySignal = signalType.equalsIgnoreCase("BUY");
        boolean isSellSignal = signalType.equalsIgnoreCase("SELL");

        if (!isBuySignal && !isSellSignal) {
            System.err.println("❌ Invalid signal type: " + signalType);
            return;
        }

        // === BUY Signal Logic ===
        if (isBuySignal) {
            if (StockList.buyList.contains(stock)) {
                System.out.println( stock + " is already flagged for BUY.");
                return;
            }

            if (StockList.sellList.contains(stock)) {
                if (green > red) {
                    // Step 1: Exit SELL position with BUY
                    boolean exitSuccess = OrderActions.placeOrder(stock, lotSize, "BUY");
                    if (exitSuccess) {
                        // Step 2: Only enter new BUY if ADX is strong
                        if (adx > 20) {
                            boolean entrySuccess = OrderActions.placeOrder(stock, lotSize, "BUY");
                            if (entrySuccess) {
                                StockList.sellList.remove(stock);
                                StockList.buyList.add(stock);
                                System.out.println("✅ " + stock + ": Boom! Trend flipped — SELL out, BUY in, RedBull’s charging full throttle!");
                            } else {
                                System.out.println("❌ BUY entry failed after exiting SELL.");
                            }
                        } else {
                            System.out.println("⚠️ ADX too weak, skipped BUY entry after exit.");
                            StockList.sellList.remove(stock);
                        }
                    } else {
                        System.out.println("❌ Failed to exit SELL. BUY entry aborted.");
                    }
                } else {
                    System.out.println("🔄 Staying in SELL mode.");
                }
                return;
            }

            // No current position — Only enter BUY if ADX is strong
            if (adx > 20 && OrderActions.placeOrder(stock, lotSize, "BUY")) {
                StockList.buyList.add(stock);
                System.out.println("🆕 Added " + stock + " to BUY list.");
            } else {
                System.out.println("❌ Skipped BUY entry due to weak ADX.");
            }

        }

        // === SELL Signal Logic ===
        else if (isSellSignal) {
            if (StockList.sellList.contains(stock)) {
                System.out.println("ℹ️  " + stock + " is already in the SELL list.");
                return;
            }

            if (StockList.buyList.contains(stock)) {
                if (green < red) {
                    // Step 1: Exit BUY position with SELL
                    boolean exitSuccess = OrderActions.placeOrder(stock, lotSize, "SELL");
                    if (exitSuccess) {
                        // Step 2: Only enter new SELL if ADX is strong
                        if (adx > 20) {
                            boolean entrySuccess = OrderActions.placeOrder(stock, lotSize, "SELL");
                            if (entrySuccess) {
                                StockList.buyList.remove(stock);
                                StockList.sellList.add(stock);
                                System.out.println("✅ " + stock + ": Boom! Trend flipped — BUY out, SELL in, RedBull’s charging full throttle!");
                            } else {
                                System.out.println("❌ SELL entry failed after exiting BUY.");
                            }
                        } else {
                            System.out.println("⚠️ ADX too weak, skipped SELL entry after exit.");
                            StockList.buyList.remove(stock);
                        }
                    } else {
                        System.out.println("❌ Failed to exit BUY. SELL entry aborted.");
                    }
                } else {
                    System.out.println("🔄 Staying in BUY mode.");
                }
                return;
            }

            // No current position — Only enter SELL if ADX is strong
            if (adx > 20 && OrderActions.placeOrder(stock, lotSize, "SELL")) {
                StockList.sellList.add(stock);
                System.out.println("🆕 Added " + stock + " to SELL list.");
            } else {
                System.out.println("❌ Skipped SELL entry due to weak ADX.");
            }
        }
    }
}
