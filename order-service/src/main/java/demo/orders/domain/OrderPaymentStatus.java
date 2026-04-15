package demo.orders.domain;

import lombok.Getter;

@Getter
public enum OrderPaymentStatus {
    PAYMENT_PENDING("PAYMENT_PENDING"),
    PAYMENT_APPROVED("PAYMENT_APPROVED"),
    PAYMENT_FAILED("PAYMENT_FAILED");

    private String value;

    OrderPaymentStatus(String value) {
    }
}
