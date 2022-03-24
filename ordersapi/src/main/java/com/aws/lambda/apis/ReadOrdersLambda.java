package com.aws.lambda.apis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadOrdersLambda {
	
	public APIGatewayProxyResponseEvent getOrders(APIGatewayProxyRequestEvent request) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Order> orders1 = new ArrayList<Order>();
		orders1 = retrieveOrderDetails();
		
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
		ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv("ORDERS_TABLE")));
		List<Order> orders = scanResult.getItems().stream().map(item-> new Order(Integer.parseInt(item.get("id").getN()),
				item.get("itemName").getS(), Integer.parseInt(item.get("quantity").getN()))).collect(Collectors.toList());
		
		String jsonOutput = objectMapper.writeValueAsString(orders);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(jsonOutput);
	}
	
	public List<Order> retrieveOrderDetails() {
		List<Order> orders = new ArrayList<Order>();
		String Sql = "SELECT * FROM ORDERS_TABLE";
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			con = DatabaseConnection.getDatabaseConnection();
			preparedStatement = con.prepareStatement(Sql);
			resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	        	Order order = new Order();
	        	order.setId(resultSet.getInt("id"));
	        	order.setItemName(resultSet.getString("itemName"));
	        	order.setQuantity(resultSet.getInt("quantity"));
	        	orders.add(order);
	        }	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orders;
	}

}
