package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.example.demo.controllers.JPAController;
import com.example.demo.domains.LatestRateTBO;

public class LatestRate implements Runnable{
	private JPAController jpaController;
	private String fixerUrl = "http://data.fixer.io/api/latest?access_key=";
	
	public LatestRate(JPAController jpaController, String authToken) {
		this.jpaController = jpaController;
		fixerUrl += authToken;
	}
	
	@Override
	public void run() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<LatestRateTBO> latestR =  restTemplate.getForEntity(fixerUrl, LatestRateTBO.class);
		if(latestR != null) {
			try {
				jpaController.add(latestR.getBody());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

}
