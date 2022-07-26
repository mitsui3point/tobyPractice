package springbook.exception.example.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends Exception {
    private BigDecimal availFunds;
    public InsufficientBalanceException() {}
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
    public InsufficientBalanceException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public InsufficientBalanceException(Throwable cause) {
        super(cause);
    }
    public InsufficientBalanceException(BigDecimal availFunds) {
        this.availFunds = availFunds;
    }
    public BigDecimal getAvailFunds() {
        return this.availFunds;
    }
    public String getAvailFundsAlertMsg() {
        return "계좌 잔고가 부족합니다. 잔고는 " + this.availFunds + " 까지만 인출 가능합니다.";
    }
}
