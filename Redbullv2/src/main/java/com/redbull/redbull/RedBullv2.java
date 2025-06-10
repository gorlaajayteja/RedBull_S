package com.redbull.redbull;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedBullv2 {
    private static final Scanner scanner = new Scanner(System.in); // Single shared scanner

    public static String userInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int userIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }


    public static void closeScanner() {
        scanner.close();
    }

    public static void main(String[] args) {
        SpringApplication.run(RedBullv2.class, args);
//        System.out.println(InstructionMassages.greeting);
//        System.out.println(InstructionMassages.Redbull_Activated);

//        int chooseStrgy = userIntInput("Choose your Strategy:\n1. ADX_Fisher\n2. DMI, MACD, Fisher\nYour Choice: ");
//        int chooseTrade;

        // Validating trading type
//        while (true) {
//            chooseTrade = userIntInput("Choose Trading Mode:\n1. Real Trade\n2. Paper Trade\n3. Personal or Testing\nYour Choice: ");
//            if (chooseTrade >= 1 && chooseTrade <= 3) {
//                break;
//            } else {
//                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
//            }
//        }
//        System.out.println("Ready to take lot size input...");
//        int lotSize = Integer.parseInt(userInput("Please enter lot size: "));
        int lotSize = 1;
        System.out.println("User entered lot size: " + lotSize);

        try {
            switch (1) {
                case 1:
                    Angel_login_Process.Papertrade_login();
//                    Xpath_Validator.validateAllIndicators();
                   
                    StrategyRunner.runADX_FABStrategy(lotSize);
                    break;

                case 2:
//                    Angel_login_Process.Papertrade_login();
////                    if (chooseStrgy == 1) {
//                        StrategyRunner.runADX_FABStrategy();
//                    } else if (chooseStrgy == 2) {
//                        StrategyRunner.DMI_MACD_FI();
//                    } else {
//                        System.out.println("Invalid strategy choice.");
//                    }
//                    break;

                case 3:
                	Xpath_Validator.validateAllIndicators();
                	System.out.println("Testing");


                default:
                    System.out.println("Unexpected error in trade selection.");
            }
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } 
//        finally {
//            closeScanner(); // Safely close scanner once done
//        }
    }
}
