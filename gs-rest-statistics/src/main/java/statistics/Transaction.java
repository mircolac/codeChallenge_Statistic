package statistics;

public class Transaction {

    private final Double amount;
    private final long timestamp;

    public Transaction(long timestamp, Double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }
    

    public long getTimestamp() {
        return timestamp;
    }

    public Double getAmount() {
        return amount;
    }
}