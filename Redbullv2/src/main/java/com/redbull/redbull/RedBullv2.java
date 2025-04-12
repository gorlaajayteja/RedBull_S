package com.redbull.redbull;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedBullv2 {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(RedBullv2 .class, args);
		System.out.println(InstructionMassages.greeting);
		System.out.println(InstructionMassages.Redbull_Activated);
		Angel_login_Process.login();
		Map<String, Double> indicatorValues = ChartReader_Frame2_Shift.frameToframe();
		System.out.println(indicatorValues);
	}

}
