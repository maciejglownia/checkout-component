package pl.glownia.maciej.checkoutcomponent.service;

import org.springframework.stereotype.Service;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.repository.ItemRepository;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;

import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {
    private List<CartItem> cartItems = new ArrayList<>();
    private final ItemRepository itemRepository;
    private final PromotionEngine promotionEngine;

    public CheckoutService(ItemRepository itemRepository, PromotionEngine promotionEngine) {
        this.itemRepository = itemRepository;
        this.promotionEngine = promotionEngine;
    }

    public void scan(String itemId) {
        Item item = itemRepository.findById(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item with ID " + itemId + " not found.");
        }

        // Look for the item in the cart
        CartItem cartItem = cartItems.stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            // Increment the quantity if item exists in the cart
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            // Add new item to the cart
            cartItems.add(new CartItem(item, 1));
        }
    }

    public double calculateTotal() {
        return promotionEngine.applyPromotions(cartItems);
    }

    public String payAndGenerateReceipt() {
        double total = calculateTotal();
        StringBuilder receipt = new StringBuilder("----- Receipt -----\n");
        for (CartItem cartItem : cartItems) {
            receipt.append(cartItem.getItem().getName())
                    .append(" x ")
                    .append(cartItem.getQuantity())
                    .append(": ")
                    .append(cartItem.getItem().getPrice())
                    .append("\n");
        }
        receipt.append("Total: $").append(total);
        cartItems.clear();
        return receipt.toString();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

}