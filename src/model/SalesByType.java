package model;

import java.util.List;

public class SalesByType {
	
	String prodType;
	List<Sale> sales;
	
	public SalesByType(String prodType, List<Sale> sales) {
		this.prodType = prodType;
		this.sales = sales;
	}
	
	public List<Sale> getSales() {
		return sales;
	}
	
}
