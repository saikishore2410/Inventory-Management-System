package com.example.inventory.services;

import com.example.inventory.dao.ItemDao;
import com.example.inventory.dao.VendorDao;
import com.example.inventory.models.Item;
import com.example.inventory.models.StockEntry;
import com.example.inventory.models.Vendor;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {
    private final ItemDao itemDao;
    private final VendorDao vendorDao;

    public InventoryService(ItemDao itemDao, VendorDao vendorDao) {
        this.itemDao = itemDao;
        this.vendorDao = vendorDao;
    }

    public void addStockEntry(StockEntry stockEntry) {
        Item item = itemDao.getItemById(stockEntry.getItemId());
        if (item != null) {
            item.setQuantity(item.getQuantity() + stockEntry.getQuantity());
            itemDao.updateItem(item);
        }
    }

    public List<Item> getLowStockItems(int threshold) {
        return itemDao.getAllItems().stream()
                .filter(item -> item.getQuantity() < threshold)
                .collect(Collectors.toList());
    }

    public void addVendor(Vendor vendor) {
        vendorDao.addVendor(vendor);
    }

    public List<Vendor> getAllVendors() {
        return vendorDao.getAllVendors();
    }

    public void updateVendor(Vendor vendor) {
        vendorDao.updateVendor(vendor);
    }
}