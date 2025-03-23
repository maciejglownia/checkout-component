package pl.glownia.maciej.checkoutcomponent.unit.promotion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.glownia.maciej.checkoutcomponent.dto.CartItem;
import pl.glownia.maciej.checkoutcomponent.promotion.Promotion;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PromotionEngineTests {

    private PromotionEngine promotionEngine;

    @Mock
    private Promotion promotion1;

    @Mock
    private Promotion promotion2;

    @Mock
    private CartItem cartItem1;

    @Mock
    private CartItem cartItem2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Promotion> promotions = Arrays.asList(promotion1, promotion2);
        promotionEngine = new PromotionEngine(promotions);
    }

    @Test
    void testApplyPromotionsWithValidPromotions() {
        // Arrange
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);
        when(promotion1.apply(cartItems)).thenReturn(10.0);
        when(promotion2.apply(cartItems)).thenReturn(5.0);

        // Act
        double totalDiscount = promotionEngine.applyPromotions(cartItems);

        // Assert
        assertEquals(15.0, totalDiscount, 0.01);
        verify(promotion1, times(1)).apply(cartItems);
        verify(promotion2, times(1)).apply(cartItems);
    }

    @Test
    void testApplyPromotionsWithNoPromotions() {
        // Arrange
        PromotionEngine emptyPromotionEngine = new PromotionEngine(Collections.emptyList());
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        // Act
        double totalDiscount = emptyPromotionEngine.applyPromotions(cartItems);

        // Assert
        assertEquals(0.0, totalDiscount, 0.01);
    }

    @Test
    void testApplyPromotionsWithNoCartItems() {
        // Arrange
        List<CartItem> emptyCartItems = Collections.emptyList();
        when(promotion1.apply(emptyCartItems)).thenReturn(0.0);
        when(promotion2.apply(emptyCartItems)).thenReturn(0.0);

        // Act
        double totalDiscount = promotionEngine.applyPromotions(emptyCartItems);

        // Assert
        assertEquals(0.0, totalDiscount, 0.01);
        verify(promotion1, times(1)).apply(emptyCartItems);
        verify(promotion2, times(1)).apply(emptyCartItems);
    }

    @Test
    void testGetPromotions() {
        // Act & Assert
        assertEquals(2, promotionEngine.getPromotions().size());
    }

}