package model;

public class Sale {

	String prodType;
	double value;
	public Sale(String productType, double value) {
		this.prodType = productType;
		this.value = value;
		// TODO Auto-generated constructor stub
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
