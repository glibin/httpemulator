package ru.hh.http.emulator.entity;

import java.util.Set;

public class HttpEntryRestriction implements HttpRestriction {

	private Long id;
	
	private RestrictionType restrictionType;
	
	private Set<HttpEntry> httpEntries;
	
	public HttpEntryRestriction(RestrictionType restrictionType) {
		this.restrictionType = restrictionType;
	}

	public RestrictionType getRestrictionType() {
		return restrictionType;
	}

	public void setRestrictionType(RestrictionType restrictionType) {
		this.restrictionType = restrictionType;
	}

	
}
