package pl.glownia.maciej.checkoutcomponent.unit.checkout;

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

class CheckoutTests {

    private Checkout checkout;
    private PromotionEngine promotionEngine;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();
        promotionEngine = Mockito.mock(PromotionEngine.class);
    }

    @Test
    void testAddItem_NewItem() {
        // Arrange
        Item item = new Item("1", "Test Item", 10.00);

        // Act
        checkout.addItem(item, 2);

        // Assert
        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item, cartItems.get(0).getItem());
        assertEquals(2, cartItems.get(0).getQuantity());
    }

    @Test
    void testAddItem_ExistingItemIncreaseQuantity() {
        // Arrange
        Item item = new Item("1", "Test Item", 10.00);
        checkout.addItem(item, 2);

        // Act
        checkout.addItem(item, 3);

        // Assert
        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item, cartItems.get(0).getItem());
        assertEquals(5, cartItems.get(0).getQuantity());
    }

    @Test
    void testRemoveItem_ItemExists() {
        // Arrange
        Item item1 = new Item("1", "Item 1", 10.00);
        Item item2 = new Item("2", "Item 2", 20.00);
        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        // Act
        checkout.removeItem("1");

        // Assert
        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item2, cartItems.get(0).getItem());
        assertEquals(2, cartItems.get(0).getQuantity());
    }

    @Test
    void testRemoveItem_ItemNotExists() {
        // Arrange
        Item item = new Item("1", "Test Item", 10.00);
        checkout.addItem(item, 2);

        // Act
        checkout.removeItem("2");

        // Assert
        List<CartItem> cartItems = checkout.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(item, cartItems.get(0).getItem());
    }

    @Test
    void testCalculateTotal_NoPromotions() {
        // Arrange
        Item item1 = new Item("1", "Item 1", 10.00);
        Item item2 = new Item("2", "Item 2", 20.00);
        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        when(promotionEngine.applyPromotions(anyList())).thenReturn(0.0); // No discounts

        // Act
        double total = checkout.calculateTotal(promotionEngine);

        // Assert
        assertEquals(50.00, total); // 10*1 + 20*2
        verify(promotionEngine, times(1)).applyPromotions(anyList());
    }

    @Test
    void testCalculateTotal_WithPromotions() {
        // Arrange
        Item item1 = new Item("1", "Item 1", 10.00);
        Item item2 = new Item("2", "Item 2", 20.00);
        checkout.addItem(item1, 1);
        checkout.addItem(item2, 2);

        when(promotionEngine.applyPromotions(anyList())).thenReturn(10.0); // Discount of 10

        // Act
        double total = checkout.calculateTotal(promotionEngine);

        // Assert
        assertEquals(40.00, total); // (10*1 + 20*2) - 10
        verify(promotionEngine, times(1)).applyPromotions(anyList());
    }

    @Test
    void testClearCart() {
        // Arrange
        Item item = new Item("1", "Test Item", 10.00);
        checkout.addItem(item, 2);

        // Act
        checkout.clearCart();

        // Assert
        assertTrue(checkout.getCartItems().isEmpty());
    }

}