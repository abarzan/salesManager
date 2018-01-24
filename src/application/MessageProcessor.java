package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Adjusment;
import model.MessageTypes;
import model.Sale;
import model.SalesByType;

public class MessageProcessor {

	static int counter;
	static Map<String, SalesByType> allSales = new HashMap<String, SalesByType>();
	
	static List<String> prodTypes;
	
	static Map<String, List<Adjusment>> adjustmentPerformed = new HashMap<>();
	
	public static void main(String[] args) {
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get("resources\\inputMessages.txt"));
			while (counter<50) {
				counter++;
				String message = reader.readLine();
				readAndProcess (message);
            }
			reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("Pausing.");
		for (String prodType : adjustmentPerformed.keySet()) {
			System.out.println("Adjustments made for sales of "+ prodType);
			for (Adjusment adj : adjustmentPerformed.get(prodType)) {
				System.out.println("\t"+ adj.getOperator()+" value"+adj.getValueAdjusted());
			}
		}
	}
	
	private static void readAndProcess(String message){
		String [] messageComp = message.split(";");
		String messageType = messageComp[0];
		String productType;
		String operator;
		double value;
		int noOfSales;
		
		if (!messageType.equalsIgnoreCase(MessageTypes.MODE_1) && 
				!messageType.equalsIgnoreCase(MessageTypes.MODE_2) && 
				!messageType.equalsIgnoreCase(MessageTypes.MODE_3))  {
			System.out.println("Message type not supported");
		}
		
		else 
		{
			productType = messageComp[1];
			value= Double.valueOf(messageComp[2]);
			SalesByType salesOfCurrentType= allSales.get(productType);
			if(counter%10==0) {
				logEveryTenMessages();
			}
			
			
			if (messageType.equalsIgnoreCase(MessageTypes.MODE_1)) {
			
				if (salesOfCurrentType==null) {
					Sale sale = new Sale(productType, value);
					List<Sale> salesOfType = new ArrayList<Sale>();
					salesOfType.add(sale);
					allSales.put(productType, new SalesByType(productType, salesOfType));
				}
				else {
					salesOfCurrentType.getSales().add(new Sale (productType, value));
				}
			}	
			else if(messageType.equalsIgnoreCase(MessageTypes.MODE_2)){
				noOfSales = Integer.valueOf(messageComp[3]);
				if (salesOfCurrentType==null) {
					List<Sale> salesOfType = new ArrayList<Sale>();
					for (int i=0; i<noOfSales; i++) {
						salesOfType.add(new Sale (productType, value));
					}
					SalesByType salesByType = new SalesByType(productType, salesOfType);
					allSales.put(productType, salesByType);
				}
				else {
					for (int i=0; i<noOfSales; i++) {
						//modify to use addAll
						salesOfCurrentType.getSales().add(new Sale (productType, value));
					}
			}
			
		}
		else if (messageType.equalsIgnoreCase(MessageTypes.MODE_3)){
			
			// test comment 
			operator = messageComp[3];
			if (salesOfCurrentType!=null) {
				adjust(operator, salesOfCurrentType.getSales(), value);
				List<Adjusment>adjustmentsForType  = adjustmentPerformed.get(productType);
				if (adjustmentsForType==null) {
					List<Adjusment> adj = new ArrayList<Adjusment>();
					adj.add(new Adjusment(operator, value));
					adjustmentPerformed.put(productType,adj);
				}
				else {
					adjustmentsForType.add(new Adjusment(operator, value));
				}
			}
		}
	}
	}
	
	public static void adjust(String operator, List<Sale> sales, double adjustVal) {
		switch(operator.toLowerCase()) {
		case ("add"):
			sales.forEach(s -> s.setValue(s.getValue()+adjustVal));
			break;
		case ("substract"):
			sales.forEach(s -> s.setValue(s.getValue()-adjustVal));
			break;
		case ("multiply"):
			sales.forEach(s -> s.setValue(s.getValue()*adjustVal));
			break;
		default:
			break;
		}
		
	}
	
	static void logEveryTenMessages(){
		int noOfSales;
		double sumOfValues ;
		SalesByType salesOfCurrType = null;
		List<Sale> listOfSales= null;
		System.out.println("#####################################");
		for (String productType : allSales.keySet()) {
			salesOfCurrType = allSales.get(productType) ;
			if (salesOfCurrType!=null) {
				listOfSales = salesOfCurrType.getSales();
			}
			if (listOfSales!=null) {
				noOfSales = listOfSales.size();
				sumOfValues = listOfSales.stream().mapToDouble(s -> s.getValue()).sum();
				System.out.println("Number of sales for: "+ productType + " is: "+ noOfSales+ " and has the total value of: "+sumOfValues);
			}
		}
	}

}
