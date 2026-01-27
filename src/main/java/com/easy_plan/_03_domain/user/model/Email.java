package com.easy_plan._03_domain.user.model;

public class Email {
	private final String value;
	
	private Email(String value) {
		this.value = value;
	}
	
	public String getValue() { return value; }
}
