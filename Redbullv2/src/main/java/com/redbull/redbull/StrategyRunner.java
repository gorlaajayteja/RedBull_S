package com.redbull.redbull;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class StrategyRunner {
	public static double threshold = 0.5;

	private static final Logger logger = LoggerFactory.getLogger(StrategyRunner.class);


	public static void runADX_FABStrategy(int lotSize) throws InterruptedException {
	    Double adx;

	    // Define the market close time
	    LocalTime marketCloseTime = LocalTime.of(23, 30); // Market closes at 3:30 PM

	    // Continuous execution until the market closes
	    while (LocalTime.now().isBefore(marketCloseTime)) {
	        System.out.println("🧠 RedBull thinking deeply about the next big move...");

	        // Step 1: Wait until ADX > 20
	        while (true) {
	            List<String> onlyADX = Arrays.asList("ADX");
	            Map<String, Double> values = ChartReader_Frame2_Shift.getValueByTitle(onlyADX);
	            adx = values.get("ADX");
	            System.out.println("📡 Scanning ADX... Current ADX: " + adx);

	            if (adx > 20) {
	                System.out.println("🚀 Boom! ADX is above 20: " + adx + " — Trend strength confirmed!");
	                break;
	            } else {
	                String[] messages = {
	                    "🧘‍♂️ Hold tight Boss! ADX still chilling under 20. Patience builds fortune...",
	                    "🎯 No rush! RedBull is watching — waiting for the real trend ignition...",
	                    "⏳ ADX not ready yet, but we're staying sharp! Current ADX: " + adx,
	                    "💡 Relax, this is where discipline pays. Trust the setup, not the noise!",
	                    "📉 Low ADX? Meh. Let's sip that coffee and wait like a sniper..."
	                };
	                int index = (int) (Math.random() * messages.length);
	                System.out.println(messages[index]);
	                Thread.sleep(20000);
	            }
	        }

	        // Step 2: Wait for a clear signal (FA ≠ FB)
	        while (true) {
	            Map<String, Double> indicator = ChartReader_Frame2_Shift.getValueByTitle(ChartReader_Frame2_Shift.getIndicaters());
	            double gap = Math.abs(indicator.get("Fisher") - indicator.get("Trigger"));

	            if ((indicator.get("Fisher") > indicator.get("Trigger") && gap > threshold)) {
	                System.out.println("💥 RedBull's locked on: FA > FB ➡️ CE Attack Mode!");
	                Buy_Sell_Implemetation.buyCE(lotSize);
	                monitorExit("CE");
	                break;

	            } else if (indicator.get("Fisher") < indicator.get("Trigger") && gap > threshold) {
	                System.out.println("💣 FA < FB — PE Firestorm Incoming! Let's ride the wave!");
	                Buy_Sell_Implemetation.buyPE(lotSize);
	                monitorExit("PE");
	                break;

	            } else {
	                String[] waitingMsgs = {
	                    "🔍 Hmm... No clear divergence yet. RedBull's still on it!",
	                    "⚖️ Signal not ripe yet — staying cool, Boss. We don’t chase noise.",
	                    "🧠 Watching the FA/FB dance... The moment will strike soon!",
	                    "⏳ No magic yet — RedBull is calm, focused, and tuned in."
	                };
	                int index = (int) (Math.random() * waitingMsgs.length);
	                System.out.println(waitingMsgs[index]);
	                Thread.sleep(5000);
	            }
	        }
	    }

	    System.out.println("📴 Market closed — Strategy ended. Great job today, Boss! 💼✨");
	}


    // Step 3: Monitor for reverse signal to exit
    private static void monitorExit(String positionType) throws InterruptedException {
        boolean isPositionOpen = true;
        System.out.println("👁️ RedBull is on guard duty — watching your " + positionType + " position closely for exit signals.");
        System.out.println("🧠 No worries — RedBull's got the discipline, you stay focused. Monitoring...");

        while (isPositionOpen) {
            Thread.sleep(10000);

            Map<String, Double> fa = ChartReader_Frame2_Shift.getValueByTitle(ChartReader_Frame2_Shift.getIndicaters());

            double fisher = fa.get("Fisher");
            double trigger = fa.get("Trigger");
            double gap = Math.abs(fisher - trigger);

            System.out.printf("📊 FA (Fisher): %.2f | FB (Trigger): %.2f | GAP: %.2f%n", fisher, trigger, gap);

            boolean exitCondition = 
                (positionType.equals("CE") && fisher < trigger && gap > threshold) ||
                (positionType.equals("PE") && fisher > trigger && gap > threshold);  // corrected PE logic

            if (exitCondition) {
                System.out.println("🚪 Exit signal confirmed! RedBull says it's safe to step back.");
                System.out.println("✅ Position closed with discipline — trend reversed, and we caught the ride just right! 🎯");
                Positions.Exit_postions();
                isPositionOpen = false;
            } else {
                System.out.println("⏳ Still holding " + positionType + " — no clear exit yet. RedBull is patiently watching...");

                // Refreshing friendly messages
                if ((int)(Math.random() * 3) == 0) {
                    System.out.println("🌈 Stay calm — exits are part of the strategy, not the emotion. RedBull = zero fear.");
                }
            }
        }

        System.out.println("🏁 All clear — RedBull has exited the position safely. On to the next setup! 🔍📈");
    }

    
    public static void DMI_MACD_FI() throws InterruptedException, TimeoutException {
        Map<String, Double> values;
        
        int lotSize = 0;
        
        boolean isTradeInitiated = false;
        String positionType = null;

        // Define market close time
        LocalTime marketCloseTime = LocalTime.of(23, 30); // Market closes at 11:30 PM


        while (LocalTime.now().isBefore(marketCloseTime)) {
            System.out.println("Starting a new strategy cycle...");
            values = ChartReader_Frame2_Shift.frameToframe();

            if (!areIndicatorsValid(values)) {
                logger.warn("❌ One or more indicators are missing. Retrying in 10 seconds...");
                Thread.sleep(10000);
                continue;
            }

            executeTradeStrategy(values, isTradeInitiated, positionType, lotSize);
        }

        System.out.println("Market closed. Terminating execution.");
    }


    // **Indicator Health Check**
    private static boolean areIndicatorsValid(Map<String, Double> values) {
        return values != null && values.entrySet().stream()
            .allMatch(entry -> entry.getValue() != null && entry.getValue() != -0);
    }

    // **Trade Execution Logic**
    private static void executeTradeStrategy(Map<String, Double> values, boolean isTradeInitiated, String positionType, int lotSize) throws InterruptedException {
        double ADX = values.get("ADX");
        double MACDG = values.get("MACDG");
        double MACDR = values.get("MACDR");
        double PDMI = values.get("PDMI");
        double NDMI = values.get("NDMI");
        double FA = values.get("FA");
        double FB = values.get("FB");

        // **Trending Market Check (ADX > 20)**
        if (ADX > 20) {
            System.out.println("✅ Trending Market Detected (ADX > 20)");

            // **Bullish Condition Check**
            if (MACDG > MACDR && PDMI > NDMI && FA > FB) {
                System.out.println("📈 Bullish Signal Detected");
                System.out.println("MACDG: " + MACDG + ", MACDR: " + MACDR + ", PDMI: " + PDMI + ", NDMI: " + NDMI + ", FA: " + FA + ", FB: " + FB);

                if (!isTradeInitiated) {
                    Buy_Sell_Implemetation.buyCE(lotSize); // Execute Buy Call
                    positionType = "CE";
                    isTradeInitiated = true;
                }
            }
            // **Bearish Condition Check**
            else if (MACDG < MACDR && PDMI < NDMI && FA < FB) {
                System.out.println("📉 Bearish Signal Detected");
                System.out.println("MACDG: " + MACDG + ", MACDR: " + MACDR + ", PDMI: " + PDMI + ", NDMI: " + NDMI + ", FA: " + FA + ", FB: " + FB);

                if (!isTradeInitiated) {
                    Buy_Sell_Implemetation.buyPE(lotSize); // Execute Buy Put
                    positionType = "PE";
                    isTradeInitiated = true;
                }
            } else {
                System.out.println("⚖️ Silence from the market — it’s thinking. Meanwhile, we wait like pros.");
                Thread.sleep(30000);
            }
        } else {
            System.out.println("Patience mode on! ADX is our gatekeeper to trend confirmation.");
        }

        // **Trade Exit Monitoring**
        if (isTradeInitiated && positionType != null) {
            monitorExit(positionType); // Pass position type to monitorExit()
        }
    }

    public static void StockStategy(int lotSize, String stock) throws InterruptedException {
        System.out.println("RedBull is hunting for signals in: " + stock + " — stay sharp!");

        // Step 1: Search the stock (switch chart etc.)
        HomeSearch.searchStock(stock);

        // Step 2: Fetch all indicator values
        Map<String, Double> res = ChartReader_Frame2_Shift.getValueByTitle(ChartReader_Frame2_Shift.getIndicaters());

        Double adx    = res.get("ADX");
        Double green  = res.get("Fisher");
        Double red    = res.get("Trigger");
        double gap = Math.abs(green - red);


//        System.out.println("📊 ADX: " + adx + ", Fisher (Green): " + green + ", Trigger (Red): " + red + " Gap :"+ gap);

        boolean adxStrong    = adx > 20;
        boolean isBuySignal  = green > red;
        boolean isSellSignal = red > green;
        boolean isgap = gap > threshold;
        

        // ✅ ENTRY logic (only if ADX is strong)
        if (adxStrong) {
            if (isBuySignal && isgap) {
                System.out.println("RedBull says GO! Long signal confirmed — launching the attack!");
                Signals.handleSignal(lotSize, stock, adx, green, red, "BUY");
            } else if (isSellSignal && isgap) {
                System.out.println("RedBull’s striking hard — SHORT signal locked and loaded!");
                Signals.handleSignal(lotSize, stock, adx, green, red, "SELL");
            } else {
                System.out.println("⚖️ Signal gridlock! Fisher equals Trigger — RedBull holding fire.");
            }
            return; // exit to avoid checking exit logic again
        }

        // ✅ EXIT-ONLY logic when ADX is weak
        System.out.println("🔻 ADX too low — RedBull won’t enter blind. Eyes on exit signals now.");

        if (StockList.buyList.contains(stock) && isSellSignal && isgap) {
            Signals.handleSignal(lotSize, stock, adx, green, red, "SELL");
            System.out.println("SELL signal detected. BUY exited cleanly — RedBull's protecting your capital!");
        }

        if (StockList.sellList.contains(stock) && isBuySignal && isgap) {
            Signals.handleSignal(lotSize, stock, adx, green, red, "BUY");
            System.out.println("🚪 BUY wrapped up — reversal spotted. Don’t worry, RedBull’s protecting your gains!");
        }

        System.out.println("✅ Exit-only check completed for: " + stock);
    }

    public static void otherIndexOP(int lotSize, String stock) throws InterruptedException {
        WebDriver driver = WebDriverSingleton.getInstance();
        System.out.println("🧠 RedBull engaged — analyzing " + stock + " like a pro!");
        HomeSearch.searchStock(stock);

        System.out.println("🔍 Scanning for high-probability entry signals...");
        Thread.sleep(2000);
        System.out.println("☕ Grab a sip — RedBull is on market watch duty!");

        while (true) {
            Map<String, Double> indicators = ChartReader_Frame2_Shift.getValueByTitle(ChartReader_Frame2_Shift.getIndicaters());
            double adx     = indicators.get("ADX");
            double fisher  = indicators.get("Fisher");
            double trigger = indicators.get("Trigger");
            double gap     = Math.abs(fisher - trigger);

            System.out.printf("📊 RedBull scan → ADX: %.2f | Fisher: %.2f | Trigger: %.2f | GAP: %.2f%n", adx, fisher, trigger, gap);

            if (!isValidEntry(adx, gap)) {
                System.out.printf("⏳ Patience pays — ADX (%.2f) or GAP (%.2f) not ready. RedBull is calmly monitoring...%n", adx, gap);
                Thread.sleep(5000);

                // Refresher message every few loops
                if ((int)(Math.random() * 5) == 0) {
                    System.out.println("🧘‍♂️ Stay chill — No rush, precision over impulse. RedBull’s doing the heavy lifting!");
                }

                continue;
            }

            String[] strikeInfo = getStrikeInfo(driver);
            String call = strikeInfo[0];
            String put = strikeInfo[1];
            String exWeek = strikeInfo[2];

            if (fisher > trigger) {
                String callsreach = stock + " " + call + " " + exWeek + " CE";
                System.out.printf("🚀 Bullish momentum! RedBull spotted BUY signal → Targeting: %s%n", callsreach);
                HomeSearch.searchStock(callsreach);
                OrderActions.placeOrder(callsreach, lotSize, "BUY");
                HomeSearch.monitorIndex("CE", stock, callsreach, threshold);
                System.out.println("✅ Trade active! Riding the CE wave — RedBull’s got the exit radar on.");
                System.out.println("🌈 Let the bulls run — monitoring exit like a sniper. 🎯");
                monitorExit("CE");
                break;
            } else if (fisher < trigger) {
                String callsreach = stock + " " + put + " " + exWeek + " PE";
                System.out.printf("⚡ Bearish momentum! RedBull confirmed SELL signal → Targeting: %s%n", callsreach);
                HomeSearch.searchStock(callsreach);
                OrderActions.placeOrder(callsreach, lotSize, "SELL");
                HomeSearch.monitorIndex("PE", stock, callsreach, threshold);
                System.out.println("✅ Trade active! Short play in motion — RedBull is guarding the gate.");
                System.out.println("🌪️ Storm's coming? RedBull’s prepared. Watching for exit signals...");
                monitorExit("PE");
                break;
            } else {
                System.out.println("⚖️ Market in balance — Fisher ≈ Trigger. RedBull says: No clear edge yet. Trust the process...");
                Thread.sleep(5000);
            }
        }
    }


    private static boolean isValidEntry(double adx, double gap) {
        boolean isValid = adx > 20 && gap > threshold;

        if (!isValid) {
            String[] waitingMessages = {
                "🧘‍♂️ Hey boss, breathe easy! ADX needs a bit more juice... Let’s wait for the real move 💪",
                "📡 Hello trader! Market's warming up. No real signal yet, but RedBull’s tracking every beat 🔍",
                "⏳ Ohh! Almost there, but not quite... Patience now saves pain later 🎯",
                "📈 Hey Champ! ADX’s still under 20 — let’s not force it. Big waves take time 🌊",
                "🚦 Easy tiger! Entry's not ripe yet. Remember, no rush, no regret 😎",
                "🎮 Chill mode ON — Strategy is loading... RedBull says timing > action!",
                "🍿 Grab a sip, relax! The setup isn’t cooked yet — but it’s coming! 👀",
                "🤖 Hello Boss! RedBull here. GAP isn’t ready. Waiting like a sniper 🎯",
                "🎉 Cheers to discipline! We only enter when conditions are 🔥 perfect!",
                "🧠 Oof... RedBull’s logic says 'not yet'. But hey — great setups need seasoning 🧂"
            };

            int index = (int) (Math.random() * waitingMessages.length);
            System.out.println(waitingMessages[index]);
            try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        return isValid;
    }

    public static String[] getStrikeInfo(WebDriver driver) {
        WebElement cValueElement = driver.findElement(By.xpath("//div[contains(text(),'C')]/following-sibling::div"));
        String text = cValueElement.getText().trim();
        double currentP = ChartReader_Frame2_Shift.parseValue(text);

        // Round down to nearest 50
        double call = Math.floor(currentP / 50) * 50;

        // Round up to nearest 50
        double put = Math.ceil(currentP / 50) * 50;

        String exWeek = HomeSearch.getThirdFriday();
        return new String[]{String.valueOf((int) call), String.valueOf((int) put), exWeek};
    }

} 

   