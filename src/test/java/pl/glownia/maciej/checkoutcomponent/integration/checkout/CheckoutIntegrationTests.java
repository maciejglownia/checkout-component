package pl.glownia.maciej.checkoutcomponent.integration.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.glownia.maciej.checkoutcomponent.checkout.Checkout;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutIntegrationTests {

    private Checkout checkout;
    private PromotionEngine promotionEngine;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();
        // Mocking the PromotionEngine
        promotionEngine = Mockito.mock(PromotionEngine.class);
    }

    @Test
    void testAddItem() {
        Item item = new Item("1", "A", 1000.00);

        checkout.addItem(item, 2);

        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item, cartItems.get(0).getItem());
        assertEquals(2, cartItems.get(0).getQuantity());
    }

    @Test
    void testAddExistingItem() {
        Item item = new Item("1", "A", 1000.00);

        checkout.addItem(item, 2);
        checkout.addItem(item, 3);

        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(5, cartItems.get(0).getQuantity());
    }

    @Test
    void testRemoveItem() {
        Item item1 = new Item("1", "A", 1000.00);
        Item item2 = new Item("2", "B", 50.00);

        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        checkout.removeItem("2");

        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item1, cartItems.get(0).getItem());
    }

    @Test
    void testCalculateTotalWithoutPromotions() {
        Item item1 = new Item("1", "A", 1000.00);
        Item item2 = new Item("2", "B", 50.00);

        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        // Mock PromotionEngine to return zero discount
        when(promotionEngine.applyPromotions(anyList())).thenReturn(0.0);

        double total = checkout.calculateTotal(promotionEngine);

        assertEquals(1100.00, total); // (1000.00 * 1) + (50.00 * 2)
        verify(promotionEngine, times(1)).applyPromotions(anyList());
    }

    @Test
    void testCalculateTotalWithPromotions() {
        Item item1 = new Item("1", "A", 1000.00);
        Item item2 = new Item("2", "B", 50.00);

        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        // Mock PromotionEngine to return some discount
        when(promotionEngine.applyPromotions(anyList())).thenReturn(100.0);

        double total = checkout.calculateTotal(promotionEngine);

        assertEquals(1000.00, total); // Total after applying a $100 discount
        verify(promotionEngine, times(1)).applyPromotions(anyList());
    }

    @Test
    void testClearCart() {
        Item item1 = new Item("1", "A", 1000.00);
        Item item2 = new Item("2", "B", 50.00);

        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        checkout.clearCart();

        List<CartItem> cartItems = checkout.getCartItems();
        assertTrue(cartItems.isEmpty());
    }

}