package pl.glownia.maciej.checkoutcomponent.promotion;

import pl.glownia.maciej.checkoutcomponent.dto.CartItem;

import java.util.List;

/**
 * Represents different types of offers: multi-price, combo discounts.
 */
public interface Promotion {
    double apply(List<CartItem> cartItems);
}