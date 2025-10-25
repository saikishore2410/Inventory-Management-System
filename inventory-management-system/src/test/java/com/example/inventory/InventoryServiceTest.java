import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.inventory.models.Item;
import com.example.inventory.services.InventoryService;

public class InventoryServiceTest {

    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        inventoryService = new InventoryService();
    }

    @Test
    public void testAddItem() {
        Item item = new Item(1, "Test Item", 100, 1);
        inventoryService.addItem(item);
        assertEquals(item, inventoryService.getItemById(1));
    }

    @Test
    public void testUpdateItem() {
        Item item = new Item(1, "Test Item", 100, 1);
        inventoryService.addItem(item);
        item.setQuantity(150);
        inventoryService.updateItem(item);
        assertEquals(150, inventoryService.getItemById(1).getQuantity());
    }

    @Test
    public void testLowStockAlert() {
        Item item = new Item(1, "Test Item", 5, 1);
        inventoryService.addItem(item);
        inventoryService.checkLowStockAlerts();
        assertTrue(inventoryService.isLowStockAlertTriggered(item.getId()));
    }

    @Test
    public void testRemoveItem() {
        Item item = new Item(1, "Test Item", 100, 1);
        inventoryService.addItem(item);
        inventoryService.removeItem(item.getId());
        assertNull(inventoryService.getItemById(item.getId()));
    }
}