package edu.project.store.controller;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import edu.project.store.services.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private final OrderService orderService;

    public StripeWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        String endpointSecret = "whsec_7cba006a65b8ebbd275098d12bc78b3fe1330fa54163b36288d363f823022551"; 
        Event event;

        System.out.println("==== STRIPE WEBHOOK FIRED ====");
        System.out.println("Stripe-Signature: " + sigHeader);
        System.out.println("Payload: " + payload);

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Parsed event: " + event.getType());
        } catch (Exception e) {
            System.out.println("Signature verification failed: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            System.out.println("===> Received checkout.session.completed!");
            try {
                // Extract the session id from the event
               

            } catch (Exception ex) {
                System.out.println("Error extracting session ID: " + ex.getMessage());
            }
        } else {
            System.out.println("===> Event received but not handled: " + event.getType());
        }

        // Handle other event types as needed...

        return ResponseEntity.ok("Received");
    }
}
