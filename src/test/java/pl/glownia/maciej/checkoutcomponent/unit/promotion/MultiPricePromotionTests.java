package pl.glownia.maciej.checkoutcomponent.unit.promotion;

import org.junit.jupiter.api.Test;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.promotion.MultiPricePromotion;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPricePromotionTests {

    @Test
    public void testNoItemsInCart() {
        MultiPricePromotion promo = createPromotion();

        double discount = promo.apply(Collections.emptyList());
        assertEquals(0.0, discount, "Discount should be 0 for an empty cart");
    }

    @Test
    public void testNotEnoughItemsForPromotion() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, 2); // Only 2 items, not enough for the promotion

        double discount = promo.apply(Collections.singletonList(cartItem));
        assertEquals(0.0, discount, "Discount should be 0 when not enough items for promotion");
    }

    @Test
    public void testExactQuantityForOnePromotion() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, 3); // Exactly 3 items for promotion

        double discount = promo.apply(Collections.singletonList(cartItem));
        // Discount = (3 * 40) - 30 = 120 - 30 = 90
        assertEquals(90.0, discount, "Discount should match for an exact set of promotion items");
    }

    @Test
    public void testMultiplePromotionsApplied() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, 7); // 7 items

        double discount = promo.apply(Collections.singletonList(cartItem));
        // 2 sets of promotions = 2 * [(3 * 40) - 30] = 2 * 90 = 180
        assertEquals(180.0, discount, "Discount should account for multiple promotional sets");
    }

    @Test
    public void testMixedPromotionsAndNormalPrice() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, 5); // 5 items — 3 for promotion, 2 at normal price

        double discount = promo.apply(Collections.singletonList(cartItem));
        // 1 set of promotion = 3 items -> (3 * 40) - 30 = 120 - 30 = 90
        // Remaining 2 normal priced items = no additional discount
        assertEquals(90.0, discount, "Discount should apply only to promotional sets, not extra items");
    }

    @Test
    public void testNonMatchingItemId() {
        MultiPricePromotion promo = createPromotion();

        Item itemB = new Item("B", "Item B", 50);
        CartItem cartItem = new CartItem(itemB, 4); // Different item ID

        double discount = promo.apply(Collections.singletonList(cartItem));
        assertEquals(0.0, discount, "Discount should be 0 for non-matching item IDs");
    }

    @Test
    public void testNullCart() {
        MultiPricePromotion promo = createPromotion();

        double discount = promo.apply(null); // Cart is null
        assertEquals(0.0, discount, "Discount should be 0 when the cart is null");
    }

    @Test
    public void testNullCartItem() {
        MultiPricePromotion promo = createPromotion();

        double discount = promo.apply(Collections.singletonList(null)); // Null CartItem
        assertEquals(0.0, discount, "Discount should be 0 for a null cart item");
    }

    @Test
    public void testNegativeQuantity() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, -1); // Negative quantity

        double discount = promo.apply(Collections.singletonList(cartItem));
        assertEquals(0.0, discount, "Discount should be 0 for negative item quantities in the cart");
    }

    @Test
    public void testZeroQuantity() {
        MultiPricePromotion promo = createPromotion();

        Item itemA = new Item("A", "Item A", 40);
        CartItem cartItem = new CartItem(itemA, 0); // Zero quantity

        double discount = promo.apply(Collections.singletonList(cartItem));
        assertEquals(0.0, discount, "Discount should be 0 for zero quantity of items");
    }

    // Helper method to create a promotion with specific values
    private MultiPricePromotion createPromotion() {
        MultiPricePromotion promo = new MultiPricePromotion();
        promo.setItemId("A");
        promo.setRequiredQuantity(3);
        promo.setSpecialPrice(30);
        return promo;
    }

}