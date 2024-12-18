package pl.glownia.maciej.checkoutcomponent.promotion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class PromotionConfig {

    @Bean
    public PromotionEngine promotionEngine() {
        // Create multi-price promotions for each applicable item
        Promotion promoA = new MultiPricePromotion("A", 3, 30);     // 3 items for 30
        Promotion promoB = new MultiPricePromotion("B", 2, 7.5);    // 2 items for 7.5
        Promotion promoC = new MultiPricePromotion("C", 4, 20);     // 4 items for 20
        Promotion promoD = new MultiPricePromotion("D", 2, 23.5);   // 2 items for 23.5

        return new PromotionEngine(Arrays.asList(promoA, promoB, promoC, promoD));
    }

}