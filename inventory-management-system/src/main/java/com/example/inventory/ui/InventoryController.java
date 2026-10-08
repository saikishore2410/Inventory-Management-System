package com.example.inventory.ui;

import com.example.inventory.models.Item;
import com.example.inventory.models.Vendor;
import com.example.inventory.services.InventoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.List;

public class InventoryController {
    private final InventoryService service;
    private final TableView<Item> items = new TableView<>();
    private final TableView<Vendor> vendors = new TableView<>();
    private final TextField itemSearch = new TextField();
    private final TextField vendorSearch = new TextField();
    private final Label itemCount = new Label("0");
    private final Label vendorCount = new Label("0");
    private final Label lowStockCount = new Label("0");
    private final Label totalUnits = new Label("0");
    private final Label status = new Label("Ready");

    public InventoryController(InventoryService service) {
        this.service = service;
        configureTables();
    }

    public Node createView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        VBox header = new VBox(4);
        header.getStyleClass().add("header");
        Label title = new Label("Inventory Management System");
        title.getStyleClass().add("header-title");
        Label subtitle = new Label("Track products, stock movement and vendors");
        subtitle.getStyleClass().add("header-subtitle");
        header.getChildren().addAll(title, subtitle);
        root.setTop(header);

        VBox content = new VBox(14);
        content.setPadding(new Insets(18, 24, 18, 24));
        content.getChildren().addAll(summaryCards(), createTabs(), status);
        VBox.setVgrow(content.getChildren().get(1), Priority.ALWAYS);
        root.setCenter(content);

        refresh();
        return root;
    }

    private Node summaryCards() {
        HBox cards = new HBox(12);
        cards.getChildren().addAll(
            card("ITEMS", itemCount),
            card("TOTAL UNITS", totalUnits),
            card("LOW STOCK (≤5)", lowStockCount),
            card("VENDORS", vendorCount)
        );
        return cards;
    }

    private VBox card(String title, Label value) {
        Label t = new Label(title);
        t.getStyleClass().add("card-title");
        value.getStyleClass().add("card-value");
        VBox box = new VBox(6, t, value);
        box.getStyleClass().add("card");
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private TabPane createTabs() {
        TabPane tabs = new TabPane();
        Tab itemTab = new Tab("Inventory", itemsView());
        Tab vendorTab = new Tab("Vendors", vendorsView());
        itemTab.setClosable(false);
        vendorTab.setClosable(false);
        tabs.getTabs().addAll(itemTab, vendorTab);
        return tabs;
    }

    private Node itemsView() {
        Button add = button("＋ Add Item");
        Button edit = button("Edit");
        Button delete = dangerButton("Delete");
        Button stockIn = button("＋ Stock In");
        Button stockOut = button("− Stock Out");
        Button refresh = secondaryButton("Refresh");

        add.setOnAction(e -> addItem());
        edit.setOnAction(e -> editItem());
        delete.setOnAction(e -> deleteItem());
        stockIn.setOnAction(e -> stock("IN"));
        stockOut.setOnAction(e -> stock("OUT"));
        refresh.setOnAction(e -> refresh());

        itemSearch.setPromptText("Search items by name or ID...");
        itemSearch.setPrefWidth(260);
        HBox toolbar = new HBox(8, itemSearch, add, edit, delete, stockIn, stockOut, refresh);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");

        VBox box = new VBox(toolbar, items);
        VBox.setVgrow(items, Priority.ALWAYS);
        return box;
    }

    private Node vendorsView() {
        Button add = button("＋ Add Vendor");
        Button edit = button("Edit");
        Button delete = dangerButton("Delete");
        Button refresh = secondaryButton("Refresh");

        add.setOnAction(e -> addVendor());
        edit.setOnAction(e -> editVendor());
        delete.setOnAction(e -> deleteVendor());
        refresh.setOnAction(e -> refresh());

        vendorSearch.setPromptText("Search vendors by name or contact...");
        vendorSearch.setPrefWidth(300);
        HBox toolbar = new HBox(8, vendorSearch, add, edit, delete, refresh);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.getStyleClass().add("toolbar");

        VBox box = new VBox(toolbar, vendors);
        VBox.setVgrow(vendors, Priority.ALWAYS);
        return box;
    }

    private Button button(String text) {
        return new Button(text);
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("secondary-button");
        return b;
    }

    private Button dangerButton(String text) {
        Button b = new Button(text);
        b.getStyleClass().add("danger-button");
        return b;
    }

    private void configureTables() {
        TableColumn<Item, String> id = itemColumn("ID", x -> String.valueOf(x.getId()));
        TableColumn<Item, String> name = itemColumn("Item Name", Item::getName);
        TableColumn<Item, String> qty = itemColumn("Quantity", x -> String.valueOf(x.getQuantity()));
        TableColumn<Item, String> vendor = itemColumn("Vendor ID", x -> x.getVendorId() > 0 ? String.valueOf(x.getVendorId()) : "—");

        items.getColumns().setAll(id, name, qty, vendor);
        items.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        items.setPlaceholder(new Label("No inventory items found"));
        items.setRowFactory(tv -> new TableRow<>() {{
            itemProperty().addListener((obs, old, value) -> {
                if (value != null && value.getQuantity() <= 5) getStyleClass().add("low-stock");
                else getStyleClass().remove("low-stock");
            });
        }});

        TableColumn<Vendor, String> vid = vendorColumn("ID", x -> String.valueOf(x.getId()));
        TableColumn<Vendor, String> vname = vendorColumn("Vendor Name", Vendor::getName);
        TableColumn<Vendor, String> contact = vendorColumn("Contact", Vendor::getContactInfo);

        vendors.getColumns().setAll(vid, vname, contact);
        vendors.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vendors.setPlaceholder(new Label("No vendors found"));

        itemSearch.textProperty().addListener((obs, old, value) -> filterItems(value));
        vendorSearch.textProperty().addListener((obs, old, value) -> filterVendors(value));
    }

    private TableColumn<Item, String> itemColumn(String title, java.util.function.Function<Item, String> f) {
        TableColumn<Item, String> c = new TableColumn<>(title);
        c.setCellValueFactory(x -> new SimpleStringProperty(f.apply(x.getValue())));
        return c;
    }

    private TableColumn<Vendor, String> vendorColumn(String title, java.util.function.Function<Vendor, String> f) {
        TableColumn<Vendor, String> c = new TableColumn<>(title);
        c.setCellValueFactory(x -> new SimpleStringProperty(f.apply(x.getValue())));
        return c;
    }

    private void filterItems(String query) {
        if (items.getItems() instanceof FilteredList<Item> filtered) {
            filtered.setPredicate(x -> query == null || query.isBlank()
                || x.getName().toLowerCase().contains(query.toLowerCase())
                || String.valueOf(x.getId()).equals(query.trim()));
        }
    }

    private void filterVendors(String query) {
        if (vendors.getItems() instanceof FilteredList<Vendor> filtered) {
            filtered.setPredicate(x -> query == null || query.isBlank()
                || x.getName().toLowerCase().contains(query.toLowerCase())
                || x.getContactInfo().toLowerCase().contains(query.toLowerCase()));
        }
    }

    private boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private String[] form(String title, String... labels) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(18));

        TextField[] fields = new TextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            fields[i] = new TextField();
            fields[i].setPrefWidth(300);
            grid.add(new Label(labels[i]), 0, i);
            grid.add(fields[i], 1, i);
        }
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK
            ? java.util.Arrays.stream(fields).map(TextField::getText).toArray(String[]::new) : null;
    }

    private void addItem() {
        String[] f = form("Add Item", "Item name", "Initial quantity", "Vendor ID (optional)");
        if (f == null) return;
        try {
            String name = require(f[0], "Item name");
            int qty = positiveOrZero(f[1], "Initial quantity");
            int vendor = positiveOrZero(f[2], "Vendor ID");
            service.addItem(new Item(0, name, qty, vendor));
            refresh();
            status.setText("Item added successfully.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void editItem() {
        Item x = items.getSelectionModel().getSelectedItem();
        if (x == null) { showError("Select an item first."); return; }
        String[] f = form("Edit Item", "Item name", "Quantity", "Vendor ID (optional)");
        if (f == null) return;
        try {
            x.setName(require(f[0], "Item name"));
            x.setQuantity(positiveOrZero(f[1], "Quantity"));
            x.setVendorId(positiveOrZero(f[2], "Vendor ID"));
            service.updateItem(x);
            refresh();
            status.setText("Item updated successfully.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void deleteItem() {
        Item x = items.getSelectionModel().getSelectedItem();
        if (x == null) { showError("Select an item first."); return; }
        if (!confirm("Delete Item", "Delete "" + x.getName() + ""? This cannot be undone.")) return;
        try { service.removeItem(x.getId()); refresh(); status.setText("Item deleted."); }
        catch (Exception e) { showError(e.getMessage()); }
    }

    private void stock(String type) {
        Item x = items.getSelectionModel().getSelectedItem();
        if (x == null) { showError("Select an item first."); return; }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(type.equals("IN") ? "Stock In" : "Stock Out");
        dialog.setHeaderText(x.getName() + "  •  Current stock: " + x.getQuantity());
        dialog.setContentText("Quantity:");
        dialog.showAndWait().ifPresent(value -> {
            try {
                int qty = positive(value, "Quantity");
                service.adjustStock(x.getId(), qty, type);
                refresh();
                status.setText(type.equals("IN") ? "Stock added successfully." : "Stock removed successfully.");
            } catch (Exception e) { showError(e.getMessage()); }
        });
    }

    private void addVendor() {
        String[] f = form("Add Vendor", "Vendor name", "Contact information");
        if (f == null) return;
        try {
            service.addVendor(new Vendor(0, require(f[0], "Vendor name"), require(f[1], "Contact information")));
            refresh();
            status.setText("Vendor added successfully.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void editVendor() {
        Vendor x = vendors.getSelectionModel().getSelectedItem();
        if (x == null) { showError("Select a vendor first."); return; }
        String[] f = form("Edit Vendor", "Vendor name", "Contact information");
        if (f == null) return;
        try {
            x.setName(require(f[0], "Vendor name"));
            x.setContactInfo(require(f[1], "Contact information"));
            service.updateVendor(x);
            refresh();
            status.setText("Vendor updated successfully.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void deleteVendor() {
        Vendor x = vendors.getSelectionModel().getSelectedItem();
        if (x == null) { showError("Select a vendor first."); return; }
        if (!confirm("Delete Vendor", "Delete "" + x.getName() + ""?")) return;
        try { service.removeVendor(x.getId()); refresh(); status.setText("Vendor deleted."); }
        catch (Exception e) { showError(e.getMessage()); }
    }

    private void refresh() {
        try {
            List<Item> itemList = service.getAllItems();
            List<Vendor> vendorList = service.getAllVendors();
            FilteredList<Item> filteredItems = new FilteredList<>(FXCollections.observableArrayList(itemList), x -> true);
            FilteredList<Vendor> filteredVendors = new FilteredList<>(FXCollections.observableArrayList(vendorList), x -> true);
            items.setItems(filteredItems);
            vendors.setItems(filteredVendors);
            filterItems(itemSearch.getText());
            filterVendors(vendorSearch.getText());

            itemCount.setText(String.valueOf(itemList.size()));
            vendorCount.setText(String.valueOf(vendorList.size()));
            lowStockCount.setText(String.valueOf(service.getLowStockItems(5).size()));
            totalUnits.setText(String.valueOf(itemList.stream().mapToInt(Item::getQuantity).sum()));
            status.setText("Last refreshed successfully.");
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private String require(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required.");
        return value.trim();
    }

    private int positive(String value, String field) {
        int n = Integer.parseInt(require(value, field));
        if (n <= 0) throw new IllegalArgumentException(field + " must be greater than zero.");
        return n;
    }

    private int positiveOrZero(String value, String field) {
        if (value == null || value.isBlank()) return 0;
        int n = Integer.parseInt(value.trim());
        if (n < 0) throw new IllegalArgumentException(field + " cannot be negative.");
        return n;
    }

    private void showError(String message) {
        String m = message == null || message.isBlank() ? "An unexpected error occurred." : message;
        status.setText("Error: " + m);
        new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait();
    }
}
