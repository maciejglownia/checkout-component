package pl.glownia.maciej.checkoutcomponent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.glownia.maciej.checkoutcomponent.service.CheckoutService;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> scanItem(@RequestBody(required = false) String requestBody) {
        // Check if the request body is null or empty
        if (requestBody == null || requestBody.trim().isEmpty()) {
            throw new IllegalArgumentException("Request body cannot be empty");
        }

        try {
            // Check if it's valid JSON and contains an "itemId"
            if (requestBody.startsWith("{")) {
                // Parse JSON object into a Map
                Map<String, String> payload = new ObjectMapper().readValue(requestBody, Map.class);

                // Validate JSON payload
                String itemId = payload.get("itemId");
                if (itemId == null || itemId.trim().isEmpty()) {
                    throw new IllegalArgumentException("Item ID cannot be null or empty");
                }
                return ResponseEntity.ok("Item added to cart.");
            } else {
                // Handle raw string case (e.g., "item123")
                return ResponseEntity.ok("Item added to cart.");
            }
        } catch (JsonProcessingException ex) {
            // Handle malformed JSON
            throw new IllegalArgumentException("Invalid JSON format", ex);
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotal() {
        return ResponseEntity.ok(checkoutService.calculateTotal());
    }

    @PostMapping("/pay")
    public ResponseEntity<String> pay() {
        String receipt = checkoutService.payAndGenerateReceipt();
        return ResponseEntity.ok(receipt);
    }

}