package com.redbull.redbull;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StockListCollectFromUser {
    public static Set<String> stocklist() {
        Set<String> stock = new HashSet<>();
        try (Scanner scanner = new Scanner(System.in)) {
			try {
			    System.out.println("Captain: Please provide stock list for trading:\n"+"Enter stock names and type 'end' to finish:");
			    
			    while (true) {
			        String stc = scanner.nextLine();
			        if ("end".equalsIgnoreCase(stc)) {
			            break;
			        }
			        stock.add(stc);
			    }
			} finally {
//            scanner.close(); // Ensure the scanner is closed even if an error occurs
			}
		}
        
        return stock;
    }
}
