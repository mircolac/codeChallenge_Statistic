package statistics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
	
    private final long timestamp;

    private final Double amount;
    
    @JsonCreator    
    public Transaction(@JsonProperty("timestamp")Long timestamp,@JsonProperty("amount") Double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public Double getAmount() {
        return amount;
    }
    
    public String toString() {
    	return "timestamp: "+timestamp+ ", amount: "+amount;
    }
}