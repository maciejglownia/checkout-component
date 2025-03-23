package pl.glownia.maciej.checkoutcomponent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.glownia.maciej.checkoutcomponent.dto.ScanItemRequest;
import pl.glownia.maciej.checkoutcomponent.dto.ScanItemResponse;
import pl.glownia.maciej.checkoutcomponent.service.CheckoutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/scan")
    public ScanItemResponse scanItem(ScanItemRequest scanItemRequest) {
        checkoutService.scan(scanItemRequest.getItemId());
        return ScanItemResponse.success();
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