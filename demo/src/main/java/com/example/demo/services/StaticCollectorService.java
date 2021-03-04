package com.example.demo.services;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.controllers.JPAController;
import com.example.demo.domains.RequestMQ;
import com.example.demo.domains.LatestRateTBO;
import com.example.demo.domains.RateWrapper;
import com.example.demo.domains.RequestTBO;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class StaticCollectorService {

	@Autowired
	JPAController jpaController;

	@Autowired
	RabbitMQSenderService rabbitMQSender;
	
	@Value("${hasRabbitMQ}")
	private boolean hasRabbitMQ;

	public StaticCollectorService() {

	}

	public Object persistCurrentRate(RateWrapper data) throws Exception {
		EntityManager em = null;
		EntityTransaction transaction = null;

		try {
			em = jpaController.getEntityManager();
			transaction = em.getTransaction();
			transaction.begin();
			Object oldRec = jpaController.findById(RequestTBO.class, data.getRequestId());
			if (oldRec == null) {
				RequestTBO requestData = getRequestTBOFromWrapper(data);
				jpaController.add(requestData);
				
				//send to rabbit only if we have permission from hasRabbitMQ in application properties
				//we use that permission cuz if we don't have active rabbit server, we will have connection
				//error and the transaction will fail
				if(hasRabbitMQ) {
					RequestMQ requestMQ = convertRDtoMQ(requestData);
					rabbitMQSender.send(requestMQ);
				}
				
				//use named query which take the last value for the selected currency
				String namedQuery = "LatestRateTBO.findByBase";
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("base", data.getCurrency());
				Object oldLR = jpaController.executeSingleResultNamedQuery(LatestRateTBO.class, namedQuery,
						paramMap, em);
				return oldLR;
			} else {
				throw new Exception("There is same request id in database!");
			}

		} catch (Exception ex) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	

	private RequestMQ convertRDtoMQ(RequestTBO requestData) {
		// TODO Auto-generated method stub
		RequestMQ requestMQ = new RequestMQ();
		requestMQ.setRequestId(requestData.getRequestId());
		requestMQ.setClient(requestData.getClient());
		requestMQ.setCurrency(requestData.getCurrency());
		requestMQ.setPeriod(requestData.getPeriod());
		requestMQ.setTimestamp(requestData.getTimestamp());
		return requestMQ;
	}

	public List<Object> persistHistoryRate(RateWrapper data) throws Exception {
		EntityManager em = null;
		EntityTransaction transaction = null;

		try {
			em = jpaController.getEntityManager();
			transaction = em.getTransaction();
			transaction.begin();
			Object oldRec = jpaController.findById(RequestTBO.class, data.getRequestId());
			if (oldRec == null) {
				//we use this cuz in xml file we have go timestamp and we take current timestamp
				if(data.getTimestamp() == null) {
					Date tmp = new Date();
					data.setTimestamp(tmp.getTime()/1000);
				}
				//insert the request data to database
				RequestTBO requestData = getRequestTBOFromWrapper(data);				
				jpaController.add(requestData);
				
				//send to rabbit only if we have permission from hasRabbitMQ in application properties
				//we use that permission cuz if we don't have active rabbit server, we will have connection
				//error and the transaction will fail
				if(hasRabbitMQ) {
					RequestMQ requestMQ = convertRDtoMQ(requestData);
					rabbitMQSender.send(requestMQ);
				}
				
				//get start data from timestamp
				Date endDate = new Date(data.getTimestamp() * 1000);
			    SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String endDateStr = endDateFormat.format(endDate);  

				//get end data from timestamp
				LocalDate startDate = LocalDate.parse(endDateStr); 
				startDate = startDate.minusDays(data.getPeriod());
			    
				//execute period query to take the data in this period
				String namedQuery = "LatestRateTBO.period";
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("endDate", endDateStr);
				paramMap.put("startDate", startDate.toString());
				List<Object> oldLR = jpaController.executeNamedQuery(LatestRateTBO.class, namedQuery,
						paramMap, em);
				return oldLR;
			} else {
				throw new Exception("There is same request id in database!");
			}

		} catch (Exception ex) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw ex;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private RequestTBO getRequestTBOFromWrapper(RateWrapper data) {
		RequestTBO requestData = new RequestTBO();
		requestData.setRequestId(data.getRequestId());
		requestData.setClient(data.getClient());
		requestData.setCurrency(data.getCurrency());
		requestData.setTimestamp(data.getTimestamp());
		requestData.setPeriod(data.getPeriod());
		return requestData;
	}

}
