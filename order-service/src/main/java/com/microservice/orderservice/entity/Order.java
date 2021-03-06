package com.microservice.orderservice.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORDERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	
	@Id
	private int id;
	private String name;
	private int quantity;
	private double price;

}
