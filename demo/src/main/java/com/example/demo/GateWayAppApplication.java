package com.example.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

import com.example.demo.controllers.JPAController;

@SpringBootApplication
public class GateWayAppApplication implements CommandLineRunner {
	
	@Value("${fixerAuthToken}")
	private String authToken;
	
	@Value("${fixerSecondsDelay}")
	private int secondsDelay;
	
	@Autowired
	JPAController jpaController;
	
	public static void main(String[] args) {
		SpringApplication.run(GateWayAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		int coreCount = Runtime.getRuntime().availableProcessors();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(coreCount);
		service.scheduleWithFixedDelay(new LatestRate(jpaController, authToken), 1, secondsDelay, TimeUnit.SECONDS);
	}
}
