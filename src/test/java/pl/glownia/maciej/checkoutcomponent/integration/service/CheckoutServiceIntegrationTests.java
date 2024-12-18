package pl.glownia.maciej.checkoutcomponent.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;
import pl.glownia.maciej.checkoutcomponent.repository.ItemRepository;
import pl.glownia.maciej.checkoutcomponent.service.CheckoutService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceIntegrationTest {

    private ItemRepository mockItemRepository;
    private PromotionEngine mockPromotionEngine;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        mockItemRepository = Mockito.mock(ItemRepository.class);
        mockPromotionEngine = Mockito.mock(PromotionEngine.class);
        checkoutService = new CheckoutService(mockItemRepository, mockPromotionEngine);
    }

    @Test
    void testScan_ValidItem_AddsToCart() {
        // Arrange
        String itemId = "1";
        Item mockItem = new Item(itemId, "A", 2.5);
        when(mockItemRepository.findById(itemId)).thenReturn(mockItem);

        // Act
        checkoutService.scan(itemId);

        // Assert
        List<CartItem> cartItems = checkoutService.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(mockItem, cartItems.get(0).getItem());
        assertEquals(1, cartItems.get(0).getQuantity());
    }

    @Test
    void testScan_InvalidItem_ThrowsException() {
        // Arrange
        String itemId = "100"; // Non-existent item
        when(mockItemRepository.findById(itemId)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> checkoutService.scan(itemId));
        assertTrue(exception.getMessage().contains("Item with ID " + itemId + " not found."));
    }

    @Test
    void testScan_SameItem_IncrementsQuantity() {
        // Arrange
        String itemId = "1";
        Item mockItem = new Item(itemId, "A", 2.5);
        when(mockItemRepository.findById(itemId)).thenReturn(mockItem);

        // Act
        checkoutService.scan(itemId);
        checkoutService.scan(itemId);

        // Assert
        List<CartItem> cartItems = checkoutService.getCartItems();
        assertEquals(1, cartItems.size());
        assertEquals(mockItem, cartItems.get(0).getItem());
        assertEquals(2, cartItems.get(0).getQuantity());
    }

    @Test
    void testCalculateTotal_AppliesPromotions() {
        // Arrange
        String itemId = "1";
        Item mockItem = new Item(itemId, "A", 2.5);
        when(mockItemRepository.findById(itemId)).thenReturn(mockItem);

        CartItem cartItem = new CartItem(mockItem, 2);
        checkoutService.getCartItems().add(cartItem);

        double expectedTotal = 4.5; // Mocked total after promotion
        when(mockPromotionEngine.applyPromotions(checkoutService.getCartItems())).thenReturn(expectedTotal);

        // Act
        double total = checkoutService.calculateTotal();

        // Assert
        assertEquals(expectedTotal, total);
        verify(mockPromotionEngine, times(1)).applyPromotions(checkoutService.getCartItems());
    }

    @Test
    void testPayAndGenerateReceipt_ClearsCartAndGeneratesCorrectReceipt() {
        // Arrange
        String itemId = "1";
        Item mockItem = new Item(itemId, "A", 2.5);
        when(mockItemRepository.findById(itemId)).thenReturn(mockItem);

        CartItem cartItem = new CartItem(mockItem, 2);
        checkoutService.getCartItems().add(cartItem);

        double expectedTotal = 5.0; // Mocked total
        when(mockPromotionEngine.applyPromotions(checkoutService.getCartItems())).thenReturn(expectedTotal);

        // Act
        String receipt = checkoutService.payAndGenerateReceipt();

        // Assert
        assertNotNull(receipt);
        assertTrue(receipt.contains("A x 2: 2.5"));
        assertTrue(receipt.contains("Total: $5.0"));
        assertTrue(checkoutService.getCartItems().isEmpty());
    }

}