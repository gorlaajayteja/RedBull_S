package com.redbull.redbull;

import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.WebDriver;

public class RedBullv2 {
    static final Scanner scanner = new Scanner(System.in); // Single shared scanner

    public static void main(String[] args) {
        System.out.println("🚀 Welcome to RedBull Trader!");
        System.out.println("🚀 Choose Trading Type\n"+"1. NIFTY AND SENSEX\n"+"2. CRUDEOIL OR OTHER INDEX FOR OPTION'S\n"+"3. Stock's");
        int choice = scanner.nextInt();
        int lotSize = 3;
//        System.out.println("🧾 Lot size selected: " + lotSize);
//        StockList.buyList.add("ETERNAL");
//        StockList.buyList.add("ONGC");
//        StockList.sellList.add("ASHOKLEY");
//        StockList.sellList.add("WIPRO");

        try {
            switch (choice) {
                case 1:// NIFTY AND SENSEX
                    Angel_login_Process.Papertrade_login();
                    System.out.println("Boss! Please ensure the index and Chart setting... and confirm with YES");
                    String con = scanner.next();
                    System.out.println("Thannks for the confirmation.... Moveing to RedBull......"+con);
                    StrategyRunner.runADX_FABStrategy(lotSize);
                    
                    break;
                case 2: //CRUDEOIL
//                	System.out.println("Please provide the Index for Option Trading");
//                	String indexOp = scanner.next();
                	String indexOp = "CRUDEOIL MCX ";//+ HomeSearch.getThirdFriday();
                    Angel_login_Process.Papertrade_login();
                    StrategyRunner.otherIndexOP(lotSize, indexOp );
                    break;

                case 3://STOCKS
                    System.out.println("🟢 Stock Trading Mode Activated");
                    InstructionMassages.instructions();

                    // Load stock list
                    Set<String> stocks = StockListCollectFromUser.stocklist();
                    System.out.println("📃 Tracking Stocks: " + stocks);

                    // Add test stocks to Buy/Sell lists
                    StockList.buyList.add("Open Buy list:");
                    StockList.sellList.add("Open Sell list:");

                    // Open chart page
                    WebDriver driver = WebDriverSingleton.getInstance();
                    driver.get("https://www.angelone.in/trade/watchlist/chart");

                 // Wait for manual login
                    if (System.console() != null) {
                        System.out.println("🟡 Please complete login and press ENTER to continue...");
                        System.console().readLine();
                    } else {
                        System.out.println("⚠️ Console not available. Waiting 60 seconds...");
                        Thread.sleep(60000);
                    }
                    System.out.println("✅ Login confirmed — Starting signal cycles");


                    // Run signal detection in loop
                    for (int i = 0; i < 5000; i++) {
                        for (String stock : stocks) {
                            try {
                                System.err.println("🧠 RedBull engines on — decoding market behavior for: " + stock);
                                StrategyRunner.StockStategy(lotSize, stock); // One signal check per stock
                            } catch (Exception e) {
                                System.err.println("❌ Error processing stock " + stock + ": " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        System.out.println(StockList.buyList);
                        System.out.println(StockList.sellList);
                        System.out.println("**************************************************************************************************************" + i);
                        

                        Thread.sleep(10000); // Optional: wait before starting next cycle
                    }
                    break;

                default:
                    System.out.println("❗ Unexpected choice. Exiting.");
            }
        } catch (InterruptedException e) {
            System.out.println("⚠️ Execution interrupted: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("🚨 An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
