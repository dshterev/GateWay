package com.example.demo.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.domains.RequestMQ;

@Service
public class RabbitMQSenderService {
	
	@Autowired
	private AmqpTemplate rabbitTemplate;
	
	@Value("${javainuse.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${javainuse.rabbitmq.routingkey}")
	private String routingkey;	
	
	public void send(RequestMQ company) {
		rabbitTemplate.convertAndSend(exchange, routingkey, company.toString());
		System.out.println("Send msg = " + company.toString());
	    
	}
}