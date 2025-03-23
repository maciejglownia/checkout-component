package pl.glownia.maciej.checkoutcomponent.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.glownia.maciej.checkoutcomponent.controller.CheckoutController;
import pl.glownia.maciej.checkoutcomponent.service.CheckoutService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        // Mock service behavior
        Mockito.when(checkoutService.calculateTotal())
                .thenReturn(20.5);
        Mockito.when(checkoutService.payAndGenerateReceipt())
                .thenReturn("Receipt: Thank you for your purchase!");
    }

    @Test
    void testScanItem() throws Exception {
        // Mock the scan method
        Mockito.doNothing().when(checkoutService).scan(anyString());

        mockMvc.perform(post("/checkout/scan")
                        .content("item-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Item added to cart."));
    }

    @Test
    void testGetTotal() throws Exception {
        mockMvc.perform(get("/checkout/total"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("20.5"));
    }

    @Test
    void testPay() throws Exception {
        mockMvc.perform(post("/checkout/pay"))
                .andExpect(status().isOk())
                .andExpect(content().string("Receipt: Thank you for your purchase!"));
    }

    // Test configuration to inject the mock service
    @TestConfiguration
    static class TestConfig {
        @Bean
        public CheckoutService checkoutService() {
            return Mockito.mock(CheckoutService.class);
        }
    }

}