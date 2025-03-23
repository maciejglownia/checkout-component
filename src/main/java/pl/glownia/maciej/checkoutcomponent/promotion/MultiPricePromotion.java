package pl.glownia.maciej.checkoutcomponent.promotion;

import org.springframework.stereotype.Component;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;

import java.util.List;

@Component
public class MultiPricePromotion implements Promotion {

    private String itemId;
    private int requiredQuantity;
    private double specialPrice;

    // Default no-args constructor
    public MultiPricePromotion() {
    }

    // Constructor for manual configuration
    public MultiPricePromotion(String itemId, int requiredQuantity, double specialPrice) {
        this.itemId = itemId;
        this.requiredQuantity = requiredQuantity;
        this.specialPrice = specialPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
    }

    @Override
    public double apply(List<CartItem> cartItems) {
        double discount = 0.0;

        if (cartItems == null || cartItems.isEmpty()) {
            return discount; // No items, so no discount
        }

        // Process each item in the cart
        for (CartItem cartItem : cartItems) {

            // Ensuring cart item and ID are valid
            if (cartItem == null || cartItem.getItem() == null || cartItem.getItem().getId() == null) {
                continue; // Skip invalid items
            }

            // Check if the item matches the promotion
            if (cartItem.getItem().getId().equals(itemId)) {
                int quantity = cartItem.getQuantity();
                double normalPrice = cartItem.getItem().getPrice();

                // Ensure valid price and quantity
                if (quantity <= 0 || normalPrice <= 0) {
                    continue; // Skip invalid input
                }

                // Calculate promotional sets
                int promotionalSets = quantity / requiredQuantity;

                // Calculate total discount ONLY for promotional sets
                discount += promotionalSets * ((requiredQuantity * normalPrice) - specialPrice);

                // Remaining items do not qualify for discount
                // (No special promotion for non-promotional items)
            }
        }

        return discount;
    }

}