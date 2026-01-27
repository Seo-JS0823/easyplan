package com.easy_plan._03_domain;

public interface EasyPlanErrorCode {
	int getStatus();
	String getMessage();
	String getCode();
	String name();
}
