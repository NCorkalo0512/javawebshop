package com.example.hralgebrajavawebshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    private final PayPalService payPalService;

    @Autowired
    public WebhookController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @PostMapping("/paypal/webhook")
    public ResponseEntity<String> listenToWebhook(@RequestBody String request, HttpServletRequest httpServletRequest) {
        try {
            if (payPalService.validateWebhookSignature(request, httpServletRequest)) {
                payPalService.processWebhookEvent(request);
                return new ResponseEntity<>("Webhook received and processed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid webhook signature", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing webhook: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
