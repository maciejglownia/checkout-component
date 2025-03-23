package pl.glownia.maciej.checkoutcomponent.checkout;

import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles cart management and calculates the total price with promotions applied.
 */
public class Checkout {

    private List<CartItem> cartItems;

    public Checkout() {
        this.cartItems = new ArrayList<>();
    }

    /**
     * Add an item to the cart.
     *
     * @param item     The item to add.
     * @param quantity The quantity of the item to add.
     */
    public void addItem(Item item, int quantity) {
        Optional<CartItem> itemInCart = cartItems.stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(item.getId()))
                .findFirst();

        if (itemInCart.isPresent()) {
            // If the item is already in the cart, increase the quantity
            itemInCart.get().setQuantity(itemInCart.get().getQuantity() + quantity);
        } else {
            // If the item is not in the cart, add a new CartItem
            cartItems.add(new CartItem(item, quantity));
        }
    }

    /**
     * Remove an item from the cart.
     *
     * @param itemId The ID of the item to remove.
     */
    public void removeItem(String itemId) {
        cartItems.removeIf(cartItem -> cartItem.getItem().getId().equals(itemId));
    }

    /**
     * Calculate the total price of the cart items, including promotions.
     *
     * @param promotionEngine The promotion engine to apply promotions.
     * @return The total price after applying promotions.
     */
//    public double calculateTotal(PromotionEngine promotionEngine) {
//        // Calculate the normal price
//        double total = cartItems.stream()
//                .mapToDouble(cartItem -> cartItem.getItem().getPrice() * cartItem.getQuantity())
//                .sum();
//
//        // Apply promotions to the cart
//        double discount = promotionEngine.applyPromotions(cartItems);
//
//        // Return the total price after discounts
//        return total - discount;
//    }
    public double calculateTotal(PromotionEngine promotionEngine) {
        // Calculate total cost logic
        double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                .sum();

        double discount = promotionEngine.applyPromotions(cartItems);
        return subtotal - discount;
    }
    /**
     * Clears the cart after checkout.
     */
    public void clearCart() {
        cartItems.clear();
    }

    /**
     * Get the list of items in the cart for receipt generation.
     *
     * @return The list of cart items.
     */
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

}