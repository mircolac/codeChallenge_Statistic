package statistics;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
	
    @JsonProperty("timestamp")
    private final long timestamp;

    @JsonProperty("amount")
    private final Double amount;

    public Transaction(long timestamp, Double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }
    
    public Transaction(String timestamp, String amount) {
        this.timestamp = Long.parseLong(timestamp);
        this.amount = Double.parseDouble(amount);
    }

    
    public long getTimestamp() {
        return timestamp;
    }

    public Double getAmount() {
        return amount;
    }
}