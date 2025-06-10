package com.redbull.redbull;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TradeLogger {

    public static void log(String  adx) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String row = timestamp + ",ADX," + adx;

        try (PrintWriter writer = new PrintWriter(new FileWriter("logfile.csv", true))) {
            writer.println(row);
        } catch (IOException e) {
            System.err.println("❌ Failed to write ADX log: " + e.getMessage());
        }
    }

}
