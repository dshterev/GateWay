package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domains.RateWrapper;
import com.example.demo.domains.xml.Command;
import com.example.demo.services.StaticCollectorService;

@Controller
@RequestMapping(path = "/xml_api")
public class XmlApiController {

	@Autowired
	StaticCollectorService staticCollectorService;
	
	public XmlApiController() {		
	}
	
	
	@PostMapping(path = "/command", consumes = {MediaType.APPLICATION_XML_VALUE})
	@ResponseBody
	public Object valCurrentRate(@RequestBody Command command) {
		Object res = null;
		try {
			RateWrapper wrapper = parseCommandToRateWrapper(command);
			if(command.getGet() != null) {
				res = staticCollectorService.persistCurrentRate(wrapper);
			}else {
				res = staticCollectorService.persistHistoryRate(wrapper);
			}
		}catch (Exception e) {
			//here we could handle the exception
			e.printStackTrace();
		}
		return res;
	}


	private RateWrapper parseCommandToRateWrapper(Command command) {
		RateWrapper res = new RateWrapper();
		if(command != null) {
			res.setRequestId(command.getId());
			
			if(command.getGet() != null) {
				res.setClient(command.getGet().getConsumer());
				res.setCurrency(command.getGet().getCurrency());
			}
			
			if(command.getHistory() != null) {
				res.setClient(command.getHistory().getConsumer());
				res.setCurrency(command.getHistory().getCurrency());
				res.setPeriod(Integer.parseInt(command.getHistory().getPeriod()));
			}
		}
		return res;
	}
}
