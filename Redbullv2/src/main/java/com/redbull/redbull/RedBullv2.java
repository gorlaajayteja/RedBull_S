package com.redbull.redbull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedBullv2 {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RedBullv2 .class, args);
		System.out.println(InstructionMassages.greeting);
		System.out.println(InstructionMassages.Redbull_Activated);
		Angel_login_Process.login();
		
        StrategyRunner.runADX_FABStrategy();
	}

}
