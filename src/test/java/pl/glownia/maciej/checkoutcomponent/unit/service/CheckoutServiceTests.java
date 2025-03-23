package pl.glownia.maciej.checkoutcomponent.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;
import pl.glownia.maciej.checkoutcomponent.repository.ItemRepository;
import pl.glownia.maciej.checkoutcomponent.service.CheckoutService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTests {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PromotionEngine promotionEngine;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testScan_AddNewItemToCart() {
        // Arrange
        String itemId = "item123";
        Item item = new Item(itemId, "Test Item", 10.0);
        when(itemRepository.findById(itemId)).thenReturn(item);

        // Act
        checkoutService.scan(itemId);

        // Assert
        List<CartItem> cartItems = checkoutService.getCartItems(); // Assuming a getter exists for cartItems
        assertEquals(1, cartItems.size());
        assertEquals(item, cartItems.get(0).getItem());
        assertEquals(1, cartItems.get(0).getQuantity());
    }

    @Test
    void testScan_IncrementQuantityIfItemAlreadyInCart() {
        // Arrange
        String itemId = "item123";
        Item item = new Item(itemId, "Test Item", 10.0);
        when(itemRepository.findById(itemId)).thenReturn(item);

        checkoutService.scan(itemId); // Add to cart initially

        // Act
        checkoutService.scan(itemId); // Scan the same item again

        // Assert
        List<CartItem> cartItems = checkoutService.getCartItems(); // Assuming a getter exists
        assertEquals(1, cartItems.size());
        assertEquals(2, cartItems.get(0).getQuantity());
        assertEquals(item, cartItems.get(0).getItem());
    }

    @Test
    void testScan_ThrowsExceptionIfItemNotFound() {
        // Arrange
        String itemId = "nonExistentItem";
        when(itemRepository.findById(itemId)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> checkoutService.scan(itemId));
        assertEquals("Item with ID nonExistentItem not found.", exception.getMessage());
    }

    @Test
    void testCalculateTotal_WithPromotions() {
        // Arrange
        String itemId1 = "item123";
        String itemId2 = "item456";
        Item item1 = new Item(itemId1, "Item 1", 10.0);
        Item item2 = new Item(itemId2, "Item 2", 15.0);
        when(itemRepository.findById(itemId1)).thenReturn(item1);
        when(itemRepository.findById(itemId2)).thenReturn(item2);

        checkoutService.scan(itemId1);
        checkoutService.scan(itemId2);
        when(promotionEngine.applyPromotions(anyList())).thenReturn(20.0); // Mocked promotion discount

        // Act
        double total = checkoutService.calculateTotal();

        // Assert
        assertEquals(20.0, total);
        verify(promotionEngine, times(1)).applyPromotions(anyList());
    }

    @Test
    void testPayAndGenerateReceipt() {
        // Arrange
        String itemId1 = "item123";
        String itemId2 = "item456";
        Item item1 = new Item(itemId1, "Item 1", 10.0);
        Item item2 = new Item(itemId2, "Item 2", 15.0);
        when(itemRepository.findById(itemId1)).thenReturn(item1);
        when(itemRepository.findById(itemId2)).thenReturn(item2);

        checkoutService.scan(itemId1); // Adding two items to cart
        checkoutService.scan(itemId2);
        when(promotionEngine.applyPromotions(anyList())).thenReturn(25.0); // Mocked total after promotion

        // Act
        String receipt = checkoutService.payAndGenerateReceipt();

        // Assert
        assertTrue(receipt.contains("----- Receipt -----"));
        assertTrue(receipt.contains("Item 1 x 1: 10.0"));
        assertTrue(receipt.contains("Item 2 x 1: 15.0"));
        assertTrue(receipt.contains("Total: $25.0"));
        assertTrue(checkoutService.getCartItems().isEmpty()); // Assuming a getter exists
    }

}