package pl.glownia.maciej.checkoutcomponent.unit.promotion;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import pl.glownia.maciej.checkoutcomponent.promotion.MultiPricePromotion;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionConfig;
import pl.glownia.maciej.checkoutcomponent.promotion.PromotionEngine;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = PromotionConfig.class)
class PromotionConfigTests {

    @Test
    void testPromotionEngineBeanConfiguration() {
        // Load the configuration class into the Spring context
        ApplicationContext context = new AnnotationConfigApplicationContext(PromotionConfig.class);

        // Assert that the PromotionEngine bean exists in the context
        assertTrue(context.containsBean("promotionEngine"), "PromotionEngine bean should be registered");

        // Retrieve the PromotionEngine bean
        PromotionEngine promotionEngine = context.getBean(PromotionEngine.class);
        assertNotNull(promotionEngine, "PromotionEngine should not be null");

        // Assert that the promotions are correctly configured
        assertEquals(4, promotionEngine.getPromotions().size(), "The PromotionEngine should have 4 promotions");

        // Validate individual promotions
        MultiPricePromotion promoA = promotionEngine.getPromotions().stream()
                .filter(p -> p instanceof MultiPricePromotion) // Check the type
                .map(p -> (MultiPricePromotion) p) // Cast to MultiPricePromotion
                .filter(mp -> mp.getItemId().equals("A")) // Perform filtering
                .findFirst()
                .orElse(null);
        assertNotNull(promoA, "Promotion A should exist");
        assertNotNull(promoA, "Promotion for item A should exist");
        assertTrue(true, "Promotion for item A should be a MultiPricePromotion");
        assertEquals(3, promoA.getRequiredQuantity(), "Promotion A should have a quantity of 3");
        assertEquals(30, promoA.getSpecialPrice(), "Promotion A should have a price of 30");

        MultiPricePromotion promoB = promotionEngine.getPromotions().stream()
                .filter(p -> p instanceof MultiPricePromotion)
                .map(p -> (MultiPricePromotion) p)
                .filter(mp -> mp.getItemId().equals("B"))
                .findFirst()
                .orElse(null);
        assertNotNull(promoB, "Promotion for item B should exist");
        assertTrue(true, "Promotion for item B should be a MultiPricePromotion");
        assertEquals(2, promoB.getRequiredQuantity(), "Promotion B should have a quantity of 2");
        assertEquals(7.5, promoB.getSpecialPrice(), "Promotion B should have a price of 7.5");

        MultiPricePromotion promoC = promotionEngine.getPromotions().stream()
                .filter(p -> p instanceof MultiPricePromotion)
                .map(p -> (MultiPricePromotion) p)
                .filter(mp -> mp.getItemId().equals("C"))
                .findFirst()
                .orElse(null);
        assertNotNull(promoC, "Promotion for item C should exist");
        assertTrue(true, "Promotion for item C should be a MultiPricePromotion");
        assertEquals(4, promoC.getRequiredQuantity(), "Promotion C should have a quantity of 4");
        assertEquals(20, promoC.getSpecialPrice(), "Promotion C should have a price of 20");

        MultiPricePromotion promoD = promotionEngine.getPromotions().stream()
                .filter(p -> p instanceof MultiPricePromotion)
                .map(p -> (MultiPricePromotion) p)
                .filter(mp -> mp.getItemId().equals("D"))
                .findFirst()
                .orElse(null);
        assertNotNull(promoD, "Promotion for item D should exist");
        assertTrue(true, "Promotion for item D should be a MultiPricePromotion");
        assertEquals(2, promoD.getRequiredQuantity(), "Promotion D should have a quantity of 2");
        assertEquals(23.5, promoD.getSpecialPrice(), "Promotion D should have a price of 23.5");
    }

}