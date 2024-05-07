package ru.mikhalev.projects.CurrencyCalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.mikhalev.projects.CurrencyCalculator.repository.CurrencyRepository;
import ru.mikhalev.projects.CurrencyCalculator.scheduler.CentralBankRequestTask;
import ru.mikhalev.projects.CurrencyCalculator.service.CurrencyService;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class CurrencyCalculatorApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CurrencyCalculatorApplication.class, args);
	}
}
