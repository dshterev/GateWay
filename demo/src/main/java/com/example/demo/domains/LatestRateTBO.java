package com.example.demo.domains;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyClass;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "latest_rates")
@NamedQueries(  
	    {  
	    	@NamedQuery(  
	    	        name = "LatestRateTBO.findByBase",  
	    	        query = "from LatestRateTBO l where l.base = :base order by l.id desc"  
	    	        )  ,
	    	@NamedQuery(  
	    	        name = "LatestRateTBO.period",  
	    	        query = "FROM LatestRateTBO l WHERE l.date > :startDate and l.date <= :endDate")  
	    }  
	)  
public class LatestRateTBO {//SELECT * FROM gateway.latest_rates l WHERE l.date > "2021-03-02" and l.date <= "2021-03-03"
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "success")
	private boolean success;

	@Column(name = "timestamp")
	private Long timestamp;

	@Column(name = "base")
	private String base;

	@Column(name = "date")
	private String date;
	
	@ElementCollection(targetClass = Double.class)
	@MapKeyClass(String.class)
	private Map<String, Double> rates;  

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

}