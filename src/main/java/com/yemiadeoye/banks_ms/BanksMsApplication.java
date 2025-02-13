package com.yemiadeoye.banks_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BanksMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanksMsApplication.class, args);
	}

}
