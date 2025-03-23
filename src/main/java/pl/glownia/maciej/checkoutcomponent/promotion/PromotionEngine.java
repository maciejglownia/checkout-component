package pl.glownia.maciej.checkoutcomponent.promotion;

import org.springframework.stereotype.Component;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;

import java.util.List;

@Component
public class PromotionEngine {

    private final List<Promotion> promotions;

    public PromotionEngine(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    // Getter for promotions
    public List<Promotion> getPromotions() {
        return promotions;
    }

    /**
     * Apply all promotions to the cart items and calculate the total discount
     *
     * @param cartItems The list of cart items
     * @return The total discount applied
     */
    public double applyPromotions(List<CartItem> cartItems) {
        return promotions.stream()
                .mapToDouble(promotion -> promotion.apply(cartItems))
                .sum();
    }

}