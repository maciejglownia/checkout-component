package pl.glownia.maciej.checkoutcomponent.unit.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.glownia.maciej.checkoutcomponent.dto.Item;
import pl.glownia.maciej.checkoutcomponent.repository.ItemRepository;

import java.util.Map;

public class ItemRepositoryTests {

    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        itemRepository = new ItemRepository();
    }

    @Test
    public void testFindById_WhenItemExists() {
        Item item = itemRepository.findById("A");
        assertNotNull(item);
        assertEquals("A", item.getId());
        assertEquals(40, item.getPrice());
    }

    @Test
    public void testFindById_WhenItemDoesNotExist() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            itemRepository.findById("Z");
        });
        assertEquals("Item with ID: Z not found!", exception.getMessage());
    }

    @Test
    public void testFindAll() {
        Map<String, Item> items = itemRepository.findAll();
        assertEquals(4, items.size()); // A, B, C, D
        assertTrue(items.containsKey("A"));
        assertTrue(items.containsKey("B"));
        assertTrue(items.containsKey("C"));
        assertTrue(items.containsKey("D"));
    }

    @Test
    public void testSaveNewItem() {
        Item newItem = new Item("E", "Item E", 50);
        itemRepository.save(newItem);

        Item fetchedItem = itemRepository.findById("E");
        assertNotNull(fetchedItem);
        assertEquals("Item E", fetchedItem.getName());
        assertEquals(50, fetchedItem.getPrice());
    }

}