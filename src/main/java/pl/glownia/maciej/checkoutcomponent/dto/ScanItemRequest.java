package pl.glownia.maciej.checkoutcomponent.dto;

import jakarta.validation.constraints.NotEmpty;

public class ScanItemRequest {

    @NotEmpty
    private final String itemId;

    public ScanItemRequest(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

}