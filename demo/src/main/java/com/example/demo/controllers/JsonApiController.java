package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domains.RequestMQ;
import com.example.demo.domains.RateWrapper;
import com.example.demo.services.RabbitMQSenderService;
import com.example.demo.services.StaticCollectorService;

@Controller
@RequestMapping(path = "/json_api")
public class JsonApiController {	
	
	@Autowired
	StaticCollectorService staticCollectorService;
	
	public JsonApiController() {
		
	}
	
	
	@PostMapping("/current")
	public Object valCurrentRate(@RequestBody RateWrapper wrapper) {
		Object res = null;
		try {
			res = staticCollectorService.persistCurrentRate(wrapper);
		}catch (Exception e) {
			//here we could handle the exception
			e.printStackTrace();
		}
		return res;
	}
	
	@PostMapping("/history")
	public List<Object> valHistoryRate(@RequestBody RateWrapper wrapper) {
		List<Object> res = null;
		try {
			res = staticCollectorService.persistHistoryRate(wrapper);
		
		}catch (Exception e) {
			//here we could handle the exception
			e.printStackTrace();
		}
		return res;
	}
}
