public class Transaction {
    private int transactionId;
    private int orderId;
    private double totalAmount;

    public Transaction(int transactionId, int orderId, double totalAmount) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
}
