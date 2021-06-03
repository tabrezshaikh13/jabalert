package dev.tab.covalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovalertApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovalertApplication.class, args);
	}

}
