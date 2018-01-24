package model;

import java.util.List;

public class Adjusment {
	
	String operator;
	double valueAdjusted;
//	String prodType;
//	
	
	public Adjusment(String operator, double value) {
		this.operator = operator;
		this.valueAdjusted = value;
	}

	

	public String getOperator() {
		return operator;
	}

	
	public double getValueAdjusted() {
		return valueAdjusted;
	}

	
	

}
