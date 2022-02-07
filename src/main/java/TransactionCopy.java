import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;
import java.util.Optional;

public class TransactionCopy {
    @CsvBindByName
    private String transactionId;
    @CsvBindByName
    private String fromAccountId;
    @CsvBindByName
    private String toAccountId;
    @CsvBindByName
    private LocalDateTime createdAt;
    @CsvBindByName
    private Float amount;
    @CsvBindByName
    private String transactionType;
    @CsvBindByName(required = false)
    private Optional<String> relatedTransaction;

    public TransactionCopy(String transactionId, String fromAccountId, String toAccountId,
                       LocalDateTime createdAt, Float amount, String transactionType, Optional<String> relatedTransaction) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.createdAt = createdAt;
        this.amount = amount;
        this.transactionType = transactionType;
        this.relatedTransaction = relatedTransaction;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Optional<String> getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(Optional<String> relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }
}

