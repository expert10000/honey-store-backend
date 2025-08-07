package edu.project.store.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class StripeController {

    public StripeController(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession() throws Exception {
        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(1500L) // $15.00 (amount in cents)
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Test Product")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sessionId", session.getId());
        return responseData;
    }
}
