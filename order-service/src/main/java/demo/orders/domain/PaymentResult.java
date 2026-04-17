package demo.orders.domain;

import lombok.Getter;

@Getter
public enum PaymentResult {
    UNKNOWN("UNKNOWN"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String value;

    PaymentResult(String value) {
    }
}
