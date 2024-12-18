package pl.glownia.maciej.checkoutcomponent.integration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.repository.ItemRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(itemRepository.findById("item123"))
                .thenReturn(new Item("item123", "Test Item", 9.99));
    }

    @Test
    void shouldAddItemToCart_whenValidPostRequestSent() throws Exception {
        // Simulate a valid POST request with raw string and expect 200 OK response
        mockMvc.perform(post("/checkout/scan")
                        .content("\"item123\"") // Raw string input
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect a 200 OK
                .andExpect(content().string("Item added to cart."));
    }

    @Test
    void shouldReturnMethodNotAllowed_whenGetRequestSent() throws Exception {
        // Simulate a GET request and expect 405 Method Not Allowed
        mockMvc.perform(get("/checkout/scan"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnBadRequest_whenEmptyPostRequestSent() throws Exception {
        // Simulate a POST request with NO body and expect 400 Bad Request
        mockMvc.perform(post("/checkout/scan")
                        .content("") // Empty request body
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Request body cannot be empty"));
    }

    @Test
    void shouldReturnBadRequest_whenMalformedJsonIsSent() throws Exception {
        mockMvc.perform(post("/checkout/scan")
                        .content("{itemId:123}") // Malformed JSON
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expecting a 400 Bad Request
                .andExpect(content().string(containsString("Error: Invalid JSON format"))); // General error message
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public ItemRepository itemRepository() {
            return Mockito.mock(ItemRepository.class); // Create a mock repository
        }
    }

}