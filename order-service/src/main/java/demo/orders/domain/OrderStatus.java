package demo.orders.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED"),
    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private String value;

    OrderStatus(String value) {
    }
}
