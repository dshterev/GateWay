package com.example.demo.domains.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Command {

	@XmlAttribute
    String id;
	
	@XmlElement
	Get get;
	
	@XmlElement
	History history;
    
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
    
	public Get getGet() {
		return get;
	}
	
	public void setGet(Get get) {
		this.get = get;
	}

	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}
	
}

