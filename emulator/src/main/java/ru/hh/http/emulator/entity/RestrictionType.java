package ru.hh.http.emulator.entity;

public enum RestrictionType {

	IN(0),
	
	OR(1),
	
	AND(2);
	
	
	private final int value;

	private RestrictionType(int value) {
		this.value = value;
	}
	
	
}
