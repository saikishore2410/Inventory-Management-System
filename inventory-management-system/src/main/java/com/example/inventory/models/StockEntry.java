public class StockEntry {
    private int id;
    private int itemId;
    private int quantity;
    private String transactionType; // e.g., "IN" for stock entry, "OUT" for stock removal

    public StockEntry(int id, int itemId, int quantity, String transactionType) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.transactionType = transactionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}