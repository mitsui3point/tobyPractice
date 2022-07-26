package springbook.exception.example.domain;

import java.math.BigDecimal;

public class Account {
    // 잔고
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
