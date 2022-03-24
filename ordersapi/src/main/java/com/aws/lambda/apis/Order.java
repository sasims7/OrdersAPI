package com.aws.lambda.apis;

public class Order {
	public int id;
	public String itemName;
	public int quantity;
	
	public Order() {
		
	}
	
	public Order(int id, String itemName, int quantity) {
		this.id = id;
		this.itemName = itemName;
		this.quantity = quantity;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
