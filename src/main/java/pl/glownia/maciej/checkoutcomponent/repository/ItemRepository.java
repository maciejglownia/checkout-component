package pl.glownia.maciej.checkoutcomponent.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import pl.glownia.maciej.checkoutcomponent.dto.Item;

/**
 * Stores items and fetch them based on IDs.
 * NOTE: In case scalability is required, consider using Spring Data JPA or
 * similar tools to persist the catalog in a database.
 */
@Component
public class ItemRepository {

    // Items will be initialized using a Map or a small database table - here is a Map
    private final Map<String, Item> catalog;

    public ItemRepository() {
        this.catalog = new HashMap<>();
        initializeCatalog();
    }

    /**
     * Initialize the item catalog with predefined items.
     */
    private void initializeCatalog() {
        catalog.put("A", new Item("A", "Item A", 40));
        catalog.put("B", new Item("B", "Item B", 10));
        catalog.put("C", new Item("C", "Item C", 30));
        catalog.put("D", new Item("D", "Item D", 25));
    }

    /**
     * Finds an item by its ID.
     *
     * @param id The ID of the item to find.
     * @return The item if found, or throws an exception if not found.
     */
    public Item findById(String id) {
        return Optional.ofNullable(catalog.get(id))
                .orElseThrow(() -> new IllegalArgumentException("Item with ID: " + id + " not found!"));
    }

    /**
     * Fetches all available items in the catalog.
     *
     * @return A map of item IDs and their corresponding items.
     */
    public Map<String, Item> findAll() {
        return new HashMap<>(catalog);
    }

    /**
     * Adds a new item to the catalog.
     *
     * @param item The item to add.
     */
    public void save(Item item) {
        catalog.put(item.getId(), item);
    }

}