package com.paypal.walletms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WalletmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletmsApplication.class, args);
	}

}
