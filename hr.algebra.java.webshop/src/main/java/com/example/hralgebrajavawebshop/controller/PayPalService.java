package com.example.hralgebrajavawebshop.controller;


import com.example.hralgebrajavawebshop.models.Cart;
import com.example.hralgebrajavawebshop.models.Order;
import com.example.hralgebrajavawebshop.repository.OrderRepositoryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PayPalService {

    private final OrderRepositoryInterface orderRepositoryInterface;
    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;
    private final ObjectMapper objectMapper;

    public PayPalService(OrderRepositoryInterface orderRepositoryInterface, ObjectMapper objectMapper) {
        this.orderRepositoryInterface = orderRepositoryInterface;
        this.objectMapper = objectMapper;
    }

    public String createPayment(Cart cart) throws PayPalRESTException {
        APIContext apiContext=getAPIContext();

        Amount amount= new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(calculateTotalAmount(cart).toString());

        Transaction transaction= new Transaction();
        transaction.setDescription("Order description");
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/carts.html");
        redirectUrls.setReturnUrl("http://localhost:8080/orderConfirmation.html");
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        return createdPayment.getLinks().stream()
                .filter(link -> link.getRel().equalsIgnoreCase("approvalUrl"))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(() -> new RuntimeException("Approval link not available"));
    }

    private BigDecimal calculateTotalAmount(Cart cart) {
        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public APIContext getAPIContext() {
        return new APIContext(clientId, clientSecret, mode);
    }

    public boolean validateWebhookSignature(String requestBody, HttpServletRequest request) {
        APIContext apiContext = getAPIContext();
        try {
            Map<String, String> headers = getHeadersMap(request);
            String webhookId = "OVDJE MORAM STAVITI WEBHOOK ID SA PAYPAL DEVELOPERA";
            String expectedSignature = headers.get("OVDJE TREBA STAVITI HEADER KEY");

            String computedSignature = computeSignature(requestBody, "YOUR_SECRET");


            return expectedSignature.equals(computedSignature);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        return headers;
    }

    public void processWebhookEvent(String requestBody) {
        try {
            Event event = parseEvent(requestBody);

            if (event.getEventType().equals("PAYMENT.SALE.COMPLETED")) {
                handlePaymentSaleCompleted(event);
            } else {
                handleUnknownEventType(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Event parseEvent(String requestBody) throws Exception {
        return objectMapper.readValue(requestBody, Event.class);
    }
    private String computeSignature(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] signedBytes = sha256Hmac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(signedBytes);
    }
    private void handlePaymentSaleCompleted(Event event) {
        String orderId = String.valueOf(event.getResource().getClass());

        Order order = orderRepositoryInterface.findById(Integer.valueOf(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order != null) {

            /*STATUS NARUDZBE*/

            System.out.println("Order with ID " + orderId + " has been completed successfully.");


        }
    }
    private void handleUnknownEventType(Event event) {

        System.out.println("Received unknown event type: " + event.getEventType());

    }
}
