package com.example.demo.domains;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = RequestMQ.class)
public class RequestMQ {

	private String requestId;
	private Long timestamp;
	private String client;
	private String currency;
	private int period;

	

	public String getRequestId() {
		return requestId;
	}



	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}



	public Long getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}



	public String getClient() {
		return client;
	}



	public void setClient(String client) {
		this.client = client;
	}



	public String getCurrency() {
		return currency;
	}



	public void setCurrency(String currency) {
		this.currency = currency;
	}



	public int getPeriod() {
		return period;
	}



	public void setPeriod(int period) {
		this.period = period;
	}



	@Override
	public String toString() {
		return "Request [requestId=" + requestId + ", timestamp=" + timestamp 
				+ ", client=" + client + ", currency=" + currency
				+ ", period=" + period + "]";
	}

}