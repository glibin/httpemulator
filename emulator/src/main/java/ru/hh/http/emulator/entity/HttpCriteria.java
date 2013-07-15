package ru.hh.http.emulator.entity;

import java.util.Collection;

public class HttpCriteria {

	private Collection<HttpRestriction> restrictions;
	
	private Collection<HttpEntry> response;

	public Collection<HttpEntry> getResponse() {
		return response;
	}

	public void setResponse(Collection<HttpEntry> response) {
		this.response = response;
	}
	
	
}
