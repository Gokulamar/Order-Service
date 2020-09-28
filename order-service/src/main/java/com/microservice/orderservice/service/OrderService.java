package com.microservice.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.orderservice.common.Payment;
import com.microservice.orderservice.common.TransactionRequest;
import com.microservice.orderservice.common.TransactionResponse;
import com.microservice.orderservice.repository.OrderRepository;


@Service
@RefreshScope
public class OrderService {

	@Autowired
	OrderRepository orderrepository;

	@Autowired
	@Lazy
	RestTemplate restTemplate;
	
	@Value("${microservice.payment-service.endpoints.endpoint.uri}")
	private String PAYMENT_URL;

	public TransactionResponse saveOrder(TransactionRequest transactionRequest) {

		Payment payment = new Payment();
		payment.setOrderId(transactionRequest.getOrder().getId());
		payment.setAmount(transactionRequest.getOrder().getPrice());

		Payment paymentResponse = restTemplate.postForObject(PAYMENT_URL, payment, Payment.class);
		orderrepository.save(transactionRequest.getOrder());

		String response = paymentResponse.getPaymentStatus().equalsIgnoreCase("Success") ? "Payment Successfull, Order Placed" : "Failure in Payment" ;

		return new TransactionResponse(transactionRequest.getOrder(),paymentResponse.getTransactionId(),paymentResponse.getAmount(),response);

	}

}
