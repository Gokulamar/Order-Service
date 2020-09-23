package com.microservice.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.orderservice.common.Payment;
import com.microservice.orderservice.common.TransactionRequest;
import com.microservice.orderservice.common.TransactionResponse;
import com.microservice.orderservice.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderrepository;

	@Autowired
	RestTemplate restTemplate;

	public TransactionResponse saveOrder(TransactionRequest transactionRequest) {

		Payment payment = new Payment();
		payment.setOrderId(transactionRequest.getOrder().getId());
		payment.setAmount(transactionRequest.getOrder().getPrice());

		Payment paymentResponse = restTemplate.postForObject("http://localhost:9191/payment/doPayment", payment, Payment.class);
		orderrepository.save(transactionRequest.getOrder());

		String response = paymentResponse.getPaymentStatus().equalsIgnoreCase("Success") ? "Payment Successfull, Order Placed" : "Failure in Payment" ;

		return new TransactionResponse(transactionRequest.getOrder(),paymentResponse.getTransactionId(),paymentResponse.getAmount(),response);

	}

}
